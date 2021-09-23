package com.mapmyindia.sdk.demo.kotlin.plugin

import com.mapmyindia.sdk.geojson.Feature
import com.mapmyindia.sdk.geojson.FeatureCollection
import com.mapmyindia.sdk.geojson.Point
import com.mapmyindia.sdk.maps.MapView
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.Style
import com.mapmyindia.sdk.maps.style.expressions.Expression.*
import com.mapmyindia.sdk.maps.style.layers.CircleLayer
import com.mapmyindia.sdk.maps.style.layers.HeatmapLayer
import com.mapmyindia.sdk.maps.style.layers.PropertyFactory.*
import com.mapmyindia.sdk.maps.style.sources.GeoJsonSource


class HeatMapPlugin private constructor(private val mapmyIndiaMap: MapmyIndiaMap, mapView: MapView, private val builder: Builder) : MapView.OnDidFinishLoadingStyleListener {

    private var featureCollection: FeatureCollection? = null

    init {
        updateState()
        mapView.addOnDidFinishLoadingStyleListener(this)
    }

    /**
     * Update the state of the heatmap
     */
    private fun updateState() {
        mapmyIndiaMap.getStyle {
            val source: GeoJsonSource? = it.getSource(HEAT_MAP_SOURCE_ID) as GeoJsonSource?
            if (source == null) {
                initialise(it)
                return@getStyle
            }

            if (featureCollection != null)
                source.setGeoJson(featureCollection)
        }
    }

    private fun initialise(style: Style) {
        addHeatMapSource(style)
        addHeatmapLayer(style)
        addCircleLayer(style)
    }

    private fun addHeatMapSource(style: Style) {
        if (style.getSource(HEAT_MAP_SOURCE_ID) == null) {
            style.addSource(GeoJsonSource(HEAT_MAP_SOURCE_ID, featureCollection))
        }
    }

    private fun addCircleLayer(style: Style) {
        if (style.getLayer(CIRCLE_LAYER_ID) == null) {
            val circleLayer: CircleLayer = CircleLayer(CIRCLE_LAYER_ID, HEAT_MAP_SOURCE_ID)
            circleLayer.setProperties(

// Size circle radius by earthquake magnitude and zoom level
                    circleRadius(
                            interpolate(
                                    linear(), zoom(),
                                    literal(7), interpolate(
                                    linear(), get(PROPERTY_MAG),
                                    stop(1, 1),
                                    stop(6, 4)
                            ),
                                    literal(16), interpolate(
                                    linear(), get(PROPERTY_MAG),
                                    stop(1, 5),
                                    stop(6, 50)
                            )
                            )
                    ),

// Color circle by earthquake magnitude
                    circleColor(
                            interpolate(
                                    linear(), get(PROPERTY_MAG),
                                    literal(1), rgba(33, 102, 172, 0),
                                    literal(2), rgb(103, 169, 207),
                                    literal(3), rgb(209, 229, 240),
                                    literal(4), rgb(253, 219, 199),
                                    literal(5), rgb(239, 138, 98),
                                    literal(6), rgb(178, 24, 43)
                            )
                    ),

// Transition from heatmap to circle layer by zoom level
                    circleOpacity(
                            interpolate(
                                    linear(), zoom(),
                                    stop(7, 0),
                                    stop(8, 1)
                            )
                    ),
                    circleStrokeColor("white"),
                    circleStrokeWidth(1.0f)
            )
            style.addLayerBelow(circleLayer, HEAT_MAP_LAYER_ID)
        }
    }

    private fun addHeatmapLayer(style: Style) {
        if(style.getLayer(HEAT_MAP_LAYER_ID) == null) {
            val layer: HeatmapLayer = HeatmapLayer(HEAT_MAP_LAYER_ID, HEAT_MAP_SOURCE_ID)
            layer.maxZoom = builder.maxZoom!!;
            layer.sourceLayer = HEAT_MAP_LAYER_ID;
            layer.setProperties(

// Color ramp for heatmap.  Domain is 0 (low) to 1 (high).
// Begin color ramp at 0-stop with a 0-transparency color
// to create a blur-like effect.
                    heatmapColor(
                            interpolate(
                                    linear(), heatmapDensity(),
                                    literal(0), rgba(33, 102, 172, 0),
                                    literal(0.2), rgb(103, 169, 207),
                                    literal(0.4), rgb(209, 229, 240),
                                    literal(0.6), rgb(253, 219, 199),
                                    literal(0.8), rgb(239, 138, 98),
                                    literal(1), rgb(178, 24, 43)
                            )
                    ),

// Increase the heatmap weight based on frequency and property magnitude
                    heatmapWeight(
                            interpolate(
                                    linear(), get(PROPERTY_MAG),
                                    stop(0, 0),
                                    stop(6, 1)
                            )
                    ),

// Increase the heatmap color weight weight by zoom level
// heatmap-intensity is a multiplier on top of heatmap-weight
                    heatmapIntensity(
                            interpolate(
                                    linear(), zoom(),
                                    stop(0, 1),
                                    stop(9, 3)
                            )
                    ),

// Adjust the heatmap radius by zoom level
                    heatmapRadius(
                            interpolate(
                                    linear(), zoom(),
                                    stop(0, 2),
                                    stop(9, 20)
                            )
                    ),

// Transition from heatmap to circle layer by zoom level
                    heatmapOpacity(
                            interpolate(
                                    linear(), zoom(),
                                    stop(7, 1),
                                    stop(9, 0)
                            )
                    )
            )

            style.addLayer(layer)
        }
    }

    override fun onDidFinishLoadingStyle() {
        updateState()
        addHeatmap()
    }

    fun addHeatmap() {
        val features: MutableList<Feature> = ArrayList()
        builder.heatmapOptionList.forEach {
            val feature: Feature = Feature.fromGeometry(it.point)
            feature.addNumberProperty(PROPERTY_MAG, it.mag)
            features.add(feature)
        }
        featureCollection = FeatureCollection.fromFeatures(features)
        updateState()
    }


    companion object {

        private const val HEAT_MAP_SOURCE_ID: String = "heatMapSourceId"
        private const val HEAT_MAP_LAYER_ID: String = "heatMapLayerId"
        private const val CIRCLE_LAYER_ID: String = "heatMapCircleLayerId"
        private const val PROPERTY_MAG: String = "mag"

        fun builder(mapmyIndiaMap: MapmyIndiaMap, mapView: MapView): Builder {
            return Builder(mapmyIndiaMap, mapView)
                    .maxZoom(9.0f)
        }


    }
    class Builder internal constructor(private val mapmyIndiaMap: MapmyIndiaMap, private val mapView: MapView) {

        internal var maxZoom: Float? = null
        internal var heatmapOptionList: MutableList<HeatMapOption> = ArrayList()

        fun maxZoom(maxZoom: Float): Builder {
            this.maxZoom = maxZoom
            return this
        }

        fun add(heatMapOption: HeatMapOption): Builder {
            this.heatmapOptionList.add(heatMapOption)
            return this
        }

        fun addAll(heatmapOptionList: List<HeatMapOption>): Builder {
            this.heatmapOptionList.addAll(heatmapOptionList)
            return this
        }

        fun build() = HeatMapPlugin(mapmyIndiaMap, mapView, this)
    }

    data class HeatMapOption(
            val point: Point,
            val mag: Double
    )
}