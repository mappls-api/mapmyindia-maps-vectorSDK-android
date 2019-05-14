package com.mmi.sdk.demo.java.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mmi.sdk.demo.R;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class CameraActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, MapboxMap.OnCameraMoveListener, MapboxMap.OnCameraIdleListener, MapboxMap.OnCameraMoveCanceledListener {

    private MapboxMap mapboxMap;
    private MapView mapView;
    private TextView moveCamera, easeCamera, animateCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        mapView = findViewById(R.id.mapBoxId);
        mapView.onCreate(savedInstanceState);
        initReferences();
        initListeners();
    }

    private void initListeners() {
        mapView.getMapAsync(this);

        // Layout related listeners
        moveCamera.setOnClickListener(this);
        easeCamera.setOnClickListener(this);
        animateCamera.setOnClickListener(this);

    }

    private void initReferences() {
        moveCamera = findViewById(R.id.moveCamera);
        easeCamera = findViewById(R.id.easeCamera);
        animateCamera = findViewById(R.id.animateCamera);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setMinZoomPreference(4.5);
        mapboxMap.setMaxZoomPreference(18.5);


        mapboxMap.setPadding(20, 20, 20, 20);


        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                25.321684, 82.987289)).zoom(14).tilt(0).build();
        mapboxMap.setCameraPosition(cameraPosition);

        //Map related listeners
        mapboxMap.addOnCameraMoveListener(this);
        mapboxMap.addOnCameraIdleListener(this);
        mapboxMap.addOnCameraMoveCancelListener(this);
    }

    @Override
    public void onMapError(int i, String s) {
        // Here , Handle Map related Error on map loading
    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moveCamera:
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        22.553147478403194,
                        77.23388671875), 14));

                break;
            case R.id.easeCamera:
                mapboxMap.easeCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        28.704268, 77.103045), 14));
                break;
            case R.id.animateCamera:
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        28.698791, 77.121243), 14));
                break;
        }
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
