package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.camera.CameraPosition;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class CameraActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, MapmyIndiaMap.OnCameraMoveListener, MapmyIndiaMap.OnCameraIdleListener, MapmyIndiaMap.OnCameraMoveCanceledListener {

    private MapmyIndiaMap mapmyIndiaMap;
    private MapView mapView;
    private TextView moveCamera, easeCamera, animateCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        mapView = findViewById(R.id.map_view);
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
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;


        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                25.321684, 82.987289)).zoom(14).tilt(0).build();
        mapmyIndiaMap.setCameraPosition(cameraPosition);

        //Map related listeners
        mapmyIndiaMap.addOnCameraMoveListener(this);
        mapmyIndiaMap.addOnCameraIdleListener(this);
        mapmyIndiaMap.addOnCameraMoveCancelListener(this);
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
                mapmyIndiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        22.553147478403194,
                        77.23388671875), 14));

                break;
            case R.id.easeCamera:
                mapmyIndiaMap.easeCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        28.704268, 77.103045), 14));
                break;
            case R.id.animateCamera:
                mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
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
