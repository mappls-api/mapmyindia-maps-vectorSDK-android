package com.mapmyindia.sdk.demo.kotlin.plugin

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.geojson.Feature
import com.mapmyindia.sdk.geojson.Point
import com.mapmyindia.sdk.maps.MapView
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.Style
import com.mapmyindia.sdk.maps.annotations.BubbleLayout
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.style.expressions.Expression
import com.mapmyindia.sdk.maps.style.expressions.Expression.get
import com.mapmyindia.sdk.maps.style.layers.Property
import com.mapmyindia.sdk.maps.style.layers.PropertyFactory.*
import com.mapmyindia.sdk.maps.style.layers.SymbolLayer
import com.mapmyindia.sdk.maps.style.sources.GeoJsonSource
import com.mapmyindia.sdk.maps.utils.BitmapUtils
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by Saksham on 3/9/19.
 */
class MarkerPlugin(private val mapmyIndiaMap: MapmyIndiaMap, val mapView: MapView) : MapView.OnDidFinishLoadingStyleListener, MapmyIndiaMap.OnMapClickListener {

    private var feature : Feature? = null
    private var position : LatLng? = null
    private var isRemoveCallback : Boolean = false
    var icon : Drawable? = null
    private var markerSource : GeoJsonSource? = null
    private var isDraggable: Boolean = false
    private var isMarkerPosition: Boolean = false
    private var onMarkerDraggingListener: OnMarkerDraggingListener? = null


