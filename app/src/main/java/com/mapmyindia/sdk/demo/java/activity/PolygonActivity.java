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
import com.mapmyindia.sdk.maps.annotations.Polygon;
import com.mapmyindia.sdk.maps.annotations.PolygonOptions;
import com.mapmyindia.sdk.maps.camera.CameraPosition;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mapmyindia.sdk.maps.geometry.LatLngBounds;

import java.util.ArrayList;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class PolygonActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<LatLng> listOfLatlang = new ArrayList<>();
    private MapView mapView;
    Button addPolygonButton;
    Button removePolygonButton;
    private Polygon polygon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon);
        mapView = findViewById(R.id.map_view);
        addPolygonButton = findViewById(R.id.btn_add_polygon);
        removePolygonButton = findViewById(R.id.btn_remove_polygon);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {

        mapmyIndiaMap.setCameraPosition(setCameraAndTilt());
        mapmyIndiaMap.setPadding(20, 20, 20, 20);
        listOfLatlang.add(new LatLng(28.703900, 77.101318));
        listOfLatlang.add(new LatLng(28.703331, 77.102155));
        listOfLatlang.add(new LatLng(28.703905, 77.102761));
        listOfLatlang.add(new LatLng(28.704248, 77.102370));

        /* this is done for move camera focus to particular position */
        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(listOfLatlang).build();
        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 70));

        addPolygonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                polygon = mapmyIndiaMap.addPolygon(new PolygonOptions().addAll(listOfLatlang).fillColor(Color.parseColor("#753bb2d0")));

                addPolygonButton.setVisibility(View.GONE);
                removePolygonButton.setVisibility(View.VISIBLE);

            }
        });
        removePolygonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (polygon != null) {
                    mapmyIndiaMap.removePolygon(polygon);
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
