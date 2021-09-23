package com.mapmyindia.sdk.demo.java.plugin;

import static com.mapmyindia.sdk.maps.style.expressions.Expression.eq;
import static com.mapmyindia.sdk.maps.style.expressions.Expression.get;
import static com.mapmyindia.sdk.maps.style.expressions.Expression.literal;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.iconAnchor;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.iconImage;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.iconOffset;
import static com.mapmyindia.sdk.maps.style.layers.PropertyFactory.iconRotate;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.geojson.Feature;
import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.Style;
import com.mapmyindia.sdk.maps.annotations.BubbleLayout;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mapmyindia.sdk.maps.style.layers.Property;
import com.mapmyindia.sdk.maps.style.layers.SymbolLayer;
import com.mapmyindia.sdk.maps.style.sources.GeoJsonSource;
import com.mapmyindia.sdk.maps.utils.BitmapUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Saksham on 31/8/19.
 */
public class MarkerPlugin implements MapView.OnDidFinishLoadingStyleListener, MapmyIndiaMap.OnMapClickListener {

    private MapmyIndiaMap mapmyIndiaMap;
    private Feature feature;
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final String INFOWINDOW_LAYER_ID = "INFOWINDOW_LAYER_ID";
    private LatLng position;
    private boolean isRemoveCallbacks = false;
    private Drawable drawable;
    private boolean isDraggable = false;
    private MapView mMapView;
    private boolean isMarkerPosition = false;

    private GeoJsonSource markerSource;
    private String PROPERTY_ROTATION = "rotation";
    private static final String PROPERTY_SELECTED = "property-selected";
    private static final String TITLE = "title";
    private static final String PROPERTY_NAME = "name";
    private static final String FILTER_TEXT = "filter_text";
    private static final String PROPERTY_ADDRESS = "address";
    private OnMarkerDraggingListener onMarkerDraggingListener;


