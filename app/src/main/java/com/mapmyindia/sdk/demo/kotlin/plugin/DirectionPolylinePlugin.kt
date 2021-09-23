package com.mapmyindia.sdk.demo.kotlin.plugin

import android.graphics.Color
import com.mapmyindia.sdk.geojson.Feature

import com.mapmyindia.sdk.geojson.FeatureCollection
import com.mapmyindia.sdk.geojson.LineString
import com.mapmyindia.sdk.geojson.Point
import com.mapmyindia.sdk.maps.MapView
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.Style
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.style.layers.LineLayer
import com.mapmyindia.sdk.maps.style.layers.Property
import com.mapmyindia.sdk.maps.style.layers.PropertyFactory.*
import com.mapmyindia.sdk.maps.style.sources.GeoJsonOptions
import com.mapmyindia.sdk.maps.style.sources.GeoJsonSource
import com.mapmyindia.sdk.maps.utils.ColorUtils
import com.mmi.services.api.directions.DirectionsCriteria
import java.util.*

/**
 * Created by Saksham on 20/9/19.
 */
class DirectionPolylinePlugin(val mapmyIndiaMap: MapmyIndiaMap, mapView: MapView, private var directionsCriteria: String?) : MapView.OnDidFinishLoadingStyleListener {

    private var featureCollection: FeatureCollection? = null
    private var latLngs: List<LatLng>? = null

    private val widthDash = 5f
    private val gapDash = 5f

    private var polylineSource: GeoJsonSource? = null
    private var lineLayer: LineLayer? = null

    init {

        updateSource()
        mapView.addOnDidFinishLoadingStyleListener(this)
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
            lineLayer!!.setProperties(lineDasharray(arrayOf()),
                    lineColor(ColorUtils.colorToRgbaString(Color.parseColor("#3bb2d0"))))
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
            lineLayer!!.setProperties(lineDasharray(arrayOf()),
                    lineColor(ColorUtils.colorToRgbaString(Color.parseColor("#3bb2d0"))))
        }

        featureCollection = FeatureCollection.fromFeature(features)
        updateSource()
    }

    /**
     * Add various sources to the map.
     */
    private fun initSources(featureCollection: FeatureCollection) {
        mapmyIndiaMap.getStyle {
            if(it.getSource(UPPER_SOURCE_ID) == null) {
                polylineSource = GeoJsonSource(UPPER_SOURCE_ID, featureCollection,
                        GeoJsonOptions().withLineMetrics(true).withBuffer(2))
                it.addSource(polylineSource!!)
            }
        }

    }

    /**
     * Update Source and GeoJson properties
     */
    private fun updateSource() {
        mapmyIndiaMap.getStyle {
            val source = it.getSource(UPPER_SOURCE_ID) as GeoJsonSource?
            if (source == null) {
                create(it)
                return@getStyle
            }
            if (featureCollection != null) {
                source.setGeoJson(featureCollection)
            }
        }

    }

    /**
     * Add Line layer on map
     */
    private fun create(style: Style) {
        if(style.getLayer(LAYER_ID) == null) {
            lineLayer = LineLayer(LAYER_ID, UPPER_SOURCE_ID).withProperties(
                    lineCap(Property.LINE_CAP_ROUND),
                    lineJoin(Property.LINE_JOIN_ROUND),
                    lineWidth(5f))
            style.addLayer(lineLayer!!)


            if (directionsCriteria!!.equals(DirectionsCriteria.PROFILE_WALKING, ignoreCase = true)) {
                lineLayer?.setProperties(lineDasharray(arrayOf(gapDash, widthDash)))
            }
        }
    }


    override fun onDidFinishLoadingStyle() {
        updateSource()
        createPolyline(latLngs!!)
    }

    companion object {
        private const val UPPER_SOURCE_ID = "line-source-upper-id"
        private const val LAYER_ID = "line-layer-upper-id"
    }
}