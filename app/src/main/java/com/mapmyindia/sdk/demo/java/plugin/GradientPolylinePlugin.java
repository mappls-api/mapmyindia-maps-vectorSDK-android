package com.mapmyindia.sdk.demo.java.plugin;

import static com.mapmyindia.sdk.maps.style.expressions.Expression.color;
import static com.mapmyindia.sdk.maps.style.expressions.Expression.interpolate;
import static com.mapmyindia.sdk.maps.style.expressions.Expression.lineProgress;
import static com.mapmyindia.sdk.maps.style.expressions.Expression.linear;
import static com.mapmyindia.sdk.maps.style.expressions.Expression.stop;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.lineCap;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.lineGradient;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.lineJoin;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.lineWidth;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.mapmyindia.sdk.geojson.Feature;
import com.mapmyindia.sdk.geojson.FeatureCollection;
import com.mapmyindia.sdk.geojson.LineString;
import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.Style;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mapmyindia.sdk.maps.style.layers.LineLayer;
import com.mapmyindia.sdk.maps.style.layers.Property;
import com.mapmyindia.sdk.maps.style.sources.GeoJsonOptions;
import com.mapmyindia.sdk.maps.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saksham on 18-07-2019
 */
public class GradientPolylinePlugin implements MapView.OnDidFinishLoadingStyleListener {
    private static final String UPPER_SOURCE_ID = "line-source-upper-id";
    private static final String LAYER_ID = "line-layer-upper-id";
    private MapmyIndiaMap mapmyIndiaMap;
    private FeatureCollection featureCollection;
    private int startColor = Color.parseColor("#3dd2d0");
    private int endColor = Color.parseColor("#FF20d0");
    private List<LatLng> latLngs;

    private GeoJsonSource polylineSource;

    public GradientPolylinePlugin(MapmyIndiaMap mapmyIndiaMap, MapView mapView) {
        this.mapmyIndiaMap = mapmyIndiaMap;

        updateSource();
        mapView.addOnDidFinishLoadingStyleListener(this);
    }

    /**
     * Set start color of the gradient
     *
     * @param startColor starting color of the polyline
     */
    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    /**
     * Set end color of the gradient
     *
     * @param endColor end color of the polyline
     */
    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }

    /**
     * Add feature to source
     *
     * @param latLngs List of points
     */
    public void createPolyline(List<LatLng> latLngs) {
        this.latLngs = latLngs;
        List<Point> points = new ArrayList<>();
        for (LatLng latLng : latLngs) {
            points.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
        }
        featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(LineString.fromLngLats(points)));

        initSources(featureCollection);
    }

    /**
     * Add Line layer to map
     */
    private void create(Style style) {

        if (style.getLayer(LAYER_ID) == null) {
            style.addLayer(new LineLayer(LAYER_ID, UPPER_SOURCE_ID).withProperties(
//                lineColor(Color.RED),
                    lineCap(Property.LINE_CAP_ROUND),
                    lineJoin(Property.LINE_JOIN_BEVEL),
                    lineGradient(interpolate(
                            linear(), lineProgress(),
                            stop(0f, color(startColor)),
                            stop(1f, color(endColor)))),
                    lineWidth(4f)));
        }
    }

    /**
     * Remove dotted line
     */
    public void clear() {
        featureCollection = FeatureCollection.fromFeatures(new ArrayList<>());
        updateSource();

    }

    /**
     * Add various sources to the map.
     */
    private void initSources(@NonNull FeatureCollection featureCollection) {
        mapmyIndiaMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if (style.getSource(UPPER_SOURCE_ID) == null) {
                    style.addSource(polylineSource = new GeoJsonSource(UPPER_SOURCE_ID, featureCollection,
                            new GeoJsonOptions().withLineMetrics(true).withBuffer(2)));
                }
            }
        });

    }


    @Override
    public void onDidFinishLoadingStyle() {
        updateSource();
        createPolyline(latLngs);
    }

    /**
     * Update the source of the polyline
     */
    private void updateSource() {
        mapmyIndiaMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                GeoJsonSource source = (GeoJsonSource) style.getSource(UPPER_SOURCE_ID);
                if (source == null) {
                    create(style);
                    return;
                }
                if (featureCollection != null) {
                    source.setGeoJson(featureCollection);
                }
            }
        });

    }
}
