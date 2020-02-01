package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.plugin.DashedPolylinePlugin;
import com.mapmyindia.sdk.demo.java.utils.SemiCirclePointsListHelper;

import java.util.List;

public class SemiCirclePolylineActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private List<LatLng> listOfLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semi_circle_polyline);

        mapView = findViewById(R.id.map_view);
        mapView.getMapAsync(this);

        listOfLatLng = SemiCirclePointsListHelper.showCurvedPolyline(new LatLng(28.7039, 77.101318), new LatLng(28.704248, 77.102370), 0.5);
    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMap) {
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .includes(listOfLatLng)
                .build();

      mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100));

      DashedPolylinePlugin dashedPolylinePlugin = new DashedPolylinePlugin(mapmyIndiaMap, mapView);
        dashedPolylinePlugin.createPolyline(listOfLatLng);

//        PolylineOptions polylineOptions = new PolylineOptions()
//                .addAll(listOfLatLng)
//                .color(Color.parseColor("#3bb2d0"))
//                .width(4);
//
//        mapmyIndiaMap.addPolyline(polylineOptions);
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
