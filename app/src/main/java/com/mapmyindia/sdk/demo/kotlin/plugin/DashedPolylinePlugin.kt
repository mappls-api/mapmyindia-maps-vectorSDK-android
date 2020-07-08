package com.mapmyindia.sdk.demo.kotlin.plugin

import android.graphics.Color
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

/**
 * Created by Saksham on 20/9/19.
 */
class DashedPolylinePlugin(private val mapmyIndiaMap: MapboxMap, mapView: MapView) : MapView.OnMapChangedListener {

    private var featureCollection: FeatureCollection? = null
    private val mStyle: Style? = null
    private var latLngs: List<LatLng>? = null

    private val widthDash = 4f
    private val gapDash = 6f

    private var polylineSource: GeoJsonSource? = null

    init {

        updateSource()
        mapView.addOnMapChangedListener(this)
    }

    /**
     * Add list of positions on Feature
     *
     * @param latLngs list of points
     */
    fun createPolyline(latLngs: List<LatLng>) {
        this.latLngs = latLngs
        val points = ArrayList<Point>()
        for (latLng in latLngs) {
            points.add(Point.fromLngLat(latLng.longitude, latLng.latitude))
        }
        featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(LineString.fromLngLats(points)))

        initSources(featureCollection!!)
    }

    /**
     * Add various sources to the map.
     */
    private fun initSources(featureCollection: FeatureCollection) {
        if(mapmyIndiaMap.getSource(UPPER_SOURCE_ID) == null) {
            polylineSource = GeoJsonSource(UPPER_SOURCE_ID, featureCollection,
                    GeoJsonOptions().withLineMetrics(true).withBuffer(2))
            mapmyIndiaMap.addSource(polylineSource!!)
        } else {
            updateSource()
        }
    }

    /**
     * Update Source of the polyline
     */
    private fun updateSource() {
        val source = mapmyIndiaMap.getSource(UPPER_SOURCE_ID) as GeoJsonSource?
        if (source == null) {
            create()
            return
        }
        if (featureCollection != null) {
            polylineSource!!.setGeoJson(featureCollection)
        }
    }
    /**
     * Remove dotted line
     */
   fun clear(){
        featureCollection = FeatureCollection.fromFeatures(ArrayList())
        updateSource()
    }

    /**
     * Add Layer on map
     */
    private fun create() {
        if(mapmyIndiaMap.getLayer(LAYER_ID) == null) {
            mapmyIndiaMap.addLayer(LineLayer(LAYER_ID, UPPER_SOURCE_ID).withProperties(
                    lineColor(Color.RED),
                    lineDasharray(arrayOf<Float>(widthDash, gapDash)),
                    lineCap(Property.LINE_CAP_ROUND),
                    lineJoin(Property.LINE_JOIN_BEVEL),
                    lineWidth(4f)))
        }
    }


    override fun onMapChanged(i: Int) {
        if (i == MapView.DID_FINISH_LOADING_STYLE) {
            updateSource()
            createPolyline(latLngs!!)
        }
    }

    companion object {
        private val UPPER_SOURCE_ID = "line-source-upper-id"
        private val LAYER_ID = "line-layer-upper-id"
    }
}
