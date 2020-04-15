package com.mapmyindia.sdk.demo.java.plugin;

import android.graphics.Color;
import androidx.annotation.NonNull;

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

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.color;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lineProgress;
import static com.mapbox.mapboxsdk.style.expressions.Expression.linear;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineGradient;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 * Created by Saksham on 18-07-2019
 */
public class GradientPolylinePlugin implements MapView.OnMapChangedListener {
    private static final String UPPER_SOURCE_ID = "line-source-upper-id";

  private MapboxMap mapmyIndiaMap;

    private FeatureCollection featureCollection;
    private int startColor = Color.parseColor("#3dd2d0");
    private int endColor = Color.parseColor("#FF20d0");
    private static final String LAYER_ID = "line-layer-upper-id";
    private List<LatLng> latLngs;

    private GeoJsonSource polylineSource;

  public GradientPolylinePlugin(MapboxMap mapmyIndiaMap, MapView mapView) {
    this.mapmyIndiaMap = mapmyIndiaMap;

        updateSource();
        mapView.addOnMapChangedListener(this);
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
    private void create() {
        if(mapmyIndiaMap.getLayer(LAYER_ID) == null) {
            mapmyIndiaMap.addLayer(new LineLayer(LAYER_ID, UPPER_SOURCE_ID).withProperties(
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
     * Add various sources to the map.
     */
    private void initSources(@NonNull FeatureCollection featureCollection) {
        if(mapmyIndiaMap.getSource(UPPER_SOURCE_ID) == null) {
            mapmyIndiaMap.addSource(polylineSource = new GeoJsonSource(UPPER_SOURCE_ID, featureCollection,
                    new GeoJsonOptions().withLineMetrics(true).withBuffer(2)));
        }
    }

    @Override
    public void onMapChanged(int i) {
        if(i == MapView.DID_FINISH_LOADING_STYLE) {
            updateSource();
            createPolyline(latLngs);
        }
    }

    /**
     * Update the source of the polyline
     */
    private void updateSource() {
      GeoJsonSource source = (GeoJsonSource) mapmyIndiaMap.getSource(UPPER_SOURCE_ID);
        if(source == null) {
            create();
            return;
        }
        if(featureCollection != null) {
            polylineSource.setGeoJson(featureCollection);
        }
    }
}
