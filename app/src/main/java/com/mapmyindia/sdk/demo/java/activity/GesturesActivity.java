package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.android.gestures.AndroidGesturesManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;

public class GesturesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapmyIndiaMap;
    private AndroidGesturesManager androidGesturesManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        androidGesturesManager = new AndroidGesturesManager(mapView.getContext(), false);
        addTouchGesture();
    }

    private void addTouchGesture() {

        if(androidGesturesManager != null) {


            mapView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    androidGesturesManager.onTouchEvent(event);
                    return false;
                }


            });
            androidGesturesManager.setMoveGestureListener(new MoveGestureDetector.OnMoveGestureListener() {
                @Override
                public boolean onMoveBegin(MoveGestureDetector detector) {

                    if (mapmyIndiaMap != null && detector.getPointersCount() == 1) {
                        LatLng latLng = mapmyIndiaMap.getProjection().fromScreenLocation(detector.getFocalPoint());
                        Toast.makeText(GesturesActivity.this, "onMoveBegin: " + latLng.toString(), Toast.LENGTH_SHORT).show();

                    }

                    return true;
                }

                @Override
                public boolean onMove(MoveGestureDetector detector, float distanceX, float distanceY) {
                    if (mapmyIndiaMap != null && detector.getPointersCount() == 1) {
                        LatLng latLng = mapmyIndiaMap.getProjection().fromScreenLocation(detector.getFocalPoint());
                        Toast.makeText(GesturesActivity.this, "onMove: " + latLng.toString(), Toast.LENGTH_SHORT).show();


                    }
                    return false;
                }

                @Override
                public void onMoveEnd(MoveGestureDetector detector, float velocityX, float velocityY) {
                    if (mapmyIndiaMap != null) {
                        LatLng latLng = mapmyIndiaMap.getProjection().fromScreenLocation(detector.getFocalPoint());
                        Toast.makeText(GesturesActivity.this, "onMoveEnd: " + latLng.toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;

        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28, 77), 14));


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
