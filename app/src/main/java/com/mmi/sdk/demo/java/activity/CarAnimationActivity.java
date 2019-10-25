package com.mmi.sdk.demo.java.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mmi.sdk.demo.R;
import com.mmi.sdk.demo.java.plugin.AnimatedCarPlugin;

import java.util.ArrayList;
import java.util.List;

public class CarAnimationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private List<LatLng> listOfLatlang = new ArrayList<>();
    int index = 0;
    private AnimatedCarPlugin animatedCarPlugin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_animation);

        mapView = findViewById(R.id.mapBoxId);
        mapView.getMapAsync(this);

        listOfLatlang.add(new LatLng(28.705436, 77.100462));
        listOfLatlang.add(new LatLng(28.705191, 77.100784));
        listOfLatlang.add(new LatLng(28.704646, 77.101514));
        listOfLatlang.add(new LatLng(28.704194, 77.101171));
        listOfLatlang.add(new LatLng(28.704083, 77.101066));
        listOfLatlang.add(new LatLng(28.703900, 77.101318));
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .includes(listOfLatlang)
                .build();

//        this.mapboxMap = mapboxMap;
        animatedCarPlugin = new AnimatedCarPlugin(getApplicationContext(), mapView, mapboxMap);
        mapboxMap.addPolyline(new PolylineOptions().addAll(listOfLatlang).color(Color.parseColor("#3bb2d0")).width(4));
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100));
        animatedCarPlugin.addMainCar(listOfLatlang.get(index), true);
        animatedCarPlugin.animateCar();

        animatedCarPlugin.setOnUpdateNextPoint(new AnimatedCarPlugin.OnUpdatePoint() {
            @Override
            public void updateNextPoint() {
                if (index < listOfLatlang.size() - 1)
                    index = index + 1;

                animatedCarPlugin.updateNextPoint(listOfLatlang.get(index));
                animatedCarPlugin.animateCar();
            }
        });

//        mapboxMap.addMarker(new MarkerOptions().position(listOfLatlang.get(listOfLatlang.size() - 1)).title("Destination"));

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
        if (animatedCarPlugin != null)
            animatedCarPlugin.clearAllCallBacks();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animatedCarPlugin != null)
            animatedCarPlugin.addAllCallBacks();
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
