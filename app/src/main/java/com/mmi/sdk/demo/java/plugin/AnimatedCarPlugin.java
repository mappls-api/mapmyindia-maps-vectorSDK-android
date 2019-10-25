package com.mmi.sdk.demo.java.plugin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.annotations.BubbleLayout;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.turf.TurfMeasurement;
import com.mmi.sdk.demo.R;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;


public class AnimatedCarPlugin implements MapView.OnMapChangedListener {


    private static final String PROPERTY_BEARING = "bearing";
    private static final String CAR = "car";
    private static final String CAR_LAYER = "car-layer";
    private static final String CAR_SOURCE = "car-source";

    private static final String SOURCE_LAYER_INFO_WINDOW = "info-window-poi-marker-layer";
    private static final String PROPERTY_SELECTED = "property-selected";
    private static final String TITLE = "title";
    private static final String PROPERTY_NAME = "name";
    private static final String FILTER_TEXT = "filter_text";
    private static final String PROPERTY_ADDRESS = "address";

    private MapboxMap mapboxMap;
    private Car car;
    private GeoJsonSource carSource;
    private LatLng nextPoint;
    private Context context;
    private OnUpdatePoint onUpdatePoint;
    private boolean isClearAllCallBacks;
    private FeatureCollection featureCollection;
    private LatLng latLng;

    public void setOnUpdateNextPoint(OnUpdatePoint onUpdatePoint) {
        this.onUpdatePoint = onUpdatePoint;
    }

    @Override
    public void onMapChanged(int change) {
        if (change == MapView.DID_FINISH_LOADING_STYLE) {
            updateState();
            addMainCar(latLng, false);
        }
    }

    public interface OnUpdatePoint {
        void updateNextPoint();
    }

    /**
     * To remove all callbacks
     */
    public void clearAllCallBacks() {
        isClearAllCallBacks = true;
    }

    /**
     * Add all callbacks
     */
    public void addAllCallBacks() {
        isClearAllCallBacks = false;
    }

    public AnimatedCarPlugin(Context context, MapView mapView, MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        this.context = context;
        updateState();
        mapView.addOnMapChangedListener(this);
        this.mapboxMap.addOnMapClickListener(this::handleClickIcon);
    }

    /**
     * Animate Marker from current point to next point
     */
    public void animateCar() {
        isClearAllCallBacks = false;
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new LatLngEvaluator(), car.current, car.next);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private LatLng latLng;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!isClearAllCallBacks) {
                    latLng = (LatLng) animation.getAnimatedValue();
                    car.current = latLng;
                    updateCarSource();
                    car.feature.properties().addProperty(PROPERTY_BEARING, Car.getBearing(car.current, car.next));
                }
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isClearAllCallBacks) {
                    onUpdatePoint.updateNextPoint();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (!isClearAllCallBacks) {
                    car.feature.properties().addProperty(PROPERTY_BEARING, Car.getBearing(car.current, car.next));
                }
            }
        });
