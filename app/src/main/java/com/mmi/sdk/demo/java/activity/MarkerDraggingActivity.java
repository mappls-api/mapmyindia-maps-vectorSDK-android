package com.mmi.sdk.demo.java.activity;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.android.gestures.MultiFingerTapGestureDetector;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mmi.sdk.demo.R;
import com.mmi.sdk.demo.java.plugin.MarkerPlugin;

public class MarkerDraggingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mMapboxMap;
    private LatLng latLng = new LatLng(28.705436, 77.100462);
    private MarkerPlugin markerPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);

        mapView = findViewById(R.id.mapBoxId);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mMapboxMap = mapboxMap;
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        initMarker();
    }

    private void initMarker() {
        markerPlugin = new MarkerPlugin(mMapboxMap, mapView);
        markerPlugin.icon(getResources().getDrawable(R.drawable.placeholder));
        markerPlugin.addMarker(latLng);
        markerPlugin.draggable(true);
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
