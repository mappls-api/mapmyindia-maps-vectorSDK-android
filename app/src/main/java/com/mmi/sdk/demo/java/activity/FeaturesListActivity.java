package com.mmi.sdk.demo.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mmi.sdk.demo.R;
import com.mmi.sdk.demo.java.InfoWindowSymbolLayerActivity;
import com.mmi.sdk.demo.java.adapter.FeaturesListAdapter;
import com.mmi.sdk.demo.java.model.Features;

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
        ArrayList<Features> featuresArrayList = new ArrayList<>();
        featuresArrayList.add(new Features(1, "Camera Features", CameraActivity.class, "Description"));
        featuresArrayList.add(new Features(2, "Map Tap", MapClickActivity.class, "Description"));
        featuresArrayList.add(new Features(3, "Map Long Tap", MapLongClickActivity.class, "Description"));
        featuresArrayList.add(new Features(4, "Add Marker", AddMarkerActivity.class, "Description"));
        featuresArrayList.add(new Features(5, "Add Custom Marker", AddCustomMarkerActivity.class, "Description"));
        featuresArrayList.add(new Features(6, "Draw Polyline", PolylineActivity.class, "Description"));
        featuresArrayList.add(new Features(7, "Draw Polygon", PolygonActivity.class, "Description"));
        featuresArrayList.add(new Features(8, "Current Location", CurrentLocationActivity.class, "Description"));
        featuresArrayList.add(new Features(9, "Auto Suggest", AutoSuggestActivity.class, "Description"));
        featuresArrayList.add(new Features(10, "Geo Code", GeoCodeActivity.class, "Description"));
        featuresArrayList.add(new Features(11, "Reverse Geocode", ReverseGeocodeActivity.class, "Description"));
        featuresArrayList.add(new Features(12, "Nearby", NearByActivity.class, "Description"));
        featuresArrayList.add(new Features(13, "Get Direction", DirectionActivity.class, "Description"));
        featuresArrayList.add(new Features(14, "Get Distance", DistanceActivity.class, "Description"));
        featuresArrayList.add(new Features(15, "Marker Rotation and Transition", MarkerRotationTransitionActivity.class, "Description"));
        featuresArrayList.add(new Features(16, "Polyline with Gradient color", GradientPolylineActivity.class, "Description"));
        featuresArrayList.add(new Features(17, "Semicircle polyline", SemiCirclePolylineActivity.class, "Description"));
        featuresArrayList.add(new Features(18, "Animate Car", CarAnimationActivity.class, "Description"));
        featuresArrayList.add(new Features(19, "Marker Dragging", MarkerDraggingActivity.class, "Description"));

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
