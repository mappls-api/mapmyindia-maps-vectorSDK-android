package com.mapmyindia.sdk.demo.java.plugin;

import android.graphics.Color;
import androidx.annotation.NonNull;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineDasharray;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 * Created by Saksham on 18/9/19.
 */
public class DashedPolylinePlugin  implements MapView.OnMapChangedListener{
    private static final String UPPER_SOURCE_ID = "line-source-upper-id";

  private MapboxMap mapmyIndiaMap;

    private FeatureCollection featureCollection;
    private Style mStyle;
    private static final String LAYER_ID = "line-layer-upper-id";
    private List<LatLng> latLngs;

    private float widthDash = 4f;
    private float gapDash = 6f;

    private GeoJsonSource polylineSource;

  public DashedPolylinePlugin(MapboxMap mapmyIndiaMap, MapView mapView) {
    this.mapmyIndiaMap = mapmyIndiaMap;

        updateSource();
        mapView.addOnMapChangedListener(this);
    }


    /**
     * Add list of positions on Feature
     *
     * @param latLngs list of points
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
     * Add various sources to the map.
     */
    private void initSources(@NonNull FeatureCollection featureCollection) {
      mapmyIndiaMap.addSource(polylineSource = new GeoJsonSource(UPPER_SOURCE_ID, featureCollection,
                new GeoJsonOptions().withLineMetrics(true).withBuffer(2)));
    }

    /**
     * Update Source of the Polyline
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

    /**
     * Add Layer on map
     */
    private void create() {
      mapmyIndiaMap.addLayer(new LineLayer(LAYER_ID, UPPER_SOURCE_ID).withProperties(
                lineColor(Color.RED),
                lineDasharray(new Float[] {widthDash, gapDash}),
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_BEVEL),
                lineWidth(4f)));
    }


    @Override
    public void onMapChanged(int i) {
        if(i == MapView.DID_FINISH_LOADING_STYLE) {
            updateSource();
            createPolyline(latLngs);
        }
    }
}
