package com.mapmyindia.sdk.demo.kotlin.plugin

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.geojson.Feature
import com.mapmyindia.sdk.geojson.FeatureCollection
import com.mapmyindia.sdk.geojson.Point
import com.mapmyindia.sdk.maps.MapView
import com.mapmyindia.sdk.maps.MapView.OnDidFinishLoadingStyleListener
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.MapmyIndiaMap.OnMapClickListener
import com.mapmyindia.sdk.maps.Style
import com.mapmyindia.sdk.maps.Style.OnStyleLoaded
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.geometry.LatLngBounds
import com.mapmyindia.sdk.maps.style.expressions.Expression
import com.mapmyindia.sdk.maps.style.layers.CircleLayer
import com.mapmyindia.sdk.maps.style.layers.PropertyFactory
import com.mapmyindia.sdk.maps.style.layers.SymbolLayer
import com.mapmyindia.sdk.maps.style.sources.GeoJsonOptions
import com.mapmyindia.sdk.maps.style.sources.GeoJsonSource

class ClusterMarkerPlugin(private val mapView: MapView, private val mapmyIndiaMap: MapmyIndiaMap) :
    OnDidFinishLoadingStyleListener, OnMapClickListener {
    companion object {
        private const val CLUSTER_MARKER_SOURCE = "cluster-marker-source"
        private const val UN_CLUSTER_MARKER_LAYER = "un-cluster-marker-layer"
        private const val CLUSTER_CIRCLE_LAYER = "cluster-circle-layer"
        private const val CLUSTER_COUNT_LAYER = "cluster-count-layer"
        private const val MARKER_ICON_PROPERTY = "marker-icon-property"
        private const val MARKER_CLUSTER_PROPERTY = "marker-color-property"
        private const val POINT_COUNT_TEXT = "point_count"
    }

    private var featureCollection = FeatureCollection.fromFeatures(ArrayList())
    private fun updateState() {
        mapmyIndiaMap.getStyle(OnStyleLoaded { style ->
            val source = style.getSource(CLUSTER_MARKER_SOURCE) as GeoJsonSource?
            if (source == null) {
                initialise(style)
                return@OnStyleLoaded
            }
            source.setGeoJson(featureCollection)
        })
    }

    fun setMarkers(latLngList: List<LatLng>?) {
        val features: MutableList<Feature> = ArrayList()
        if (latLngList != null) {
            for (latLng in latLngList) {
                val feature =
                    Feature.fromGeometry(Point.fromLngLat(latLng.longitude, latLng.latitude))
                feature.addStringProperty(MARKER_ICON_PROPERTY, "marker-icon")
                feature.addBooleanProperty(MARKER_CLUSTER_PROPERTY, true)
                features.add(feature)
            }
        }
        featureCollection = FeatureCollection.fromFeatures(features)
        updateState()
    }

    private fun initialise(style: Style) {
        initImages(style)
        initialiseSource(style)
        initialiseUnClusterLayer(style)
        initialiseClusterLayer(style)
        initialiseCountLayer(style)
    }

    private fun initialiseCountLayer(style: Style) {
        style.removeLayer(CLUSTER_COUNT_LAYER)

//Add the count labels
        val count = SymbolLayer(CLUSTER_COUNT_LAYER, CLUSTER_MARKER_SOURCE)
        count.setProperties(
            PropertyFactory.textField(Expression.toString(Expression.get(POINT_COUNT_TEXT))),
            PropertyFactory.textSize(12f),
            PropertyFactory.textColor(Color.WHITE),
            PropertyFactory.textIgnorePlacement(true),
            PropertyFactory.textAllowOverlap(true)
        )
        style.addLayer(count)
    }

    private fun initialiseClusterLayer(style: Style) {
        val layers = arrayOf(
            intArrayOf(10, Color.BLUE),
            intArrayOf(5, Color.BLUE),
            intArrayOf(0, Color.BLUE)
        )
        for (i in layers.indices) {
            style.removeLayer(CLUSTER_CIRCLE_LAYER + i)
            //Add clusters' circles
            val circles = CircleLayer(CLUSTER_CIRCLE_LAYER + i, CLUSTER_MARKER_SOURCE)
            circles.setProperties(
                PropertyFactory.circleColor(layers[i][1]),
                PropertyFactory.circleRadius(18f)
            )
            val pointCount = Expression.toNumber(Expression.get(POINT_COUNT_TEXT))

// Add a filter to the cluster layer that hides the circles based on "point_count"
            circles.setFilter(
                if (i == 0) Expression.all(
                    Expression.has(POINT_COUNT_TEXT),
                    Expression.gte(
                        pointCount, Expression.literal(
                            layers[i][0]
                        )
                    )
                ) else Expression.all(
                    Expression.has(POINT_COUNT_TEXT),
                    Expression.gte(
                        pointCount, Expression.literal(
                            layers[i][0]
                        )
                    ),
                    Expression.lt(
                        pointCount, Expression.literal(
                            layers[i - 1][0]
                        )
                    )
                )
            )
            style.addLayer(circles)
        }
    }

    private fun initialiseUnClusterLayer(style: Style) {
        style.removeLayer(UN_CLUSTER_MARKER_LAYER)
        style.addLayer(
            SymbolLayer(UN_CLUSTER_MARKER_LAYER, CLUSTER_MARKER_SOURCE).withProperties(
                PropertyFactory.iconImage(Expression.get(MARKER_ICON_PROPERTY)),
                PropertyFactory.iconSize(0.5f)
            ).withFilter(Expression.has(MARKER_CLUSTER_PROPERTY))
        )
    }

    private fun initImages(style: Style) {
        if (style.getImage("marker-icon") == null) {
            style.addImage(
                "marker-icon",
                ContextCompat.getDrawable(mapView.context, R.drawable.placeholder)!!
            )
        }
    }

    private fun initialiseSource(style: Style) {
        style.removeSource(CLUSTER_MARKER_SOURCE)
        style.addSource(
            GeoJsonSource(
                CLUSTER_MARKER_SOURCE, featureCollection,
                GeoJsonOptions().withCluster(true).withClusterRadius(50).withClusterMaxZoom(14)
            )
        )
    }

    override fun onDidFinishLoadingStyle() {
        updateState()
    }

    override fun onMapClick(latLng: LatLng): Boolean {
        val features = mapmyIndiaMap.queryRenderedFeatures(
            mapmyIndiaMap.projection.toScreenLocation(latLng),
            CLUSTER_CIRCLE_LAYER + 0,
            CLUSTER_CIRCLE_LAYER + 1,
            CLUSTER_CIRCLE_LAYER + 2
        )
        if (features.size > 0) {
            val style = mapmyIndiaMap.style
            if (style != null && style.isFullyLoaded) {
                val source = style.getSource(CLUSTER_MARKER_SOURCE) as GeoJsonSource?
                if (source != null) {
                    val clusterLeaves = source.getClusterLeaves(features[0], 8000, 0)
                    moveCameraToLeavesBounds(clusterLeaves)
                }
            }
        }
        return false
    }

    private fun moveCameraToLeavesBounds(featureCollectionToInspect: FeatureCollection) {
        val latLngList: MutableList<LatLng> = ArrayList()
        if (featureCollectionToInspect.features() != null) {
            for (singleClusterFeature in featureCollectionToInspect.features()!!) {
                val clusterPoint = singleClusterFeature.geometry() as Point?
                if (clusterPoint != null) {
                    latLngList.add(LatLng(clusterPoint.latitude(), clusterPoint.longitude()))
                }
            }
            if (latLngList.size > 1) {
                val latLngBounds = LatLngBounds.Builder()
                    .includes(latLngList)
                    .build()
                mapmyIndiaMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        latLngBounds,
                        20,
                        20,
                        20,
                        20
                    )
                )
            }
        }
    }

    init {
        mapView.addOnDidFinishLoadingStyleListener(this)
        mapmyIndiaMap.addOnMapClickListener(this)
        updateState()
    }
}