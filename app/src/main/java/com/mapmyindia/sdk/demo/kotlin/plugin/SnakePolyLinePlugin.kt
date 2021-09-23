package com.mapmyindia.sdk.demo.kotlin.plugin

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import com.mapmyindia.sdk.geojson.Feature
import com.mapmyindia.sdk.geojson.FeatureCollection
import com.mapmyindia.sdk.geojson.LineString
import com.mapmyindia.sdk.maps.MapView
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.Style
import com.mapmyindia.sdk.maps.style.layers.LineLayer
import com.mapmyindia.sdk.maps.style.layers.Property
import com.mapmyindia.sdk.maps.style.layers.PropertyFactory
import com.mapmyindia.sdk.maps.style.sources.GeoJsonSource
import com.mmi.services.api.directions.models.LegStep
import com.mmi.services.utils.Constants
import java.util.*

class SnakePolyLinePlugin : MapView.OnDidFinishLoadingStyleListener {

    private lateinit var mapView: MapView
    private lateinit var mapmyIndiaMap: MapmyIndiaMap

    companion object {
        private const val NAVIGATION_LINE_WIDTH = 6f
        private const val NAVIGATION_LINE_OPACITY = .8f
        private const val DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID = "DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID"
        private const val DRIVING_ROUTE_POLYLINE_SOURCE_ID = "DRIVING_ROUTE_POLYLINE_SOURCE_ID"
        private const val DRAW_SPEED_MILLISECONDS = 500
    }

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var legSteps: List<LegStep>
    private lateinit var runnable: Runnable

    fun SnakePolyLinePlugin(mapView: MapView, mapmyIndiaMap: MapmyIndiaMap?) {
        this.mapView = mapView
        this.mapmyIndiaMap = mapmyIndiaMap!!
        mapView.addOnDidFinishLoadingStyleListener(this)
        initialiseSourceAndLayer()
    }

    private fun initialiseSourceAndLayer() {
        mapmyIndiaMap.getStyle {
            addSource(it)
            addLayer(it)
        }
    }

    private fun addLayer(style: Style) {
        if (style.getLayer(DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID) == null) {
            style.addLayer(LineLayer(DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID,
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

    private fun addSource(style: Style) {
        if (style.getSource(DRIVING_ROUTE_POLYLINE_SOURCE_ID) == null) {
            style.addSource(GeoJsonSource(DRIVING_ROUTE_POLYLINE_SOURCE_ID))
        }
    }

    fun create(legSteps: List<LegStep>) {
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


    override fun onDidFinishLoadingStyle() {
        handler.removeCallbacks(runnable)
        runnable = DrawRouteRunnable(mapmyIndiaMap, legSteps, handler)
        handler.postDelayed(runnable, DRAW_SPEED_MILLISECONDS.toLong())
    }

    private class DrawRouteRunnable constructor(private val mapmyIndiaMap: MapmyIndiaMap?, private val steps: List<LegStep>, private val handler: Handler) : Runnable {
        private val drivingRoutePolyLineFeatureList: MutableList<Feature>
        private var counterIndex = 0
        override fun run() {
            if (counterIndex < steps.size) {
                val singleStep = steps[counterIndex]
                if (singleStep?.geometry() != null) {
                    val lineStringRepresentingSingleStep = LineString.fromPolyline(
                            singleStep.geometry()!!, Constants.PRECISION_6)
                    val featureLineString = Feature.fromGeometry(lineStringRepresentingSingleStep)
                    drivingRoutePolyLineFeatureList.add(featureLineString)
                }
                mapmyIndiaMap?.getStyle {
                    val source = it.getSourceAs<GeoJsonSource>(DRIVING_ROUTE_POLYLINE_SOURCE_ID)
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