package com.mapmyindia.sdk.demo.java.plugin;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.HeatmapLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.heatmapDensity;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.linear;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgb;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgba;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleStrokeColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleStrokeWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.heatmapColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.heatmapIntensity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.heatmapOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.heatmapRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.heatmapWeight;

public class HeatMapPlugin implements MapView.OnMapChangedListener {


    private MapboxMap mapmyIndiaMap;
    private FeatureCollection featureCollection;
    private static final String HEAT_MAP_SOURCE_ID = "heatMapSourceId";
    private static final String HEAT_MAP_LAYER_ID = "heatMapLayerId";
    private static final String CIRCLE_LAYER_ID = "heatMapCircleLayerId";
    private static final String PROPERTY_MAG = "mag";
    private Builder builder;

    private HeatMapPlugin(MapboxMap mapmyIndiaMap, MapView mapView, Builder builder) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        this.builder = builder;
        mapView.addOnMapChangedListener(this);
        updateState();

    }

    /**
     * Update the state of the marker
     */
    private void updateState() {
        GeoJsonSource source = (GeoJsonSource) mapmyIndiaMap.getSource(HEAT_MAP_SOURCE_ID);
        if (source == null) {
            initialise();
            return;
        }
        if (featureCollection != null) {
            source.setGeoJson(featureCollection);
        }
    }

    private void initialise() {
        addHeatMapSource();
        addHeatmapLayer();
        addCircleLayer();
    }

    private void addHeatMapSource() {
        if(mapmyIndiaMap.getSource(HEAT_MAP_SOURCE_ID) == null) {
            mapmyIndiaMap.addSource(new GeoJsonSource(HEAT_MAP_SOURCE_ID, featureCollection));
        }
    }

    private void addCircleLayer() {
        if (mapmyIndiaMap.getLayer(CIRCLE_LAYER_ID) == null) {
            CircleLayer circleLayer = new CircleLayer(CIRCLE_LAYER_ID, HEAT_MAP_SOURCE_ID);
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
            );

            mapmyIndiaMap.addLayerBelow(circleLayer, HEAT_MAP_LAYER_ID);
        }
    }

    private void addHeatmapLayer() {
        if (mapmyIndiaMap.getLayer(HEAT_MAP_LAYER_ID) == null) {
            HeatmapLayer layer = new HeatmapLayer(HEAT_MAP_LAYER_ID, HEAT_MAP_SOURCE_ID);
            layer.setMaxZoom(builder.maxZoom);
            layer.setSourceLayer(HEAT_MAP_LAYER_ID);
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
            );

//            mapmyIndiaMap.addLayerAbove(layer, "waterway-label");
            mapmyIndiaMap.addLayer(layer);
        }
    }

    @Override
    public void onMapChanged(int change) {
        if (change == MapView.DID_FINISH_LOADING_STYLE) {
            updateState();
            addHeatMap();
        }
    }

    public void addHeatMap() {
        List<Feature> features = new ArrayList<>();
        for (HeatMapOption heatMapOption : builder.heatMapOptionList) {
            if (heatMapOption.mag != null) {
                Feature feature = Feature.fromGeometry(heatMapOption.point);
                feature.addNumberProperty(PROPERTY_MAG, heatMapOption.mag);
                features.add(feature);
            }
        }
        featureCollection = FeatureCollection.fromFeatures(features);
        updateState();
    }

    public static Builder builder(MapboxMap mapmyIndiaMap, MapView mapView) {
        return new Builder(mapmyIndiaMap, mapView)
                .maxZoom(9.0f);
    }


    public static class Builder {
        private MapboxMap mapmyIndiaMap;
        private MapView mapView;
        private Float maxZoom;
        private List<HeatMapOption> heatMapOptionList = new ArrayList<>();

        private Builder(MapboxMap mapmyIndiaMap, MapView mapView) {
            this.mapmyIndiaMap = mapmyIndiaMap;
            this.mapView = mapView;
        }

        public Builder maxZoom(Float maxZoom) {
            this.maxZoom = maxZoom;
            return this;
        }

        public Builder addAll(List<HeatMapOption> heatMapData) {
            this.heatMapOptionList.addAll(heatMapData);
            return this;
        }

        public Builder add(HeatMapOption heatMapOption) {
            this.heatMapOptionList.add(heatMapOption);
            return this;
        }

        public HeatMapPlugin build() {
            return new HeatMapPlugin(mapmyIndiaMap, mapView, this);
        }
    }

    public static class HeatMapOption {
        private Point point;
        private Double mag;

        public HeatMapOption(Point point, Double mag) {
            this.point = point;
            this.mag = mag;
        }

        public Point getPoint() {
            return point;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public Double getMag() {
            return mag;
        }

        public void setMag(Double mag) {
            this.mag = mag;
        }
    }
}
