package com.mmi.sdk.demo.java.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mmi.sdk.demo.R;
import com.mmi.sdk.demo.java.plugin.GradientPolylinePlugin;

import java.util.ArrayList;
import java.util.List;

public class GradientPolylineActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapboxMap mMapboxMap;
    private MapView mapView;
    private List<LatLng> listOfLatLng = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient_polyline);
        mapView = findViewById(R.id.mapBoxId);
        mapView.getMapAsync(this);

        listOfLatLng.add(new LatLng(28.705436, 77.100462));
        listOfLatLng.add(new LatLng(28.705191, 77.100784));
        listOfLatLng.add(new LatLng(28.704646, 77.101514));
        listOfLatLng.add(new LatLng(28.704194, 77.101171));
        listOfLatLng.add(new LatLng(28.704083, 77.101066));
        listOfLatLng.add(new LatLng(28.703900, 77.101318));

    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {

        mapboxMap.setPadding(20, 20, 20, 20);
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .includes(listOfLatLng)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));

        GradientPolylinePlugin animatedPolylinePlugin = new GradientPolylinePlugin(mapboxMap, mapView);
        animatedPolylinePlugin.createPolyline(listOfLatLng);
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
