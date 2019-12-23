package com.mapmyindia.sdk.demo.kotlin.plugin

import android.graphics.Color
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mmi.services.api.directions.DirectionsCriteria
import java.util.*

/**
 * Created by Saksham on 20/9/19.
 */
class DirectionPolylinePlugin(mapboxMap: MapboxMap, mapView: MapView, private var directionsCriteria: String?) : MapView.OnMapChangedListener {

    private var mapboxMap: MapboxMap? = null

    private var featureCollection: FeatureCollection? = null
    private var latLngs: List<LatLng>? = null

    private val widthDash = 5f
    private val gapDash = 5f

    private var polylineSource: GeoJsonSource? = null
    private var lineLayer: LineLayer? = null

    init {
        this.mapboxMap = mapboxMap
        this.mapboxMap = mapboxMap

        updateSource()
        mapView.addOnMapChangedListener(this)
    }

    /**
     * Add polyline features and set polyline property for walk and other
     *
     * @param latLngs list of points
     */
    fun createPolyline(latLngs: List<LatLng>) {
        this.latLngs = latLngs
        val points = ArrayList<Point>()
        for (latLng in latLngs) {
            points.add(Point.fromLngLat(latLng.longitude, latLng.latitude))
        }
        val features = Feature.fromGeometry(LineString.fromLngLats(points))

        if (directionsCriteria!!.equals(DirectionsCriteria.PROFILE_WALKING, ignoreCase = true)) {
            lineLayer!!.setProperties(lineDasharray(arrayOf(widthDash, gapDash)),
                    lineColor(Color.BLACK))
        } else {
            lineLayer!!.setProperties(lineDasharray(arrayOf(0f, 0f)),
                    lineColor(Color.parseColor("#3bb2d0")))
        }
        featureCollection = FeatureCollection.fromFeature(features)
        initSources(featureCollection!!)
    }

    /**
     * Update polyline features and set polyline property for walk and other
     *
     * @param directionsCriteria {"foot", "biking", "driving"}
     * @param latLngs list of points
     */
    fun updatePolyline(directionsCriteria: String, latLngs: List<LatLng>) {
        this.directionsCriteria = directionsCriteria
        val points = ArrayList<Point>()
        for (latLng in latLngs) {
            points.add(Point.fromLngLat(latLng.longitude, latLng.latitude))
        }
        val features = Feature.fromGeometry(LineString.fromLngLats(points))
        if (directionsCriteria.equals(DirectionsCriteria.PROFILE_WALKING, ignoreCase = true)) {
            lineLayer!!.setProperties(lineDasharray(arrayOf(widthDash, gapDash)),
                    lineColor(Color.BLACK))
        } else {
            lineLayer!!.setProperties(lineDasharray(arrayOf(0f, 0f)),
                    lineColor(Color.parseColor("#3bb2d0")))
        }

        featureCollection = FeatureCollection.fromFeature(features)
        updateSource()
    }

    /**
     * Add various sources to the map.
     */
    private fun initSources(featureCollection: FeatureCollection) {
        polylineSource = GeoJsonSource(UPPER_SOURCE_ID, featureCollection,
                GeoJsonOptions().withLineMetrics(true).withBuffer(2))
        mapboxMap!!.addSource(polylineSource!!)
    }

    /**
     * Update Source and GeoJson properties
     */
    private fun updateSource() {
        val source = mapboxMap!!.getSource(UPPER_SOURCE_ID) as GeoJsonSource?
        if (source == null) {
            create()
            return
        }
        if (featureCollection != null) {
            polylineSource!!.setGeoJson(featureCollection)
        }
    }

    /**
     * Add Line layer on map
     */
    private fun create() {
        lineLayer = LineLayer(LAYER_ID, UPPER_SOURCE_ID).withProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f))
        mapboxMap!!.addLayer(lineLayer!!)


        if (directionsCriteria!!.equals(DirectionsCriteria.PROFILE_WALKING, ignoreCase = true)) {
            lineLayer!!.setProperties(lineDasharray(arrayOf(gapDash, widthDash)))
        }
    }


    override fun onMapChanged(i: Int) {
        if (i == MapView.DID_FINISH_LOADING_STYLE) {
            updateSource()
            createPolyline(latLngs!!)
        }
    }

    companion object {
        private const val UPPER_SOURCE_ID = "line-source-upper-id"
        private const val LAYER_ID = "line-layer-upper-id"
    }
}