//        (long) (7 * car.current.distanceTo(car.next))
        valueAnimator.setDuration((long) (100 * car.current.distanceTo(car.next)));
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.start();
    }

    /**
     * For updating the next point
     *
     * @param nextPoint next point where the marker move
     */
    public void updateNextPoint(LatLng nextPoint) {
        this.nextPoint = nextPoint;
        car.setNext(nextPoint);
    }

    /**
     * Update the maker feature
     */
    private void updateCarSource() {
        car.updateFeature();
        carSource.setGeoJson(car.feature);
    }


    /**
     * Add marker & Source
     *
     * @param latLng   position of the marker
     * @param selected if it true than it shows info window
     */
    public void addMainCar(LatLng latLng, boolean selected) {
        this.latLng = latLng;
        this.nextPoint = latLng;
        setVisibility(true);
        JsonObject properties = new JsonObject();
        properties.addProperty(PROPERTY_BEARING, Car.getBearing(latLng, nextPoint));
        properties.addProperty(FILTER_TEXT, "filter");
        properties.addProperty(TITLE, "Tittle");
        properties.addProperty(PROPERTY_NAME, "name");
        properties.addProperty(PROPERTY_ADDRESS, "Address");
        if (selected)
            properties.addProperty(PROPERTY_SELECTED, false);

        Feature feature = Feature.fromGeometry(
                Point.fromLngLat(
                        latLng.getLongitude(),
                        latLng.getLatitude()), properties);

        featureCollection = FeatureCollection.fromFeatures(new Feature[]{feature});

        car = new Car(feature, nextPoint);
        mapboxMap.addImage(CAR,
                ((BitmapDrawable) context.getResources().getDrawable(R.drawable.placeholder)).getBitmap());


        carSource = new GeoJsonSource(CAR_SOURCE, featureCollection);
        mapboxMap.addSource(carSource);

        new GenerateViewIconTask(new WeakReference<>(this).get()).execute(featureCollection);
    }


    /**
     * Generate Info window Icon
     */
    private static class GenerateViewIconTask extends AsyncTask<FeatureCollection, Void, HashMap<String, Bitmap>> {

        private final HashMap<String, View> viewMap = new HashMap<>();
        private final WeakReference<AnimatedCarPlugin> activityRef;
        private final boolean refreshSource;

        GenerateViewIconTask(AnimatedCarPlugin activity, boolean refreshSource) {
            this.activityRef = new WeakReference<>(activity);
            this.refreshSource = refreshSource;
        }

        GenerateViewIconTask(AnimatedCarPlugin activity) {
            this(activity, true);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected HashMap<String, Bitmap> doInBackground(FeatureCollection... params) {
            AnimatedCarPlugin activity = activityRef.get();
            if (activity != null) {
                HashMap<String, Bitmap> imagesMap = new HashMap<>();
                LayoutInflater inflater = LayoutInflater.from(activity.getContext());

                FeatureCollection featureCollection = params[0];

                for (Feature feature : Objects.requireNonNull(featureCollection.features())) {

                    BubbleLayout bubbleLayout = (BubbleLayout)
                            inflater.inflate(R.layout.symbol_layer_info_window_layout_callout, null);
                    if (feature.hasProperty(PROPERTY_NAME)) {
                        String name1 = feature.getStringProperty(PROPERTY_NAME);
                        TextView titleTextView = bubbleLayout.findViewById(R.id.info_window_title);
                        titleTextView.setText(name1);

                        String address = feature.getStringProperty(PROPERTY_ADDRESS);
                        TextView addressTextView = bubbleLayout.findViewById(R.id.info_window_description);
                        addressTextView.setText(address);


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
                        viewMap.put(name1, bubbleLayout);
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
            AnimatedCarPlugin activity = activityRef.get();
            if (activity != null && bitmapHashMap != null) {
                activity.setImageGenResults(bitmapHashMap);
                if (refreshSource) {
                    activity.refreshSource();
                }
                Toast.makeText(activity.getContext(), "Marker Instructions", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private Context getContext() {
        return context;
    }

    /**
     * This method handles click events for SymbolLayer symbols.
     * <p>
     * When a SymbolLayer icon is clicked, we moved that feature to the selected state.
     * </p>
     *
     * @param screenPoint the point on screen clicked
     */
    private void handleClickIcon(LatLng screenPoint) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(this.mapboxMap.getProjection().toScreenLocation(screenPoint), CAR_LAYER, SOURCE_LAYER_INFO_WINDOW);
        if (!features.isEmpty()) {
            String name = features.get(0).getStringProperty(PROPERTY_NAME);
            List<Feature> featureList = featureCollection.features();
            for (int i = 0; i < Objects.requireNonNull(featureList).size(); i++) {
                if (featureList.get(i).hasProperty(PROPERTY_NAME)) {
                    if (featureList.get(i).getStringProperty(PROPERTY_NAME).equals(name)) {
                        Timber.tag("TAG").d(featureList.get(i).toJson());
                        car.setSelected(!car.selected);
                    }
                }
            }
        }
    }


    /**
     * Add images on the map
     *
     * @param imageMap Hashmap of images
     */
    private void setImageGenResults(HashMap<String, Bitmap> imageMap) {
        if (mapboxMap != null) {
            // calling addImages is faster as separate addImage calls for each bitmap.
            mapboxMap.addImages(imageMap);
        }
    }

    /**
     * Refresh the Source to update state of the marker
     */
    private void refreshSource() {
//        updateCarSource();
        updateState();
    }


    /**
     * Update the state of the marker.
     */
    private void updateState() {
        GeoJsonSource source = (GeoJsonSource) mapboxMap.getSource(CAR_SOURCE);
        if (source == null) {
            initialise();
            return;
        }
        if (featureCollection != null) {
            carSource.setGeoJson(featureCollection);
        }

        setVisibility(true);
    }


    /**
     * Set the visibility of all layers
     *
     * @param visible if it is true than layers will be visible
     */
    private void setVisibility(boolean visible) {
        if (layerIds == null)
            return;
        List<Layer> layers = mapboxMap.getLayers();
        if (layers.size() > 0) {
            for (Layer layer : layers) {
                if (layerIds.contains(layer.getId())) {

                    layer.setProperties(visibility(visible ? Property.VISIBLE : Property.NONE));
                }
            }
        }
    }

    //List of Layer Ids
    private List<String> layerIds;

    /**
     * Initialise the marker
     */
    private void initialise() {
        layerIds = new ArrayList<>();

        //Symbol layer for car
        SymbolLayer symbolLayer = new SymbolLayer(CAR_LAYER, CAR_SOURCE);
        symbolLayer.withProperties(
                iconImage(CAR),
                iconRotate(get(PROPERTY_BEARING)),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)

        );
        mapboxMap.addLayer(symbolLayer);
        layerIds.add(symbolLayer.getId());

        //Symbol layer for Info Window
        SymbolLayer symbolLayerInfoWindow = new SymbolLayer(SOURCE_LAYER_INFO_WINDOW, CAR_SOURCE)
                .withProperties(
                        /* show image with id title based on the value of the name feature property */
                        iconImage("{name}"),

                        /* set anchor of icon to bottom-left */
                        iconAnchor(Property.ICON_ANCHOR_BOTTOM_LEFT),

                        /* all info window and marker image to appear at the same time*/
                        iconAllowOverlap(true),

                        /* offset the info window to be above the marker */
                        iconOffset(new Float[]{-2f, -25f})
                )
                /* setData a filter to show only when selected feature property is true */
                .withFilter(eq((get(PROPERTY_SELECTED)), literal(true)));

        mapboxMap.addLayer(symbolLayerInfoWindow);
        layerIds.add(symbolLayerInfoWindow.getId());
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
        private Feature feature;
        private LatLng next;
        private LatLng current;
        private boolean selected;

        Car(Feature feature, LatLng next) {
            this.feature = feature;
            Point point = ((Point) feature.geometry());
            if (point != null)
                this.current = new LatLng(point.latitude(), point.longitude());
            this.next = next;
        }

        /**
         * Set the value that marker is selected or not
         *
         * @param selected if it is true than Info window is visible
         */
        private void setSelected(Boolean selected) {
            this.selected = selected;
        }

        /**
         * Set the next point
         *
         * @param next next point
         */
        void setNext(LatLng next) {
            this.next = next;
        }


        /**
         * Update the feature of the marker
         */
        void updateFeature() {

            feature = Feature.fromGeometry(Point.fromLngLat(
                    current.getLongitude(),
                    current.getLatitude())
            );

            feature.properties().addProperty(PROPERTY_BEARING, getBearing(current, next));
            feature.properties().addProperty(FILTER_TEXT, "filter");
            feature.properties().addProperty(TITLE, "Tittle");
            feature.properties().addProperty(PROPERTY_NAME, "name");
            feature.properties().addProperty(PROPERTY_ADDRESS, "Address");
            feature.properties().addProperty(PROPERTY_SELECTED, selected);
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
