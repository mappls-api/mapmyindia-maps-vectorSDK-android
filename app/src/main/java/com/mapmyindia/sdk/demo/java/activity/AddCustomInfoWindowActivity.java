package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saksham on 2/12/19.
 */
public class AddCustomInfoWindowActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private List<LatLng> latLngList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        mapView = findViewById(R.id.mapBoxId);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        latLngList.add(new LatLng(25.321684, 82.987289));
        latLngList.add(new LatLng(25.331684, 82.997289));
        latLngList.add(new LatLng(25.321684, 82.887289));
        latLngList.add(new LatLng(25.311684, 82.987289));
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        mapboxMap.setMinZoomPreference(4.5);
        mapboxMap.setMaxZoomPreference(18.5);

        for (LatLng latLng : latLngList) {
            mapboxMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("XYZ"));
        }

        mapboxMap.setInfoWindowAdapter(new MapboxMap.InfoWindowAdapter() {
            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                View view = LayoutInflater.from(AddCustomInfoWindowActivity.this).inflate(R.layout.custom_info_window_layout, null);
                TextView textView = view.findViewById(R.id.text);
                textView.setText(marker.getTitle());
                return view;
            }
        });

        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                Toast.makeText(AddCustomInfoWindowActivity.this, marker.getPosition().toString(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .includes(latLngList)
                .build();

        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100, 10, 100, 10));
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
