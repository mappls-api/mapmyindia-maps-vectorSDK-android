package com.mapmyindia.sdk.demo.kotlin.plugin

import android.graphics.Color
import android.os.Handler
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapView.OnMapChangedListener
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mmi.services.api.directions.models.LegStep
import java.util.*

class SnakePolyLinePlugin(val mapView: MapView, val mapmyIndiaMap: MapboxMap?): OnMapChangedListener {


    companion object{
        private const val NAVIGATION_LINE_WIDTH = 6f
        private const val NAVIGATION_LINE_OPACITY = .8f
        private const val DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID = "DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID"
        private const val DRIVING_ROUTE_POLYLINE_SOURCE_ID = "DRIVING_ROUTE_POLYLINE_SOURCE_ID"
        private const val DRAW_SPEED_MILLISECONDS = 500
    }

    private val handler = Handler()
    private var  legSteps: List<LegStep>? =null
    private lateinit var runnable: Runnable

   init {

        mapView.addOnMapChangedListener(this)
        initialiseSourceAndLayer()
    }

    private fun initialiseSourceAndLayer() {
        addSource()
        addLayer()
    }
    private fun addLayer() {
        if (mapmyIndiaMap?.getLayer(DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID) == null) {
            mapmyIndiaMap?.addLayer(LineLayer(DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID,
                   DRIVING_ROUTE_POLYLINE_SOURCE_ID)
                    .withProperties(
                            PropertyFactory.lineWidth(NAVIGATION_LINE_WIDTH),
                            PropertyFactory.lineOpacity(NAVIGATION_LINE_OPACITY),
                            PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                            PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                            PropertyFactory.lineColor(Color.BLUE)
                    ))
        }
    }

    private fun addSource() {
        if (mapmyIndiaMap?.getSource(DRIVING_ROUTE_POLYLINE_SOURCE_ID) == null) {
            mapmyIndiaMap?.addSource(GeoJsonSource(DRIVING_ROUTE_POLYLINE_SOURCE_ID))
        }
    }

    fun create(legSteps: List<LegStep>?) {
        this.legSteps = legSteps
        // Start the step-by-step process of drawing the route
        runnable = DrawRouteRunnable(mapmyIndiaMap, legSteps, handler)
        handler.postDelayed(runnable, DRAW_SPEED_MILLISECONDS.toLong())
    }

    fun removeCallback() {
        if (runnable != null) {
            handler.removeCallbacks(runnable)
        }
    }

    override fun onMapChanged(i: Int) {
        if (i == MapView.DID_FINISH_LOADING_MAP) {
            handler.removeCallbacks(runnable)
            runnable = DrawRouteRunnable(mapmyIndiaMap, legSteps, handler)
            handler.postDelayed(runnable, DRAW_SPEED_MILLISECONDS.toLong())
        }
    }

    private class DrawRouteRunnable internal constructor(private val mapmyIndiaMap: MapboxMap?, private val steps: List<LegStep>?, private val handler: Handler) : Runnable {
        private val drivingRoutePolyLineFeatureList: MutableList<Feature>
        private var counterIndex = 0
        override fun run() {
            if (counterIndex < steps?.size?:0) {
                val singleStep = steps!![counterIndex]
                if (singleStep.geometry() != null) {
                    val lineStringRepresentingSingleStep = LineString.fromPolyline(
                            singleStep.geometry()!!, Constants.PRECISION_6)
                    val featureLineString = Feature.fromGeometry(lineStringRepresentingSingleStep)
                    drivingRoutePolyLineFeatureList.add(featureLineString)
                }
                if (mapmyIndiaMap != null) {
                    val source = mapmyIndiaMap.getSourceAs<GeoJsonSource>(DRIVING_ROUTE_POLYLINE_SOURCE_ID)
                    source?.setGeoJson(FeatureCollection.fromFeatures(drivingRoutePolyLineFeatureList))
                }
                counterIndex++
                handler.postDelayed(this, DRAW_SPEED_MILLISECONDS.toLong())
            }
        }

        init {
            drivingRoutePolyLineFeatureList = ArrayList()
        }
    }

}