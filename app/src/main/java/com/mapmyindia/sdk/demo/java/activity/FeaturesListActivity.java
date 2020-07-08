package com.mapmyindia.sdk.demo.java.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.adapter.FeaturesListAdapter;
import com.mapmyindia.sdk.demo.java.model.Features;

import java.util.ArrayList;


/**
 * Created by CEINFO on 26-02-2019.
 */

public class FeaturesListActivity extends AppCompatActivity {

    private RecyclerView featuresRecycleView;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_list_activity);
        init();
    }

    private void init() {
        int i= 0;
        ArrayList<Features> featuresArrayList = new ArrayList<>();
        featuresArrayList.add(new Features(++i, "Camera Features", CameraActivity.class, "Animate, Move or Ease Camera Position"));
        featuresArrayList.add(new Features(++i, "Map Tap", MapClickActivity.class, "Tap on map and get tapped Latitude Longitude"));
        featuresArrayList.add(new Features(++i, "Map Long Tap", MapLongClickActivity.class, "Long press on map and get Latitude Longitude"));
        featuresArrayList.add(new Features(++i, "Add Marker", AddMarkerActivity.class, "Add a marker on the map"));
        featuresArrayList.add(new Features(++i, "Add Custom Infowindow", AddCustomInfoWindowActivity.class, "Show custom info window when user click on a marker"));
        featuresArrayList.add(new Features(++i, "Add Custom Marker", AddCustomMarkerActivity.class, "Customize a marker, change its marker image"));
        featuresArrayList.add(new Features(++i, "Draw Polyline", PolylineActivity.class, "Draw a polyline with given list of latitude and longitude"));
        featuresArrayList.add(new Features(++i, "Draw Polygon", PolygonActivity.class, "Draw a polygon on the map"));
        featuresArrayList.add(new Features(++i, "Current Location", CurrentLocationActivity.class, "Show current location on the map"));
        featuresArrayList.add(new Features(++i, "Auto  Suggest", AutoSuggestActivity.class, "Auto suggest places on the map"));
        featuresArrayList.add(new Features(++i, "Location Camera Options", LocationCameraActivity.class, "Location camera options for render and tracking modes"));
        featuresArrayList.add(new Features(++i, "Geo Code", GeoCodeActivity.class, "Geocode rest API call"));
        featuresArrayList.add(new Features(++i, "Reverse Geocode", ReverseGeocodeActivity.class, "Reverse Geocode rest API call"));
        featuresArrayList.add(new Features(++i, "Nearby", NearByActivity.class, "Show nearby results on the map"));
        featuresArrayList.add(new Features(++i, "Get Direction", DirectionActivity.class, "get directions between two points and show on the map"));
        featuresArrayList.add(new Features(++i, "Get Distance", DistanceActivity.class, "Get distance between points"));
        featuresArrayList.add(new Features(++i, "Marker Rotation and Transition", MarkerRotationTransitionActivity.class, "Rotate a marker by given degree and animate the marker to a new position"));
        featuresArrayList.add(new Features(++i, "Polyline with Gradient color", GradientPolylineActivity.class, "Draw a gradient color polyline"));
        featuresArrayList.add(new Features(++i, "Semicircle polyline", SemiCirclePolylineActivity.class, "Draw a semicircle polyline on the map"));
        featuresArrayList.add(new Features(++i, "Animate Car", CarAnimationActivity.class, "Animate a car marker on predefined route"));
        featuresArrayList.add(new Features(++i, "Marker Dragging", MarkerDraggingActivity.class, "Drag a marker"));
        featuresArrayList.add(new Features(++i, "Indoor", IndoorActivity.class, "Show indoor widget when focus on multi storey building"));
        featuresArrayList.add(new Features(++i, "Show Heatmap data", HeatMapActivity.class, "Add a heatmap to visualize data"));
        featuresArrayList.add(new Features(++i, "Place Autocomplete Widget", PlaceAutoCompleteActivity.class, "Location search functionality and UI to search a place"));
        featuresArrayList.add(new Features(++i, "MapmyIndia Safety Plugin", SafetyPluginActivity.class, "MapmyIndia Safety Plugin, To show you current location safety"));
        featuresArrayList.add(new Features(++i, "Map Gestures", GesturesActivity.class, "Gestures detection for map view"));
        featuresArrayList.add(new Features(++i, "Snake Polyline", SnakeMotionPolylineActivity.class, "Snake a polyline from the origin to the destination"));
        featuresArrayList.add(new Features(++i, "Interactive Layer", InteractiveLayerActivity.class, "Show Interactive CORONA Layers on the map view"));
        featuresArrayList.add(new Features(++i, "Direction Step", DirectionStepActivity.class, "How to show textual instructions and maneuver icon to the user"));
        featuresArrayList.add(new Features(++i, "Map Scalebar", ScalebarActivity.class, "Add a scale bar on map view to determine distance based on zoom level"));
        featuresArrayList.add(new Features(++i, "Place Picker", PickerActivity.class, "Place Picker to search and choose a specific location"));
        featuresArrayList.add(new Features(++i, "GeoFence", GeoFenceActivity.class, "Highly customizable UI widget to create/edit geofence widget"));
        featuresArrayList.add(new Features(++i, "Map in Fragment", MapFragmentActivity.class, "Way to use mapview in Fragment"));

        featuresRecycleView = findViewById(R.id.featuresRecycleView);
        mLayoutManager = new LinearLayoutManager(FeaturesListActivity.this);
        featuresRecycleView.setLayoutManager(mLayoutManager);
        FeaturesListAdapter featuresListAdapter = new FeaturesListAdapter(featuresArrayList) {
            @Override
            public void redirectOnFeatureCallBack(Features features) {
                switchToActivity(features.getFeatureActivityName());
            }
        };
        featuresRecycleView.setAdapter(featuresListAdapter);
    }

    private void switchToActivity(Class featureActivityName) {
        Intent intent = new Intent(FeaturesListActivity.this, featureActivityName);
        startActivity(intent);
    }
}
