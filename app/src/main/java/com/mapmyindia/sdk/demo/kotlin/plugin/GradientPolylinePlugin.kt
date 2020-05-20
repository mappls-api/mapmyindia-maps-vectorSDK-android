package com.mapmyindia.sdk.demo.kotlin.plugin

import android.graphics.Color
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import java.util.*

/**
 * Created by Saksham on 20/9/19.
 */
class GradientPolylinePlugin(private val mapmyIndiaMap: MapboxMap, mapView: MapView) : MapView.OnMapChangedListener {

    private var featureCollection: FeatureCollection? = null
    private var startColor = Color.parseColor("#3dd2d0")
    private var endColor = Color.parseColor("#FF20d0")
    private var latLngs: List<LatLng>? = null
    private var polylineSource: GeoJsonSource? = null

    /**
     * Set start color of the gradient
     *
     * @param startColor starting color of the polyline
     */
    fun setStartColor(startColor: Int) {
        this.startColor = startColor
    }

    /**
     * Set end color of the gradient
     *
     * @param endColor end color of the polyline
     */
    fun setEndColor(endColor: Int) {
        this.endColor = endColor
    }

    init {

        updateSource()
        mapView.addOnMapChangedListener(this)
    }

    /**
     * Add feature to source
     *
     * @param latLngs List of points
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
     * Add Line layer to map
     */
    private fun create() {
        if(mapmyIndiaMap.getLayer(LAYER_ID) == null) {
            mapmyIndiaMap.addLayer(LineLayer(LAYER_ID, UPPER_SOURCE_ID).withProperties(
                    //                lineColor(Color.RED),
                    lineCap(Property.LINE_CAP_ROUND),
                    lineJoin(Property.LINE_JOIN_BEVEL),
                    lineGradient(interpolate(
                            linear(), lineProgress(),
                            stop(0f, color(startColor)),
                            stop(1f, color(endColor)))),
                    lineWidth(4f)))
        }
    }


    /**
     * Add various sources to the map.
     */
    private fun initSources(featureCollection: FeatureCollection) {
        if(mapmyIndiaMap.getSource(UPPER_SOURCE_ID) == null) {
            polylineSource = GeoJsonSource(UPPER_SOURCE_ID, featureCollection,
                    GeoJsonOptions().withLineMetrics(true).withBuffer(2))
            mapmyIndiaMap.addSource(polylineSource!!)
        }
    }

    override fun onMapChanged(i: Int) {
        if (i == MapView.DID_FINISH_LOADING_STYLE) {
            updateSource()
            createPolyline(latLngs!!)
        }
    }

    /**
     * Update the source of the polyline
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


    companion object {
        private const val UPPER_SOURCE_ID = "line-source-upper-id"
        private const val LAYER_ID = "line-layer-upper-id"
    }
}
