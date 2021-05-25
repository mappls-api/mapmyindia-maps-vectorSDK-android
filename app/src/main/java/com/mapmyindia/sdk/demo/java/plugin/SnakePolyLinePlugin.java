package com.mapmyindia.sdk.demo.java.plugin;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mmi.services.api.directions.models.LegStep;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.Property.LINE_CAP_ROUND;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class SnakePolyLinePlugin implements MapView.OnMapChangedListener {

    private MapView mapView;
    private MapboxMap mapmyIndiaMap;
    private static final float NAVIGATION_LINE_WIDTH = 6;
    private static final float NAVIGATION_LINE_OPACITY = .8f;
    private static final String DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID = "DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID";
    private static final String DRIVING_ROUTE_POLYLINE_SOURCE_ID = "DRIVING_ROUTE_POLYLINE_SOURCE_ID";
    private static final int DRAW_SPEED_MILLISECONDS = 500;
    private Handler handler = new Handler(Looper.getMainLooper());
    private List<LegStep> legSteps;
    private Runnable runnable;

    public SnakePolyLinePlugin(MapView mapView, MapboxMap mapmyIndiaMap) {
        this.mapView = mapView;
        this.mapmyIndiaMap = mapmyIndiaMap;
        mapView.addOnMapChangedListener(this);
        initialiseSourceAndLayer();
    }

    private void initialiseSourceAndLayer() {
        addSource();
        addLayer();
    }

    private void addLayer() {
        if(mapmyIndiaMap.getLayer(DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID) == null) {
            mapmyIndiaMap.addLayer(new LineLayer(DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID,
                    DRIVING_ROUTE_POLYLINE_SOURCE_ID)
                    .withProperties(
                            lineWidth(NAVIGATION_LINE_WIDTH),
                            lineOpacity(NAVIGATION_LINE_OPACITY),
                            lineCap(LINE_CAP_ROUND),
                            lineJoin(LINE_JOIN_ROUND),
                            lineColor(Color.BLUE)
                    ));
        }
    }


    private void addSource() {
        if(mapmyIndiaMap.getSource(DRIVING_ROUTE_POLYLINE_SOURCE_ID) == null) {
            mapmyIndiaMap.addSource(new GeoJsonSource(DRIVING_ROUTE_POLYLINE_SOURCE_ID));
        }
    }


    public void create(List<LegStep> legSteps) {
        this.legSteps = legSteps;
        // Start the step-by-step process of drawing the route
        runnable = new DrawRouteRunnable(mapmyIndiaMap,legSteps, handler);
        handler.postDelayed(runnable, DRAW_SPEED_MILLISECONDS);
    }

    public void removeCallback() {
        if(runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }


    @Override
    public void onMapChanged(int i) {
        if(i == MapView.DID_FINISH_LOADING_MAP) {
            handler.removeCallbacks(runnable);
            runnable = new DrawRouteRunnable(mapmyIndiaMap,legSteps, handler);
            handler.postDelayed(runnable, DRAW_SPEED_MILLISECONDS);
        }
    }

    private static class DrawRouteRunnable implements Runnable {
        private MapboxMap mapmyIndiaMap;
        private List<LegStep> steps;
        private List<Feature> drivingRoutePolyLineFeatureList;
        private Handler handler;
        private int counterIndex;

        DrawRouteRunnable(MapboxMap mapmyIndiaMap, List<LegStep> steps, Handler handler) {
            this.mapmyIndiaMap = mapmyIndiaMap;
            this.steps = steps;
            this.handler = handler;
            this.counterIndex = 0;
            drivingRoutePolyLineFeatureList = new ArrayList<>();
        }

        @Override
        public void run() {
            if(steps==null)
                return;
            if (  counterIndex < steps.size()) {
                LegStep singleStep = steps.get(counterIndex);
                if (singleStep != null && singleStep.geometry() != null) {
                    LineString lineStringRepresentingSingleStep = LineString.fromPolyline(
                            singleStep.geometry(), Constants.PRECISION_6);
                    Feature featureLineString = Feature.fromGeometry(lineStringRepresentingSingleStep);
                    drivingRoutePolyLineFeatureList.add(featureLineString);
                }
                if (mapmyIndiaMap != null) {
                    GeoJsonSource source = mapmyIndiaMap.getSourceAs(DRIVING_ROUTE_POLYLINE_SOURCE_ID);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(drivingRoutePolyLineFeatureList));
                    }
                }
                counterIndex++;
                handler.postDelayed(this, DRAW_SPEED_MILLISECONDS);
            }
        }
    }
}
