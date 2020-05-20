package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.plugin.GradientPolylinePlugin;

import java.util.ArrayList;
import java.util.List;

public class GradientPolylineActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private List<LatLng> listOfLatLng = new ArrayList<>();
    Button btn_remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient_polyline);
        mapView = findViewById(R.id.map_view);
        btn_remove = findViewById(R.id.btn_remove);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        listOfLatLng.add(new LatLng(28.705436, 77.100462));
        listOfLatLng.add(new LatLng(28.705191, 77.100784));
        listOfLatLng.add(new LatLng(28.704646, 77.101514));
        listOfLatLng.add(new LatLng(28.704194, 77.101171));
        listOfLatLng.add(new LatLng(28.704083, 77.101066));
        listOfLatLng.add(new LatLng(28.703900, 77.101318));

    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMap) {

        mapmyIndiaMap.setPadding(20, 20, 20, 20);
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .includes(listOfLatLng)
                .build();
      mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));

      GradientPolylinePlugin gradientPolylinePlugin = new GradientPolylinePlugin(mapmyIndiaMap, mapView);
        gradientPolylinePlugin.createPolyline(listOfLatLng);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gradientPolylinePlugin.clear();
            }
        });
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
