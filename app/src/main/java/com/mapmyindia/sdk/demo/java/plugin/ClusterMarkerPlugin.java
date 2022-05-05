package com.mapmyindia.sdk.demo.java.plugin;

import static com.mapmyindia.sdk.maps.style.expressions.Expression.all;
import static com.mapmyindia.sdk.maps.style.expressions.Expression.get;
import static com.mapmyindia.sdk.maps.style.expressions.Expression.gte;
import static com.mapmyindia.sdk.maps.style.expressions.Expression.has;
import static com.mapmyindia.sdk.maps.style.expressions.Expression.literal;
import static com.mapmyindia.sdk.maps.style.expressions.Expression.lt;
import static com.mapmyindia.sdk.maps.style.expressions.Expression.toNumber;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.circleColor;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.circleRadius;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.textColor;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.textField;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.textSize;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.geojson.Feature;
import com.mapmyindia.sdk.geojson.FeatureCollection;
import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.Style;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mapmyindia.sdk.maps.geometry.LatLngBounds;
import com.mapmyindia.sdk.maps.style.expressions.Expression;
import com.mapmyindia.sdk.maps.style.layers.CircleLayer;
import com.mapmyindia.sdk.maps.style.layers.PropertyFactory;
import com.mapmyindia.sdk.maps.style.layers.SymbolLayer;
import com.mapmyindia.sdk.maps.style.sources.GeoJsonOptions;
import com.mapmyindia.sdk.maps.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

public class ClusterMarkerPlugin implements MapView.OnDidFinishLoadingStyleListener, MapmyIndiaMap.OnMapClickListener {

    private final MapmyIndiaMap mapmyIndiaMap;
    private final MapView mapView;
    private final String CLUSTER_MARKER_SOURCE = "cluster-marker-source";
    private final String UN_CLUSTER_MARKER_LAYER = "un-cluster-marker-layer";
    private final String CLUSTER_CIRCLE_LAYER = "cluster-circle-layer";
    private final String CLUSTER_COUNT_LAYER = "cluster-count-layer";
    private final String MARKER_ICON_PROPERTY = "marker-icon-property";
    private final String MARKER_CLUSTER_PROPERTY = "marker-color-property";

    private final String POINT_COUNT_TEXT = "point_count";
    private FeatureCollection featureCollection = FeatureCollection.fromFeatures(new ArrayList<>());

    public ClusterMarkerPlugin(MapView mapView, MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        this.mapView = mapView;
        mapView.addOnDidFinishLoadingStyleListener(this);
        mapmyIndiaMap.addOnMapClickListener(this);
        updateState();
    }