    public MarkerPlugin(MapmyIndiaMap mapmyIndiaMap, MapView mapView) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        this.mMapView = mapView;
        updateState();
        mapView.addOnDidFinishLoadingStyleListener(this);
        mapmyIndiaMap.addOnMapClickListener(this);
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
        feature.addBooleanProperty(PROPERTY_SELECTED, false);
        mapmyIndiaMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                style.addImage(ICON_ID, BitmapUtils.getBitmapFromDrawable(drawable));
                if(style.getSource(SOURCE_ID) == null) {
                    style.addSource(markerSource = new GeoJsonSource(SOURCE_ID, feature));
                }

            }
        });
        new GenerateViewIconTask(this).execute(feature);
    }

    /**
     * Update the state of the marker
     */
    private void updateState() {
        mapmyIndiaMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                GeoJsonSource source = (GeoJsonSource) style.getSource(SOURCE_ID);
                if (source == null) {
                    initialise(style);
                    return;
                }
                if (feature != null) {
                    source.setGeoJson(feature);
                }
            }
        });

    }

    /**
     * Add symbol layer on map
     */
    private void initialise(Style style) {
        if(style.getLayer(LAYER_ID) == null) {
            SymbolLayer symbolLayer = new SymbolLayer(LAYER_ID, SOURCE_ID);
            symbolLayer.withProperties(
                    iconImage(ICON_ID),
                    iconRotate(get(PROPERTY_ROTATION)),
                    iconAllowOverlap(true),
                    iconIgnorePlacement(false)
            );
            style.addLayer(symbolLayer);
        }
        if(style.getLayer(INFOWINDOW_LAYER_ID) == null) {
            //Symbol layer for Info Window
            SymbolLayer symbolLayerInfoWindow = new SymbolLayer(INFOWINDOW_LAYER_ID, SOURCE_ID)
                    .withProperties(
                            /* show image with id title based on the value of the name feature property */
                            iconImage("{name}"),

                            /* set anchor of icon to bottom-left */
                            iconAnchor(Property.ICON_ANCHOR_BOTTOM),

                            /* all info window and marker image to appear at the same time*/
                            iconAllowOverlap(true),

                            /* offset the info window to be above the marker */
                            iconOffset(new Float[]{-2f, -25f})
                    )
                    /* setData a filter to show only when selected feature property is true */
                    .withFilter(eq((get(PROPERTY_SELECTED)), literal(true)));

            style.addLayer(symbolLayerInfoWindow);
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
        String name = null;
        String description = null;
        boolean isSelected = false;
        if(feature != null) {
            if(feature.hasProperty(PROPERTY_NAME)) {
                name = feature.getStringProperty(PROPERTY_NAME);
            }
            if(feature.hasProperty(PROPERTY_ADDRESS)) {
                description = feature.getStringProperty(PROPERTY_ADDRESS);
            }
            if(feature.hasProperty(PROPERTY_SELECTED)) {
                isSelected = feature.getBooleanProperty(PROPERTY_SELECTED);
            }
        }
        feature = Feature.fromGeometry(Point.fromLngLat(position.getLongitude(), position.getLatitude()));
        feature.addBooleanProperty(PROPERTY_SELECTED, isSelected);
        if(name != null) {
            feature.addStringProperty(PROPERTY_NAME, name);
        }
        if(description != null) {
            feature.addStringProperty(PROPERTY_ADDRESS, description);
        }
        updateState();
    }

    public void addTitle(String title) {
        if(feature != null && feature.properties() != null) {
            feature.properties().addProperty(PROPERTY_NAME, title);
        }
    }

    public void addDescription(String description) {
        if(feature != null && feature.properties() != null) {
            feature.properties().addProperty(PROPERTY_ADDRESS, description);
        }
    }

    @Override
    public boolean onMapClick(@NonNull LatLng latLng) {

        if(feature != null) {
            if(feature.hasProperty(PROPERTY_SELECTED)) {
                if(feature.getBooleanProperty(PROPERTY_SELECTED)) {
                    feature.addBooleanProperty(PROPERTY_SELECTED, false);
                    updateState();
                    return false;
                }
            }
        }
        List<Feature> features = mapmyIndiaMap.queryRenderedFeatures(this.mapmyIndiaMap.getProjection().toScreenLocation(latLng), LAYER_ID);
        if (!features.isEmpty()) {
            feature = features.get(0);
            feature.addBooleanProperty(PROPERTY_SELECTED, true);
            new GenerateViewIconTask(this).execute(feature);
        }
        return false;
    }

    /**
     * Generate Info window Icon
     */
    private static class GenerateViewIconTask extends AsyncTask<Feature, Void, HashMap<String, Bitmap>> {

        private final HashMap<String, View> viewMap = new HashMap<>();
        private final WeakReference<MarkerPlugin> activityRef;
        private final boolean refreshSource;

        GenerateViewIconTask(MarkerPlugin activity, boolean refreshSource) {
            this.activityRef = new WeakReference<>(activity);
            this.refreshSource = refreshSource;
        }

        GenerateViewIconTask(MarkerPlugin activity) {
            this(activity, true);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected HashMap<String, Bitmap> doInBackground(Feature... params) {
            MarkerPlugin activity = activityRef.get();
            if (activity != null) {
                HashMap<String, Bitmap> imagesMap = new HashMap<>();
                LayoutInflater inflater = LayoutInflater.from(activity.mMapView.getContext());

                Feature feature = params[0];

              if(feature != null) {

                    BubbleLayout bubbleLayout = (BubbleLayout)
                            inflater.inflate(R.layout.symbol_layer_info_window_layout_callout, null);
                    if (feature.hasProperty(PROPERTY_NAME)) {
                        String name1 = feature.getStringProperty(PROPERTY_NAME);
                        TextView titleTextView = bubbleLayout.findViewById(R.id.info_window_title);
                        titleTextView.setText(name1);


                        if (feature.hasProperty(PROPERTY_ADDRESS)) {
                            String address = feature.getStringProperty(PROPERTY_ADDRESS);
                            TextView addressTextView = bubbleLayout.findViewById(R.id.info_window_description);
                            addressTextView.setText(address);
                        }


//                    String style = feature.getStringProperty(PROPERTY_CAPITAL);
//                    TextView descriptionTextView = bubbleLayout.findViewById(R.id.info_window_description);
//                    descriptionTextView.setText(
//                            String.format("capital", style));

                        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        bubbleLayout.measure(measureSpec, measureSpec);

                        float measuredWidth = bubbleLayout.getMeasuredWidth();

                        bubbleLayout.setArrowPosition(measuredWidth / 2 - 5);

                        Bitmap bitmap = SymbolGenerator.generate(bubbleLayout);
                        imagesMap.put(name1, bitmap);
                    }

                }

                return imagesMap;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, Bitmap> bitmapHashMap) {
            super.onPostExecute(bitmapHashMap);
            MarkerPlugin activity = activityRef.get();
            if (activity != null && bitmapHashMap != null) {
                activity.setImageGenResults(bitmapHashMap);
                if (refreshSource) {
                    activity.updateState();
                }
                Toast.makeText(activity.mMapView.getContext(), "Marker Instructions", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * Add images on the map
     *
     * @param imageMap Hashmap of images
     */
    private void setImageGenResults(HashMap<String, Bitmap> imageMap) {
        if (mapmyIndiaMap != null) {
            mapmyIndiaMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    // calling addImages is faster as separate addImage calls for each bitmap.
                    style.addImages(imageMap);
                }
            });
        }
    }

    /**
     * Utility class to generate Bitmaps for Symbol.
     * <p>
     * Bitmaps can be added to the map with
     * </p>
     */
    private static class SymbolGenerator {

        /**
         * Generate a Bitmap from an Android SDK View.
         *
         * @param view the View to be drawn to a Bitmap
         * @return the generated bitmap
         */
        static Bitmap generate(@NonNull View view) {


            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(measureSpec, measureSpec);

            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            view.layout(0, 0, measuredWidth, measuredHeight);
            Bitmap bitmap1 = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inBitmap = bitmap1;
//            BitmapFactory.decodeByteArray(bitmap1.getNinePatchChunk(), 0, view.getHeight(), options);
            options.inSampleSize = calculateInSampleSize(options, measuredWidth, measuredHeight);
//            options.inBitmap = bitmap1;
            Bitmap bitmap = options.inBitmap;
            bitmap.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        }
    }


    /**
     * Calculate the sample size of the image
     *
     * @param options   BitmapFactory options
     * @param reqWidth  required width
     * @param reqHeight required height
     * @return calculated sample size
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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
    public void onDidFinishLoadingStyle() {
        updateState();
        addMarker(position);
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
