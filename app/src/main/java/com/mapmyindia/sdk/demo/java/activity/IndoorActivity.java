package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.annotations.Icon;
import com.mapmyindia.sdk.maps.annotations.IconFactory;
import com.mapmyindia.sdk.maps.annotations.MarkerOptions;
import com.mapmyindia.sdk.maps.camera.CameraPosition;
import com.mapmyindia.sdk.maps.geometry.LatLng;


/**
 * Created by CEINFO on 26-02-2019.
 */

public class IndoorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapmyIndiaMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indoor_layout);
        mapView = findViewById(R.id.map_view);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


    }

    @Override
    public void onMapReady(MapmyIndiaMap map) {
        this.map = map;
        //To turn on layer control
        map.getUiSettings().setLayerControlEnabled(true);
        map.setMinZoomPreference(4.5);
        map.setMaxZoomPreference(18.5);
        map.setPadding(20, 20, 20, 20);
        IconFactory iconFactory = IconFactory.getInstance(this);
        Icon icon = iconFactory.fromResource(R.drawable.placeholder);
        map.addMarker(new MarkerOptions().position(new LatLng(
                28.5425071, 77.1560724)).icon(icon));
        /* this is done for animating/moving camera to particular position */
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                28.5425071, 77.1560724)).zoom(16).tilt(0).build();
        map.setCameraPosition(cameraPosition);


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