    private void updateState() {
        mapmyIndiaMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                GeoJsonSource source = (GeoJsonSource) style.getSource(CLUSTER_MARKER_SOURCE);
                if (source == null) {
                    initialise(style);
                    return;
                }
                source.setGeoJson(featureCollection);
            }
        });
    }

    public void setMarkers(List<LatLng> latLngList) {
        List<Feature> features = new ArrayList<>();
        if (latLngList != null) {
            for (LatLng latLng : latLngList) {
                Feature feature = Feature.fromGeometry(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
                feature.addStringProperty(MARKER_ICON_PROPERTY, "marker-icon");
                feature.addBooleanProperty(MARKER_CLUSTER_PROPERTY, true);
                features.add(feature);
            }
        }
        featureCollection = FeatureCollection.fromFeatures(features);
        updateState();
    }

    private void initialise(Style style) {
        initImages(style);
        initialiseSource(style);
        initialiseUnClusterLayer(style);
        initialiseClusterLayer(style);
        initialiseCountLayer(style);
    }

    private void initialiseCountLayer(Style style) {
        style.removeLayer(CLUSTER_COUNT_LAYER);

//Add the count labels
        SymbolLayer count = new SymbolLayer(CLUSTER_COUNT_LAYER, CLUSTER_MARKER_SOURCE);
        count.setProperties(
                textField(Expression.toString(get(POINT_COUNT_TEXT))),
                textSize(12f),
                textColor(Color.WHITE),
                textIgnorePlacement(true),
                textAllowOverlap(true)
        );
        style.addLayer(count);
    }

    private void initialiseClusterLayer(Style style) {
        int[][] layers = new int[][]{
                new int[]{10, Color.BLUE}, //If markers count is greater than 10 in cluster
                new int[]{5, Color.BLUE}, //If markers count is in between 6 - 10 in cluster
                new int[]{0, Color.BLUE} //If markers count is in between 0 -5 in cluster
        };
        for (int i = 0; i < layers.length; i++) {
            style.removeLayer(CLUSTER_CIRCLE_LAYER + i);
//Add clusters' circles
            CircleLayer circles = new CircleLayer(CLUSTER_CIRCLE_LAYER + i, CLUSTER_MARKER_SOURCE);
            circles.setProperties(
                    circleColor(layers[i][1]),
                    circleRadius(18f)
            );

            Expression pointCount = toNumber(get(POINT_COUNT_TEXT));

// Add a filter to the cluster layer that hides the circles based on "point_count"
            circles.setFilter(
                    i == 0
                            ? all(has(POINT_COUNT_TEXT),
                            gte(pointCount, literal(layers[i][0]))
                    ) : all(has(POINT_COUNT_TEXT),
                            gte(pointCount, literal(layers[i][0])),
                            lt(pointCount, literal(layers[i - 1][0]))
                    )
            );
            style.addLayer(circles);
        }

    }

    private void initialiseUnClusterLayer(Style style) {
        style.removeLayer(UN_CLUSTER_MARKER_LAYER);
        style.addLayer(new SymbolLayer(UN_CLUSTER_MARKER_LAYER, CLUSTER_MARKER_SOURCE).withProperties(
                PropertyFactory.iconImage(get(MARKER_ICON_PROPERTY)),
                PropertyFactory.iconSize(0.5f)
        ).withFilter(has(MARKER_CLUSTER_PROPERTY)));
    }

    private void initImages(Style style) {
        if (style.getImage("marker-icon") == null) {
            style.addImage("marker-icon", ContextCompat.getDrawable(mapView.getContext(), R.drawable.placeholder));
        }
    }

    private void initialiseSource(Style style) {
        style.removeSource(CLUSTER_MARKER_SOURCE);
        style.addSource(new GeoJsonSource(CLUSTER_MARKER_SOURCE, featureCollection,
                new GeoJsonOptions().withCluster(true).withClusterRadius(50).withClusterMaxZoom(14)));
    }


    @Override
    public void onDidFinishLoadingStyle() {
        updateState();
    }

    @Override
    public boolean onMapClick(@NonNull LatLng latLng) {

        List<Feature> features = mapmyIndiaMap.queryRenderedFeatures(this.mapmyIndiaMap.getProjection().toScreenLocation(latLng), CLUSTER_CIRCLE_LAYER + 0, CLUSTER_CIRCLE_LAYER + 1, CLUSTER_CIRCLE_LAYER + 2);
        if(features.size() > 0) {
            Style style = mapmyIndiaMap.getStyle();
            if(style != null && style.isFullyLoaded()) {
                GeoJsonSource source = (GeoJsonSource) style.getSource(CLUSTER_MARKER_SOURCE);
                if(source != null) {
                    FeatureCollection clusterLeaves = source.getClusterLeaves(features.get(0), 8000, 0);
                    moveCameraToLeavesBounds(clusterLeaves);
                }
            }
        }
        return false;
    }


    private void moveCameraToLeavesBounds(FeatureCollection featureCollectionToInspect) {
        List<LatLng> latLngList = new ArrayList<>();
        if (featureCollectionToInspect.features() != null) {
            for (Feature singleClusterFeature : featureCollectionToInspect.features()) {
                Point clusterPoint = (Point) singleClusterFeature.geometry();
                if (clusterPoint != null) {
                    latLngList.add(new LatLng(clusterPoint.latitude(), clusterPoint.longitude()));
                }
            }
            if (latLngList.size() > 1) {
                LatLngBounds latLngBounds = new LatLngBounds.Builder()
                        .includes(latLngList)
                        .build();
                mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20, 20, 20, 20));
            }
        }
    }
}
