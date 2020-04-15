package com.mapmyindia.sdk.demo.kotlin.plugin

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.expressions.Expression.get
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils

/**
 * Created by Saksham on 3/9/19.
 */
class MarkerPlugin(private val mapmyIndiaMap: MapboxMap, val mapView: MapView) : MapView.OnMapChangedListener {

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
    }

    init {
        updateState()
        mapView.addOnMapChangedListener(this)
        initialiseForDraggingMarker()
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
        val source: GeoJsonSource? = mapmyIndiaMap.getSource(SOURCE_ID) as GeoJsonSource?
        if(source == null) {
            initialise()
            return
        }

        if (feature != null)
            markerSource?.setGeoJson(feature)
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
        feature!!.properties()!!.addProperty(PROPERTY_ROTATION, rotate)
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
        mapmyIndiaMap.addImage(ICON_ID, BitmapUtils.getBitmapFromDrawable(icon))
        if(mapmyIndiaMap.getSource(SOURCE_ID) == null) {
            markerSource = GeoJsonSource(SOURCE_ID, feature)
            mapmyIndiaMap.addSource(markerSource!!)
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
        feature = Feature.fromGeometry(Point.fromLngLat(position.longitude, position.latitude))
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

    /**
     * Add symbol layer on map
     */
    private fun initialise() {
        if(mapmyIndiaMap.getLayer(LAYER_ID) == null) {
            val symbolLayer: SymbolLayer = SymbolLayer(LAYER_ID, SOURCE_ID)
                    .withProperties(
                            iconImage(ICON_ID),
                            iconRotate(get(PROPERTY_ROTATION)),
                            iconAllowOverlap(true),
                            iconIgnorePlacement(true)
                    )

            mapmyIndiaMap.addLayer(symbolLayer)
        }
    }

    override fun onMapChanged(change: Int) {
        if(change == MapView.DID_FINISH_LOADING_STYLE) {
            updateState()
            addMarker(position!!)
        }
    }

    /**
     * Remove all callbacks
     */
    fun removeCallbacks() {
        isRemoveCallback = true
    }

    interface OnMarkerDraggingListener {
        fun onMarkerDragging(position: LatLng)

    }
}