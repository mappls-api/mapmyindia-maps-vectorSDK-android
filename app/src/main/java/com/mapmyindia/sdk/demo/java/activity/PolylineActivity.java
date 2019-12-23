package com.mapmyindia.sdk.demo.java.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;

import java.util.ArrayList;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class PolylineActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<LatLng> listOfLatlang = new ArrayList<>();
    private MapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {

        mapboxMap.setMinZoomPreference(4.5);
        mapboxMap.setMaxZoomPreference(18.5);


        mapboxMap.setPadding(20, 20, 20, 20);


        mapboxMap.setCameraPosition(setCameraAndTilt());
        listOfLatlang.add(new LatLng(28.705436, 77.100462));
        listOfLatlang.add(new LatLng(28.705191, 77.100784));
        listOfLatlang.add(new LatLng(28.704646, 77.101514));
        listOfLatlang.add(new LatLng(28.704194, 77.101171));
        listOfLatlang.add(new LatLng(28.704083, 77.101066));
        listOfLatlang.add(new LatLng(28.703900, 77.101318));
        mapboxMap.addPolyline(new PolylineOptions().addAll(listOfLatlang).color(Color.parseColor("#3bb2d0")).width(4));

        /* this is done for animating/moving camera to particular position */

        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(listOfLatlang).build();
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 70));
    }

    protected CameraPosition setCameraAndTilt() {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                28.551087, 77.257373)).zoom(14).tilt(0).build();
        return cameraPosition;
    }


    @Override
    public void onMapError(int i, String s) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
