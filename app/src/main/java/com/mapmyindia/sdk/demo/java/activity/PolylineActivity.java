package com.mapmyindia.sdk.demo.java.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.annotations.Polyline;
import com.mapmyindia.sdk.maps.annotations.PolylineOptions;
import com.mapmyindia.sdk.maps.camera.CameraPosition;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mapmyindia.sdk.maps.geometry.LatLngBounds;

import java.util.ArrayList;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class PolylineActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<LatLng> listOfLatlang = new ArrayList<>();
    private MapView mapView;
    Button addPolygonButton;
    Button removePolygonButton;
    private Polyline polyline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polyline);
        mapView = findViewById(R.id.map_view);
        addPolygonButton = findViewById(R.id.btn_add_polyline);
        removePolygonButton = findViewById(R.id.btn_remove_polyline);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {

        mapmyIndiaMap.setCameraPosition(setCameraAndTilt());
        listOfLatlang.add(new LatLng(28.705436, 77.100462));
        listOfLatlang.add(new LatLng(28.705191, 77.100784));
        listOfLatlang.add(new LatLng(28.704646, 77.101514));
        listOfLatlang.add(new LatLng(28.704194, 77.101171));
        listOfLatlang.add(new LatLng(28.704083, 77.101066));
        listOfLatlang.add(new LatLng(28.703900, 77.101318));

        /* this is done for animating/moving camera to particular position */

        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(listOfLatlang).build();
        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 70));
        addPolygonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                polyline = mapmyIndiaMap.addPolyline(new PolylineOptions().addAll(listOfLatlang).color(Color.parseColor("#3bb2d0")).width(4));


                addPolygonButton.setVisibility(View.GONE);
                removePolygonButton.setVisibility(View.VISIBLE);

            }
        });
        removePolygonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (polyline != null) {
                    mapmyIndiaMap.removePolyline(polyline);
                    addPolygonButton.setVisibility(View.VISIBLE);
                    removePolygonButton.setVisibility(View.GONE);
                }
            }
        });
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