    companion object {
        private const val ICON_ID : String = "ICON_ID"
        private const val SOURCE_ID :String = "SOURCE_ID"
        private const val LAYER_ID : String = "LAYER_ID"
        private const val PROPERTY_ROTATION : String = "rotation"
        private const val PROPERTY_SELECTED = "property-selected"
        private const val TITLE = "title"
        private const val PROPERTY_NAME = "name"
        private const val FILTER_TEXT = "filter_text"
        private const val PROPERTY_ADDRESS = "address"

        private const val INFOWINDOW_LAYER_ID = "INFOWINDOW_LAYER_ID"

        /**
         * Calculate the sample size of the image
         *
         * @param options   BitmapFactory options
         * @param reqWidth  required width
         * @param reqHeight required height
         * @return calculated sample size
         */
        private fun calculateInSampleSize(
                options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {

                val halfHeight = height / 2
                val halfWidth = width / 2

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            return inSampleSize
        }
    }

    init {
        updateState()
        mapView.addOnDidFinishLoadingStyleListener(this)
        initialiseForDraggingMarker()
        mapmyIndiaMap.addOnMapClickListener(this)
    }

    fun setOnMarkerDraggingListener(onMarkerDraggingListener: OnMarkerDraggingListener) {
        this.onMarkerDraggingListener = onMarkerDraggingListener
    }

    /**
     * Add touch listener for dragging the marker
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initialiseForDraggingMarker() {
        mapView.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
                if(isDraggable) {
                    if(motionEvent!!.action == MotionEvent.ACTION_DOWN) {
                        isMarkerPosition = isMarkerPosition(PointF(motionEvent.x, motionEvent.y))
                    } else if(motionEvent.action == MotionEvent.ACTION_MOVE) {
                        if(isMarkerPosition) {
                            val position: LatLng = mapmyIndiaMap.projection.fromScreenLocation(PointF(motionEvent.x, motionEvent.y))
                            updateMarkerPosition(position)
                            onMarkerDraggingListener?.onMarkerDragging(position)
                        }
                    }
                }
                return isMarkerPosition && isDraggable
            }
        })
    }

    /**
     * If true than marker is draggable
     */
    fun draggable(isDraggable: Boolean) {
        this.isDraggable = isDraggable
    }

    /**
     * Check that the point is on the marker
     *
     * @param pointF the point on screen touch
     * @return check is pointF is the marker
     */
    private fun isMarkerPosition(pointF: PointF) : Boolean{
        val features: List<Feature> = mapmyIndiaMap.queryRenderedFeatures(pointF, LAYER_ID)
        return features.isNotEmpty()
    }

    /**
     * Update the state of the marker
     */
    private fun updateState() {
        mapmyIndiaMap.getStyle {
            val source: GeoJsonSource? = it.getSource(SOURCE_ID) as GeoJsonSource?
            if(source == null) {
                initialise(it)
                return@getStyle
            }

            if (feature != null)
                source.setGeoJson(feature)
        }
    }

    /**
     * Start rotation of marker
     */
    fun startRotation() {
        isRemoveCallback = false

        val valueAnimatorRotate : ValueAnimator = ValueAnimator.ofFloat(0f, 360f)
        valueAnimatorRotate.duration = 1000
        valueAnimatorRotate.addUpdateListener {
            if(!isRemoveCallback) {
                val rotate: Float = it!!.animatedValue as Float
                rotate(rotate)
            }
        }
        valueAnimatorRotate.start()
    }


    /**
     * Rotate the marker
     *
     * @param rotate bearing
     */
    private fun rotate(rotate : Float) {
        feature?.properties()?.addProperty(PROPERTY_ROTATION, rotate)
        updateState()
    }

    /**
     * Add image on map for marker and source on map
     *
     * @param position position of the marker
     */
    fun addMarker(position: LatLng) {
        isRemoveCallback = false
        this.position = position
        feature = Feature.fromGeometry(Point.fromLngLat(position.longitude, position.latitude))
        feature?.addBooleanProperty(PROPERTY_SELECTED, false)
        mapmyIndiaMap.getStyle {
            it.addImage(ICON_ID, BitmapUtils.getBitmapFromDrawable(icon)!!)
            if(it.getSource(SOURCE_ID) == null) {
                markerSource = GeoJsonSource(SOURCE_ID, feature)
                it.addSource(markerSource!!)
            }
        }
    }

    /**
     * Start transition of the marker
     * Only move from start point to end point
     *
     * @param latLngStart Start Point
     * @param latLngEnd End point
     */
    fun startTransition(latLngStart: LatLng, latLngEnd: LatLng) {
        isRemoveCallback = false
        val valueAnimatorTransition: ValueAnimator = ValueAnimator.ofObject(LatLngEvaluator(), latLngStart, latLngEnd)
        valueAnimatorTransition.duration = 15000
        valueAnimatorTransition.addUpdateListener {
            if(!isRemoveCallback) {
                val latLng: LatLng = it.animatedValue as LatLng
                if (!mapmyIndiaMap.projection.visibleRegion.latLngBounds.contains(latLng)) {
                    mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0))
                }
                updateMarkerPosition(latLng)
            }
        }
        valueAnimatorTransition.start()
    }


    /**
     * Update the position of the marker
     *
     * @param position updated position of the marker
     */
    private fun updateMarkerPosition(position: LatLng) {
        var name: String? = null
        var description: String? = null
        var isSelected = false
        if(feature?.hasProperty(PROPERTY_NAME)!!) {
            name = feature?.getStringProperty(PROPERTY_NAME)
        }

        if(feature?.hasProperty(PROPERTY_ADDRESS)!!) {
            description = feature?.getStringProperty(PROPERTY_ADDRESS)
        }
        if(feature?.hasProperty(PROPERTY_SELECTED)!!) {
            isSelected = feature?.getBooleanProperty(PROPERTY_SELECTED)?:false
        }
        feature = Feature.fromGeometry(Point.fromLngLat(position.longitude, position.latitude))
        if(name != null) {
            feature?.addStringProperty(PROPERTY_NAME, name)
        }
        if(description != null) {
            feature?.addStringProperty(PROPERTY_ADDRESS, description)
        }
        feature?.addBooleanProperty(PROPERTY_SELECTED, isSelected)
        updateState()
    }

    /**
     * LatLng Evaluator
     */
    private class LatLngEvaluator: TypeEvaluator<LatLng> {

        private var latLng: LatLng = LatLng()

        override fun evaluate(fraction: Float, startValue: LatLng?, endValue: LatLng?): LatLng {

            latLng.latitude = startValue!!.latitude + ((endValue!!.latitude - startValue.latitude) * fraction)
            latLng.longitude = startValue.longitude + ((endValue.longitude - startValue.longitude) * fraction)

            return latLng
        }

    }

    fun addTitle(title: String) {
        feature?.properties()?.addProperty(PROPERTY_NAME, title)
    }


    fun addDescription(title: String) {
        feature?.properties()?.addProperty(PROPERTY_ADDRESS, title)
    }

    /**
     * Add symbol layer on map
     */
    private fun initialise(style: Style) {
        if(style.getLayer(LAYER_ID) == null) {
            val symbolLayer: SymbolLayer = SymbolLayer(LAYER_ID, SOURCE_ID)
                    .withProperties(
                            iconImage(ICON_ID),
                            iconRotate(get(PROPERTY_ROTATION)),
                            iconAllowOverlap(true),
                            iconIgnorePlacement(true)
                    )

            style.addLayer(symbolLayer)
        }

        if(style.getLayer(INFOWINDOW_LAYER_ID) == null) {
            val symbolLayerInfoWindow = SymbolLayer(INFOWINDOW_LAYER_ID, SOURCE_ID)
                    .withProperties(
                            /* show image with id title based on the value of the name feature property */
                            iconImage("{name}"),

                            /* set anchor of icon to bottom-left */
                            iconAnchor(Property.ICON_ANCHOR_BOTTOM),

                            /* all info window and marker image to appear at the same time*/
                            iconAllowOverlap(true),

                            /* offset the info window to be above the marker */
                            iconOffset(arrayOf(-2f, -25f))
                    )
                    /* setData a filter to show only when selected feature property is true */
                    .withFilter(Expression.eq(get(PROPERTY_SELECTED), Expression.literal(true)))

            style.addLayer(symbolLayerInfoWindow)
        }
    }

    override fun onDidFinishLoadingStyle() {
        updateState()
        addMarker(position!!)
    }

    /**
     * Remove all callbacks
     */
    fun removeCallbacks() {
        isRemoveCallback = true
    }


    /**
     * Generate Info window Icon
     */
    private class GenerateViewIconTask @JvmOverloads internal constructor(activity: MarkerPlugin, private val refreshSource: Boolean = true) : AsyncTask<Feature, Void, HashMap<String, Bitmap>>() {

        private val viewMap = HashMap<String, View>()
        private val activityRef: WeakReference<MarkerPlugin>

        init {
            this.activityRef = WeakReference(activity)
        }

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params: Feature): HashMap<String, Bitmap>? {
            val activity = activityRef.get()
            if (activity != null) {
                val imagesMap = HashMap<String, Bitmap>()
                val inflater = LayoutInflater.from(activity.mapView.context)

                val featureCollection = params[0]


                    val bubbleLayout = inflater.inflate(R.layout.symbol_layer_info_window_layout_callout, null) as BubbleLayout
                    if (featureCollection.hasProperty(PROPERTY_NAME)) {
                        val name1 = featureCollection.getStringProperty(PROPERTY_NAME)
                        val titleTextView: TextView = bubbleLayout.findViewById(R.id.info_window_title)
                        titleTextView.text = name1

                        if(featureCollection.hasProperty(PROPERTY_ADDRESS)) {
                            val address = featureCollection.getStringProperty(PROPERTY_ADDRESS)
                            val addressTextView = bubbleLayout.findViewById<TextView>(R.id.info_window_description)
                            addressTextView.text = address
                        }


                        //                    String style = feature.getStringProperty(PROPERTY_CAPITAL);
                        //                    TextView descriptionTextView = bubbleLayout.findViewById(R.id.info_window_description);
                        //                    descriptionTextView.setText(
                        //                            String.format("capital", style));

                        val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                        bubbleLayout.measure(measureSpec, measureSpec)

                        val measuredWidth = bubbleLayout.measuredWidth.toFloat()

                        bubbleLayout.arrowPosition = measuredWidth / 2 - 5

                        val bitmap = SymbolGenerator.generate(bubbleLayout)
                        imagesMap[name1] = bitmap
                        viewMap[name1] = bubbleLayout
                    }


                return imagesMap
            } else {
                return null
            }
        }
        override fun onPostExecute(bitmapHashMap: HashMap<String, Bitmap>?) {
            super.onPostExecute(bitmapHashMap)
            val activity = activityRef.get()
            if (activity != null && bitmapHashMap != null) {
                activity.setImageGenResults(bitmapHashMap)
                if (refreshSource) {
                    activity.updateState()
                }
            }
        }
    }

    /**
     * Add images on the map
     *
     * @param imageMap Hashmap of images
     */
    private fun setImageGenResults(imageMap: HashMap<String, Bitmap>) {
        mapmyIndiaMap.getStyle {
            it.addImages(imageMap)
        }
    }




    /**
     * Utility class to generate Bitmaps for Symbol.
     *
     *
     * Bitmaps can be added to the map with
     *
     */
    private object SymbolGenerator {

        /**
         * Generate a Bitmap from an Android SDK View.
         *
         * @param view the View to be drawn to a Bitmap
         * @return the generated bitmap
         */
        fun generate(view: View): Bitmap {


            val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(measureSpec, measureSpec)

            val measuredWidth = view.measuredWidth
            val measuredHeight = view.measuredHeight

            view.layout(0, 0, measuredWidth, measuredHeight)
            val bitmap1 = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)


            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inBitmap = bitmap1
            //            BitmapFactory.decodeByteArray(bitmap1.getNinePatchChunk(), 0, view.getHeight(), options);
            val sampleSize = calculateInSampleSize(options, measuredWidth, measuredHeight)
            options.inSampleSize = sampleSize
            //            options.inBitmap = bitmap1;
            val bitmap = options.inBitmap
            bitmap.eraseColor(Color.TRANSPARENT)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }
    }


    interface OnMarkerDraggingListener {
        fun onMarkerDragging(position: LatLng)

    }

    override fun onMapClick(latLng: LatLng): Boolean {
        if(feature?.hasProperty(PROPERTY_SELECTED)!!) {
            if(feature?.getBooleanProperty(PROPERTY_SELECTED) == true) {
                feature?.addBooleanProperty(PROPERTY_SELECTED, false)
                updateState()
                return false
            }
        }
        val features = mapmyIndiaMap.queryRenderedFeatures(this.mapmyIndiaMap.projection.toScreenLocation(latLng), LAYER_ID)
        if (features.isNotEmpty()) {

            feature = features[0]

            feature?.addBooleanProperty(PROPERTY_SELECTED, true)
            GenerateViewIconTask(this).execute(feature)

        }
        return false
    }
}