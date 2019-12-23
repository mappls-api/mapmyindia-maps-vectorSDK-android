package com.mapmyindia.sdk.demo.java.plugin;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mmi.services.api.directions.DirectionsCriteria;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineDasharray;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 * Created by Saksham on 19/9/19.
 */
public class DirectionPolylinePlugin implements MapView.OnMapChangedListener {
    private static final String UPPER_SOURCE_ID = "line-source-upper-id";

    private MapboxMap mapboxMap;

    private FeatureCollection featureCollection;
    private static final String LAYER_ID = "line-layer-upper-id";
    private List<LatLng> latLngs;

    private float widthDash = 5f;
    private float gapDash = 5f;

    private String directionsCriteria;

    private GeoJsonSource polylineSource;
    private LineLayer lineLayer;

    public DirectionPolylinePlugin(MapboxMap mapboxMap, MapView mapView, String directionsCriteria) {
        this.mapboxMap = mapboxMap;
        this.directionsCriteria = directionsCriteria;

        updateSource();
        mapView.addOnMapChangedListener(this);
    }

    /**
     * Add polyline features and set polyline property for walk and other
     *
     * @param latLngs list of points
     */
    public void createPolyline(List<LatLng> latLngs) {
        this.latLngs = latLngs;
        List<Point> points = new ArrayList<>();
        for (LatLng latLng : latLngs) {
            points.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
        }
        Feature features = Feature.fromGeometry(LineString.fromLngLats(points));

        if(directionsCriteria.equalsIgnoreCase(DirectionsCriteria.PROFILE_WALKING)) {
            lineLayer.setProperties(lineDasharray(new Float[]{widthDash, gapDash}),
                    lineColor(Color.BLACK));
        } else {
            lineLayer.setProperties(lineDasharray(new Float[]{0f,0f}),
                    lineColor(Color.parseColor("#3bb2d0")));
        }
        featureCollection = FeatureCollection.fromFeature(features);
        initSources(featureCollection);
    }

    /**
     * Update polyline features and set polyline property for walk and other
     *
     * @param directionsCriteria {"foot", "biking", "driving"}
     * @param latLngs list of points
     */
    public void updatePolyline(String directionsCriteria, List<LatLng> latLngs) {
        this.directionsCriteria = directionsCriteria;
        List<Point> points = new ArrayList<>();
        for (LatLng latLng : latLngs) {
            points.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
        }
        Feature features = Feature.fromGeometry(LineString.fromLngLats(points));
        if(directionsCriteria.equalsIgnoreCase(DirectionsCriteria.PROFILE_WALKING)) {
            lineLayer.setProperties(lineDasharray(new Float[]{widthDash, gapDash}),
                    lineColor(Color.BLACK));
        } else {
            lineLayer.setProperties(lineDasharray(new Float[]{0f,0f}),
                    lineColor(Color.parseColor("#3bb2d0")));
        }

        featureCollection = FeatureCollection.fromFeature(features);
        updateSource();
    }

    /**
     * Add various sources to the map.
     */
    private void initSources(@NonNull FeatureCollection featureCollection) {
        mapboxMap.addSource(polylineSource = new GeoJsonSource(UPPER_SOURCE_ID, featureCollection,
                new GeoJsonOptions().withLineMetrics(true).withBuffer(2)));
    }

    /**
     * Update Source and GeoJson properties
     */
    private void updateSource() {
        GeoJsonSource source = (GeoJsonSource) mapboxMap.getSource(UPPER_SOURCE_ID);
        if(source == null) {
            create();
            return;
        }
        if(featureCollection != null) {
            polylineSource.setGeoJson(featureCollection);
        }
    }

    /**
     * Add Line layer on map
     */
    private void create() {
        mapboxMap.addLayer(lineLayer = new LineLayer(LAYER_ID, UPPER_SOURCE_ID).withProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f)));


        if(directionsCriteria.equalsIgnoreCase(DirectionsCriteria.PROFILE_WALKING)) {
            lineLayer.setProperties(lineDasharray(new Float[]{gapDash, widthDash}));
        }
    }


    @Override
    public void onMapChanged(int i) {
        if(i == MapView.DID_FINISH_LOADING_STYLE) {
            updateSource();
            createPolyline(latLngs);
        }
    }
}