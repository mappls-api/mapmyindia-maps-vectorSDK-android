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
import com.mapmyindia.sdk.maps.style.expressions.Expression.*
import com.mapmyindia.sdk.maps.style.layers.LineLayer
import com.mapmyindia.sdk.maps.style.layers.Property
import com.mapmyindia.sdk.maps.style.layers.PropertyFactory.*
import com.mapmyindia.sdk.maps.style.sources.GeoJsonOptions
import com.mapmyindia.sdk.maps.style.sources.GeoJsonSource
import java.util.*

/**
 * Created by Saksham on 20/9/19.
 */
class GradientPolylinePlugin(private val mapmyIndiaMap: MapmyIndiaMap, mapView: MapView) : MapView.OnDidFinishLoadingStyleListener {

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
        mapView.addOnDidFinishLoadingStyleListener(this)
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
    private fun create(style: Style) {
        if (style.getLayer(LAYER_ID) == null) {
            style.addLayer(LineLayer(LAYER_ID, UPPER_SOURCE_ID).withProperties(
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
        mapmyIndiaMap.getStyle {
            if (it.getSource(UPPER_SOURCE_ID) == null) {
                polylineSource = GeoJsonSource(UPPER_SOURCE_ID, featureCollection,
                        GeoJsonOptions().withLineMetrics(true).withBuffer(2))
                it.addSource(polylineSource!!)
            }
        }
    }

    override fun onDidFinishLoadingStyle() {
        updateSource()
        createPolyline(latLngs!!)
    }

    /**
     * Update the source of the polyline
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
     * Remove dotted line
     */
    fun clear() {
        featureCollection = FeatureCollection.fromFeatures(ArrayList())
        updateSource()
    }


    companion object {
        private const val UPPER_SOURCE_ID = "line-source-upper-id"
        private const val LAYER_ID = "line-layer-upper-id"
    }
}
