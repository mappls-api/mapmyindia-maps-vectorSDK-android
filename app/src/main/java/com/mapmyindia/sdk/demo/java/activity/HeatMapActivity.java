package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.plugin.HeatMapPlugin;

import java.util.ArrayList;
import java.util.List;

public class HeatMapActivity extends AppCompatActivity implements OnMapReadyCallback {


    private MapView mapView;
    private List<HeatMapPlugin.HeatMapOption> heatMapOptionList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(77.5129, 28.1016), 2.3));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(77.5132, 28.1021), 2.0));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(76.4048, 28.1224), 1.7));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(77.3597, 28.0781), 1.6));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(77.3597, 28.0781), 1.6));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(74.789, 28.1725), 2.4));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat( 77.512, 28.0879), 1.8));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(74.2832, 28.674242), 1.3));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(80.8244288, 24.6778728), 2.4));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(81.637417, 24.6778728), 3.2));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(77.7372706, 27.8302397), 1.2));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(79.2973292, 28.3729432), 4.2));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(79.7477686, 26.2351935), 2.13));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(80.1762354, 26.1760509), 3.2));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(79.7367823, 27.333618), 1.2));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(80.9452784, 26.628706), 5.2));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(78.6930811, 28.9032672), 3.2));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(78.73702641, 29.3255866), 4.0));
        heatMapOptionList.add(new HeatMapPlugin.HeatMapOption(Point.fromLngLat(79.4181788, 29.5838759), 5.0));
    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMap) {

        HeatMapPlugin heatMapPlugin = HeatMapPlugin.builder(mapmyIndiaMap, mapView)
                .addAll(heatMapOptionList)
                .build();
        heatMapPlugin.addHeatMap();
        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28, 77), 4));

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
