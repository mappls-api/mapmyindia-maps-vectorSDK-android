package com.mapmyindia.sdk.demo.java.plugin;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotate;

/**
 * Created by Saksham on 31/8/19.
 */
public class MarkerPlugin implements MapView.OnMapChangedListener {

    private MapboxMap mapmyIndiaMap;
    private Feature feature;
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private LatLng position;
    private boolean isRemoveCallbacks = false;
    private Drawable drawable;
    private boolean isDraggable = false;
    private MapView mMapView;
    private boolean isMarkerPosition = false;

    private GeoJsonSource markerSource;
    private String PROPERTY_ROTATION = "rotation";
    private OnMarkerDraggingListener onMarkerDraggingListener;


    public MarkerPlugin(MapboxMap mapmyIndiaMap, MapView mapView) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        this.mMapView = mapView;
        updateState();
        mapView.addOnMapChangedListener(this);
        initialiseForDraggingMarker();
    }

    public void setOnMarkerDraggingListener(OnMarkerDraggingListener onMarkerDraggingListener) {
        this.onMarkerDraggingListener = onMarkerDraggingListener;
    }

    /**
     * Add touch listener for dragging the marker
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initialiseForDraggingMarker() {
        mMapView.setOnTouchListener((view, motionEvent) -> {
            if (isDraggable) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    isMarkerPosition = isMarkerPosition(new PointF(motionEvent.getX(), motionEvent.getY()));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    if (isMarkerPosition) {
                        LatLng position = mapmyIndiaMap.getProjection().fromScreenLocation(new PointF(motionEvent.getX(), motionEvent.getY()));
                        updateMarkerPosition(position);
                        if (onMarkerDraggingListener != null) {
                            onMarkerDraggingListener.onMarkerDragging(position);
                        }
                    }
                }
            }

            return isMarkerPosition && isDraggable;
        });
    }

    /**
     * Check that the point is on the marker
     *
     * @param pointF the point on screen touch
     * @return check is pointF is the marker
     */
    private boolean isMarkerPosition(PointF pointF) {
        List<Feature> features = mapmyIndiaMap.queryRenderedFeatures(pointF, LAYER_ID);
        return !features.isEmpty();
    }

    /**
     * Add Marker Icon
     *
     * @param icon image
     */
    public void icon(Drawable icon) {
        this.drawable = icon;

    }

    /**
     * Add image on map for marker and source on map
     *
     * @param position position of the marker
     */
    public void addMarker(LatLng position) {
        isRemoveCallbacks = false;
        this.position = position;
        feature = Feature.fromGeometry(Point.fromLngLat(position.getLongitude(), position.getLatitude()));
        mapmyIndiaMap.addImage(ICON_ID, BitmapUtils.getBitmapFromDrawable(drawable));
        if(mapmyIndiaMap.getSource(SOURCE_ID) == null) {
            mapmyIndiaMap.addSource(markerSource = new GeoJsonSource(SOURCE_ID, feature));
        }
    }

    /**
     * Update the state of the marker
     */
    private void updateState() {
        GeoJsonSource source = (GeoJsonSource) mapmyIndiaMap.getSource(SOURCE_ID);
        if (source == null) {
            initialise();
            return;
        }
        if (feature != null) {
            markerSource.setGeoJson(feature);
        }
    }

    /**
     * Add symbol layer on map
     */
    private void initialise() {
        if(mapmyIndiaMap.getLayer(LAYER_ID) == null) {
            SymbolLayer symbolLayer = new SymbolLayer(LAYER_ID, SOURCE_ID);
            symbolLayer.withProperties(
                    iconImage(ICON_ID),
                    iconRotate(get(PROPERTY_ROTATION)),
                    iconAllowOverlap(true),
                    iconIgnorePlacement(false)
            );
            mapmyIndiaMap.addLayer(symbolLayer);
        }
    }

    /**
     * Start rotation of marker
     */
    public void startRotation() {
        isRemoveCallbacks = false;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 360);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            if (!isRemoveCallbacks) {
                Float bearing = (Float) valueAnimator1.getAnimatedValue();
                rotate(bearing);
            }
        });
        valueAnimator.start();
    }

    /**
     * Update the position of the marker
     *
     * @param position updated position of the marker
     */
    public void updateMarkerPosition(LatLng position) {
        feature = Feature.fromGeometry(Point.fromLngLat(position.getLongitude(), position.getLatitude()));
        updateState();
    }

    /**
     * Rotate the marker
     *
     * @param rotate bearing
     */
    public void rotate(float rotate) {
        if (feature != null && feature.properties() != null) {
            feature.properties().addProperty(PROPERTY_ROTATION, rotate);
        }
        updateState();
    }

    /**
     * If true than marker is draggable
     */
    public void draggable(boolean draggable) {
        this.isDraggable = draggable;
    }

    /**
     * Start transition of the marker
     * Only move from start point to end point
     *
     * @param latLngStart Start Point
     * @param latLngEnd   End point
     */
    public void startTransition(LatLng latLngStart, LatLng latLngEnd) {
        isRemoveCallbacks = false;
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new LatLngEvaluator(), latLngStart, latLngEnd);
        valueAnimator.setDuration(15000);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            if (!isRemoveCallbacks) {
                LatLng latLng = (LatLng) valueAnimator1.getAnimatedValue();
                if (!mapmyIndiaMap.getProjection().getVisibleRegion().latLngBounds.contains(latLng)) {
                    mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                }
                updateMarkerPosition(latLng);
            }
        });
        valueAnimator.start();
    }

    @Override
    public void onMapChanged(int change) {
        if (change == MapView.DID_FINISH_LOADING_STYLE) {
            updateState();
            addMarker(position);
        }
    }

    /**
     * LatLng Evaluator
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

    /**
     * Remove all callbacks
     */
    public void removeCallbacks() {
        isRemoveCallbacks = true;
    }

    public interface OnMarkerDraggingListener {
        void onMarkerDragging(LatLng position);
    }
}
