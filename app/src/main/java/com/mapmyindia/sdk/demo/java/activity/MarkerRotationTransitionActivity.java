package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.plugin.MarkerPlugin;

public class MarkerRotationTransitionActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private MapboxMap mapmyIndiaMap;
    private MapView mapView;
    private TextView rotateMarker, markerTransition;
    private LatLng latLngStart = new LatLng(28.705436, 77.100462);
    private LatLng latLngEnd = new LatLng(28.703800, 77.101818);
    private MarkerPlugin markerPlugin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_rotation_transition);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        initViews();
        initListener();
    }

    private void initListener() {
        rotateMarker.setOnClickListener(this);
        markerTransition.setOnClickListener(this);
    }

    private void initViews() {
        rotateMarker = findViewById(R.id.marker_rotate);
        markerTransition = findViewById(R.id.marker_transition);
    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMap) {

      this.mapmyIndiaMap = mapmyIndiaMap;
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(latLngStart)
                .include(latLngEnd)
                .build();

      mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100));

        initMarker();

    }

    private void initMarker() {
        markerPlugin = new MarkerPlugin(mapmyIndiaMap, mapView);
        markerPlugin.icon(getResources().getDrawable(R.drawable.placeholder));
        markerPlugin.addMarker(latLngStart);

    }

    @Override
    public void onMapError(int i, String s) {

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.marker_rotate :
                markerPlugin.startRotation();
                break;

            case R.id.marker_transition :
                markerPlugin.startTransition(latLngStart, latLngEnd);
                break;
        }

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
        if(markerPlugin!= null) {
            markerPlugin.removeCallbacks();
        }
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
