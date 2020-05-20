package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.plugin.MarkerPlugin;

public class MarkerDraggingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapmyIndiaMap;
    private LatLng latLng = new LatLng(28.705436, 77.100462);
    private MarkerPlugin markerPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMap) {
      this.mapmyIndiaMap = mapmyIndiaMap;
      mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        initMarker();
    }

    private void initMarker() {
        markerPlugin = new MarkerPlugin(mapmyIndiaMap, mapView);
        markerPlugin.icon(getResources().getDrawable(R.drawable.placeholder));
        markerPlugin.addMarker(latLng);
        markerPlugin.draggable(true);
        markerPlugin.setOnMarkerDraggingListener(new MarkerPlugin.OnMarkerDraggingListener() {
            @Override
            public void onMarkerDragging(LatLng position) {
                Toast.makeText(MarkerDraggingActivity.this, position.toString(), Toast.LENGTH_SHORT).show();
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
