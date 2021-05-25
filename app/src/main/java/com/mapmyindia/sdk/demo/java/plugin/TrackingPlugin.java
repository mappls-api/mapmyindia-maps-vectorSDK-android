package com.mapmyindia.sdk.demo.java.plugin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.core.content.ContextCompat;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.turf.TurfMeasurement;
import com.mapmyindia.sdk.demo.R;
import com.mmi.services.api.directions.models.DirectionsRoute;
import com.mmi.services.utils.Constants;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotationAlignment;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 ** Created by Saksham on 01-05-2021.
 **/
public class TrackingPlugin implements MapView.OnMapChangedListener {


    private static final String PROPERTY_BEARING = "bearing";
    private static final String CAR = "car";
    private static final String CAR_LAYER = "car-layer";
    private static final String CAR_SOURCE = "car-source";

    private static final String POLYLINE_LAYER = "polyline-layer";
    private static final String POLYLINE_SOURCE = "polyline-source";

    private MapboxMap mapmyIndiaMap;
    private MapView mMapView;
    private Car car;
    private Feature markerFeature;
    private Feature polylineFeature;
    private boolean isClearAllCallBacks;


    @Override
    public void onMapChanged(int change) {
        if (change == MapView.DID_FINISH_LOADING_STYLE) {
            mapmyIndiaMap.addImage(CAR, BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(mMapView.getContext(), R.drawable.ic_bike_icon_grey)));
            updatePolylineSource();
            updateMarkerSource();
        }
    }

    public TrackingPlugin(MapView mapView, MapboxMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        this.mMapView = mapView;
        mapmyIndiaMap.addImage(CAR, BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(mapView.getContext(), R.drawable.ic_bike_icon_grey)));
        initialisePolylineLayerAndSource();
        initialiseMarkerLayerAndSource();
        mapView.addOnMapChangedListener(this);
    }

    public void addMarker(Point point) {
        markerFeature = Feature.fromGeometry(point);
        markerFeature.addNumberProperty(PROPERTY_BEARING, 0);
        updateMarkerSource();
    }

    public void updatePolyline(DirectionsRoute route) {
        if (route != null && route.geometry() != null) {
            polylineFeature = Feature.fromGeometry(LineString.fromPolyline(route.geometry(), Constants.PRECISION_6));
            updatePolylineSource();
        }
    }

    /**
     * Animate Marker from current point to next point
     */
    public void animateCar(Point start, Point end) {
        isClearAllCallBacks = false;
        LatLng startLatLng = new LatLng(start.latitude(), start.longitude());
        LatLng nextLatLng = new LatLng(end.latitude(), end.longitude());
        car = new Car(startLatLng, nextLatLng);

        ValueAnimator valueAnimator = ValueAnimator.ofObject(new LatLngEvaluator(), startLatLng, nextLatLng);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!isClearAllCallBacks) {

                    if (car.current == null || !car.current.equals(nextLatLng)) {
                        LatLng latLng = (LatLng) animation.getAnimatedValue();
                        markerFeature = Feature.fromGeometry(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
                        car.current = latLng;
                        markerFeature.addNumberProperty(PROPERTY_BEARING, Car.getBearing(car.current, car.next));

                        updateMarkerSource();
                    }
                }
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (!isClearAllCallBacks) {
                    markerFeature.properties().addProperty(PROPERTY_BEARING, Car.getBearing(car.current, car.next));
                }
            }
        });
//        (long) (7 * car.current.distanceTo(car.next))
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.start();
    }

    private void updateMarkerSource() {
        if (mapmyIndiaMap.getSource(CAR_SOURCE) == null) {
            initialiseMarkerLayerAndSource();
            return;
        }
        GeoJsonSource source = (GeoJsonSource) mapmyIndiaMap.getSource(CAR_SOURCE);
        source.setGeoJson(markerFeature);
    }

    private void updatePolylineSource() {
        if (mapmyIndiaMap.getSource(POLYLINE_SOURCE) == null) {
            initialisePolylineLayerAndSource();
            return;
        }
        GeoJsonSource source = (GeoJsonSource) mapmyIndiaMap.getSource(POLYLINE_SOURCE);
        source.setGeoJson(polylineFeature);
    }


    /**
     * Initialise the marker
     */
    private void initialiseMarkerLayerAndSource() {
        if (mapmyIndiaMap.getSource(CAR_SOURCE) == null) {
            if (markerFeature == null) {
                mapmyIndiaMap.addSource(new GeoJsonSource(CAR_SOURCE));
            } else {
                mapmyIndiaMap.addSource(new GeoJsonSource(CAR_SOURCE, markerFeature));
            }
        }
        if (mapmyIndiaMap.getLayer(CAR_LAYER) == null) {
            //Symbol layer for car
            SymbolLayer symbolLayer = new SymbolLayer(CAR_LAYER, CAR_SOURCE);
            symbolLayer.withProperties(
                    iconImage(CAR),
                    iconRotate(get(PROPERTY_BEARING)),
                    iconAllowOverlap(true),
                    iconIgnorePlacement(true),
                    iconRotationAlignment(Property.ICON_ROTATION_ALIGNMENT_MAP)

            );
            mapmyIndiaMap.addLayerAbove(symbolLayer, POLYLINE_LAYER);
        }

    }

    private void initialisePolylineLayerAndSource() {
        if (mapmyIndiaMap.getSource(POLYLINE_SOURCE) == null) {
            mapmyIndiaMap.addSource(new GeoJsonSource(POLYLINE_SOURCE));
        }
        if (mapmyIndiaMap.getLayer(POLYLINE_LAYER) == null) {
            LineLayer lineLayer = new LineLayer(POLYLINE_LAYER, POLYLINE_SOURCE);
            lineLayer.setProperties(
                    lineCap(Property.LINE_CAP_ROUND),
                    lineJoin(Property.LINE_JOIN_ROUND),
                    lineColor(Color.BLACK),
                    lineWidth(4f)
            );
            mapmyIndiaMap.addLayer(lineLayer);
        }

    }


    /**
     * Evaluator for LatLng pairs
     */
    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {

        private LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    }


    private static class Car {
        private LatLng next;
        private LatLng current;

        Car(LatLng current, LatLng next) {

            this.current = current;
            this.next = next;
        }



        /**
         * Get the bearing between two points
         *
         * @param from Current point
         * @param to   Next Point
         * @return bearing value in degree
         */
        private static float getBearing(LatLng from, LatLng to) {
            return (float) TurfMeasurement.bearing(
                    Point.fromLngLat(from.getLongitude(), from.getLatitude()),
                    Point.fromLngLat(to.getLongitude(), to.getLatitude())
            );
        }
    }
}
