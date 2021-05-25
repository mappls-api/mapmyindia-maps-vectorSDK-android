package com.mapmyindia.sdk.demo.kotlin.plugin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapView.OnMapChangedListener
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mapbox.turf.TurfMeasurement
import com.mapmyindia.sdk.demo.R
import com.mmi.services.api.directions.models.DirectionsRoute
import com.mmi.services.utils.Constants

/**
 ** Created by Saksham on 01-05-2021.
 **/
class TrackingPlugin(private val mapView: MapView, private val mapmyIndiaMap: MapboxMap) : OnMapChangedListener {


    private var car: Car? = null
    private var markerFeature: Feature? = null
    private var polylineFeature: Feature? = null
    private var isClearAllCallBacks = false


    companion object {
        private const val PROPERTY_BEARING = "bearing"
        private const val CAR = "car"
        private const val CAR_LAYER = "car-layer"
        private const val CAR_SOURCE = "car-source"
        private const val POLYLINE_LAYER = "polyline-layer"
        private const val POLYLINE_SOURCE = "polyline-source"
    }

    init {
        mapmyIndiaMap.addImage(CAR, BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(mapView.context, R.drawable.ic_bike_icon_grey)))
        initialisePolylineLayerAndSource()
        initialiseMarkerLayerAndSource()
        mapView.addOnMapChangedListener(this)
    }

    override fun onMapChanged(change: Int) {
        if (change == MapView.DID_FINISH_LOADING_STYLE) {
            mapmyIndiaMap.addImage(CAR, BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(mapView.context, R.drawable.ic_bike_icon_grey)))
            updatePolylineSource()
            updateMarkerSource()
        }
    }

    fun addMarker(point: Point?) {
        markerFeature = Feature.fromGeometry(point)
        markerFeature?.addNumberProperty(PROPERTY_BEARING, 0)
        updateMarkerSource()
    }

    fun updatePolyline(route: DirectionsRoute?) {
        if (route != null && route.geometry() != null) {
            polylineFeature = Feature.fromGeometry(LineString.fromPolyline(route.geometry()!!, Constants.PRECISION_6))
            updatePolylineSource()
        }
    }

    /**
     * Animate Marker from current point to next point
     */
    fun animateCar(start: Point, end: Point) {
        isClearAllCallBacks = false
        val startLatLng = LatLng(start.latitude(), start.longitude())
        val nextLatLng = LatLng(end.latitude(), end.longitude())
        car = Car(startLatLng, nextLatLng)
        val valueAnimator = ValueAnimator.ofObject(LatLngEvaluator(), startLatLng, nextLatLng)
        valueAnimator.addUpdateListener { animation ->
            if (!isClearAllCallBacks) {
                if (car?.current == null || car?.current != nextLatLng) {
                    val latLng = animation.animatedValue as LatLng
                    markerFeature = Feature.fromGeometry(Point.fromLngLat(latLng.longitude, latLng.latitude))
                    car?.current = latLng
                    markerFeature?.addNumberProperty(PROPERTY_BEARING, Car.getBearing(car!!.current, car!!.next))
                    updateMarkerSource()
                }
            }
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                if (!isClearAllCallBacks) {
                    markerFeature!!.properties()!!.addProperty(PROPERTY_BEARING, Car.getBearing(car?.current!!, car?.next!!))
                }
            }
        })

        valueAnimator.duration = 1000
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.start()
    }

    private fun updateMarkerSource() {
        if (mapmyIndiaMap.getSource(CAR_SOURCE) == null) {
            initialiseMarkerLayerAndSource()
            return
        }
        val source = mapmyIndiaMap.getSource(CAR_SOURCE) as GeoJsonSource?
        source!!.setGeoJson(markerFeature)
    }

    private fun updatePolylineSource() {
        if (mapmyIndiaMap.getSource(POLYLINE_SOURCE) == null) {
            initialisePolylineLayerAndSource()
            return
        }
        val source = mapmyIndiaMap.getSource(POLYLINE_SOURCE) as GeoJsonSource?
        source!!.setGeoJson(polylineFeature)
    }

    /**
     * Initialise the marker
     */
    private fun initialiseMarkerLayerAndSource() {
        if (mapmyIndiaMap.getSource(CAR_SOURCE) == null) {
            if (markerFeature == null) {
                mapmyIndiaMap.addSource(GeoJsonSource(CAR_SOURCE))
            } else {
                mapmyIndiaMap.addSource(GeoJsonSource(CAR_SOURCE, markerFeature))
            }
        }
        if (mapmyIndiaMap.getLayer(CAR_LAYER) == null) {
            //Symbol layer for car
            val symbolLayer = SymbolLayer(CAR_LAYER, CAR_SOURCE)
            symbolLayer.withProperties(
                    PropertyFactory.iconImage(CAR),
                    PropertyFactory.iconRotate(Expression.get(PROPERTY_BEARING)),
                    PropertyFactory.iconAllowOverlap(true),
                    PropertyFactory.iconIgnorePlacement(true),
                    PropertyFactory.iconRotationAlignment(Property.ICON_ROTATION_ALIGNMENT_MAP)
            )
            mapmyIndiaMap.addLayerAbove(symbolLayer, POLYLINE_LAYER)
        }
    }

    private fun initialisePolylineLayerAndSource() {
        if (mapmyIndiaMap.getSource(POLYLINE_SOURCE) == null) {
            mapmyIndiaMap.addSource(GeoJsonSource(POLYLINE_SOURCE))
        }
        if (mapmyIndiaMap.getLayer(POLYLINE_LAYER) == null) {
            val lineLayer = LineLayer(POLYLINE_LAYER, POLYLINE_SOURCE)
            lineLayer.setProperties(
                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                    PropertyFactory.lineColor(Color.BLACK),
                    PropertyFactory.lineWidth(4f)
            )
            mapmyIndiaMap.addLayer(lineLayer)
        }
    }

    /**
     * Evaluator for LatLng pairs
     */
    private class LatLngEvaluator : TypeEvaluator<LatLng> {
        private val latLng = LatLng()
        override fun evaluate(fraction: Float, startValue: LatLng, endValue: LatLng): LatLng {
            latLng.latitude = (startValue.latitude
                    + (endValue.latitude - startValue.latitude) * fraction)
            latLng.longitude = (startValue.longitude
                    + (endValue.longitude - startValue.longitude) * fraction)
            return latLng
        }
    }

    private class Car internal constructor(internal var current: LatLng, internal var next: LatLng) {
        companion object {
            /**
             * Get the bearing between two points
             *
             * @param from Current point
             * @param to   Next Point
             * @return bearing value in degree
             */
            internal fun getBearing(from: LatLng, to: LatLng): Float {
                return TurfMeasurement.bearing(
                        Point.fromLngLat(from.longitude, from.latitude),
                        Point.fromLngLat(to.longitude, to.latitude)
                ).toFloat()
            }
        }
    }
}
