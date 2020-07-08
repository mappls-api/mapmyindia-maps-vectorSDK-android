package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.LayoutBasicGeofenceBinding;
import com.mapmyindia.sdk.geofence.ui.GeoFence;
import com.mapmyindia.sdk.geofence.ui.listeners.GeoFenceViewCallback;

public class BasicGeoFenceActivity extends AppCompatActivity implements GeoFenceViewCallback {
    LayoutBasicGeofenceBinding mBinding;
    GeoFence geoFence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.layout_basic_geofence);
        geoFence = new GeoFence();
        geoFence.setPolygon(false);
        geoFence.setCircleCenter(new LatLng(24.6496185, 77.3062072));
        geoFence.setCircleRadius(200);


        mBinding.geofenceView.setGeoFence(geoFence);
        mBinding.geofenceView.onCreate(savedInstanceState);
        mBinding.geofenceView.setGeoFenceViewCallback(BasicGeoFenceActivity.this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        mBinding.geofenceView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.geofenceView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.geofenceView.onPause();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.geofenceView.onLowMemory();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mBinding.geofenceView.onSaveInstanceState(outState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.geofenceView.onDestroy();

    }

    @Override
    public void onGeoFenceReady(MapboxMap mapmyIndiaMap) {

    }

    @Override
    public void geoFenceType(boolean b) {

    }

    @Override
    public void onCircleRadiusChanging(int i) {

    }

    @Override
    public void onUpdateGeoFence(GeoFence geoFence) {


    }

    @Override
    public void hasIntersectionPoints() {

    }
}


