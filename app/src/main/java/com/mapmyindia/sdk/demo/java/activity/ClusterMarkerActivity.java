package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityClusterMarkerBinding;
import com.mapmyindia.sdk.demo.java.plugin.ClusterMarkerPlugin;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mapmyindia.sdk.maps.geometry.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

public class ClusterMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityClusterMarkerBinding mBinding;
    private final List<LatLng> latLngList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cluster_marker);
        mBinding.mapView.onCreate(savedInstanceState);
        latLngList.add(new LatLng(28.551635, 77.268805));
        latLngList.add(new LatLng(28.551041, 77.267979));
        latLngList.add(new LatLng(28.552115, 77.265833));
        latLngList.add(new LatLng(28.559786, 77.238859));
        latLngList.add(new LatLng(28.561535, 77.233345));
        latLngList.add(new LatLng(28.562469, 77.235072));
        latLngList.add(new LatLng(28.435931, 77.304689));
        latLngList.add(new LatLng(28.436214, 77.304936));
        latLngList.add(new LatLng(28.438827, 77.308337));
        latLngList.add(new LatLng(28.489028, 77.091252));
        latLngList.add(new LatLng(28.486831, 77.094492));
        latLngList.add(new LatLng(28.486491, 77.094374));
        latLngList.add(new LatLng(28.491510, 77.082149));
        latLngList.add(new LatLng(28.474800, 77.065233));
        latLngList.add(new LatLng(28.471245, 77.072722));
        latLngList.add(new LatLng(28.458440, 77.073179));
        mBinding.mapView.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mBinding.mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBinding.mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.mapView.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull MapmyIndiaMap mapmyIndiaMap) {
        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds.Builder().includes(latLngList).build(), 10, 10, 10, 10));
        ClusterMarkerPlugin plugin = new ClusterMarkerPlugin(mBinding.mapView, mapmyIndiaMap);
        plugin.setMarkers(latLngList);
    }

    @Override
    public void onMapError(int i, String s) {

    }
}