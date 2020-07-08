package com.mapmyindia.sdk.demo.java.activity;

import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;

public class LocationCameraActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, OnCameraTrackingChangedListener {

    MapView mapView;
    MapboxMap mapmyIndiaMap;
    private LocationEngine locationEngine;
    private Button btn_mode;
    private Button btn_tracking;
    private TextView tv_mode;
    private TextView tv_tracking;
    private LocationComponent locationComponent;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_camera_options);
        mapView = findViewById(R.id.add_mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        btn_mode = findViewById(R.id.btn_location_mode);
        btn_tracking = findViewById(R.id.btn_tracking);
        tv_mode = findViewById(R.id.text_view_mode);
        tv_tracking = findViewById(R.id.text_view_tracking);
        setButtonClickListener();
    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        enableLocation();
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
    protected void onResume() {
        super.onResume();
        if (locationEngine != null) {
            locationEngine.removeLocationEngineListener(this);
            locationEngine.addLocationEngineListener(this);
        }
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationEngine != null)
            locationEngine.removeLocationEngineListener(this);

        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationEngineListener(this);
            locationEngine.removeLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onConnected() {
        locationEngine.requestLocationUpdates();

    }

    @Override
    public void onLocationChanged(Location location) {
        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));

    }

    private void enableLocation() {
        LocationComponentOptions options = LocationComponentOptions.builder(LocationCameraActivity.this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .build();
// Get an instance of the component LocationComponent
        locationComponent = mapmyIndiaMap.getLocationComponent();
// Activate with options
        locationComponent.activateLocationComponent(LocationCameraActivity.this, options);
// Enable to make component visible
        locationComponent.setLocationComponentEnabled(true);
        locationEngine = locationComponent.getLocationEngine();
        if (locationEngine != null)
            locationEngine.addLocationEngineListener(this);

        locationComponent.addOnCameraTrackingChangedListener(this);
// Set the component's camera mode
        locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.setRenderMode(RenderMode.COMPASS);
    }

    private void setButtonClickListener() {
        btn_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(LocationCameraActivity.this, btn_mode);
                popupMenu.getMenuInflater().inflate(R.menu.location_mode_menu, popupMenu.getMenu());
                popupMenu.setGravity(Gravity.BOTTOM);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(LocationCameraActivity.this, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
                        btn_mode.setText(item.getTitle());
                        setRenderMode(item.getTitle().toString());
                        return true;
                    }
                });
                popupMenu.show();

            }
        });

        btn_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(LocationCameraActivity.this, btn_tracking);
                popupMenu.getMenuInflater().inflate(R.menu.tracking_mode_menu, popupMenu.getMenu());
                popupMenu.setGravity(Gravity.BOTTOM);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(LocationCameraActivity.this, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
                        // btn_tracking.setText(item.getTitle());
                        setCameraTrackingMode(item.getTitle().toString());
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void setRenderMode(String mode) {
        if (mode.equalsIgnoreCase("Normal")) {
            locationComponent.setRenderMode(RenderMode.NORMAL);
        } else if (mode.equalsIgnoreCase("Compass")) {
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else if (mode.equalsIgnoreCase("GPS")) {
            locationComponent.setRenderMode(RenderMode.GPS);
        } else
            locationComponent.setRenderMode(RenderMode.NORMAL);
    }

    private void setCameraTrackingMode(String mode) {
        if (mode.equalsIgnoreCase("None")) {
            locationComponent.setCameraMode(CameraMode.NONE);
        } else if (mode.equalsIgnoreCase("None compass")) {
            locationComponent.setCameraMode(CameraMode.NONE_COMPASS);
        } else if (mode.equalsIgnoreCase("None gps")) {
            locationComponent.setCameraMode(CameraMode.NONE_GPS);
        } else if (mode.equalsIgnoreCase("Tracking")) {
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else if (mode.equalsIgnoreCase("Tracking Compass")) {
            locationComponent.setCameraMode(CameraMode.TRACKING_COMPASS);
        } else if (mode.equalsIgnoreCase("Tracking GPS")) {
            locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
        } else if (mode.equalsIgnoreCase("Tracking GPS North")) {
            locationComponent.setCameraMode(CameraMode.TRACKING_GPS_NORTH);
        } else
            locationComponent.setCameraMode(CameraMode.TRACKING);
    }

    @Override
    public void onCameraTrackingChanged(int currentMode) {
        if (currentMode == CameraMode.NONE) {
            btn_tracking.setText("None");
        } else if (currentMode == CameraMode.TRACKING) {
            btn_tracking.setText("Tracking");
        } else if (currentMode == CameraMode.TRACKING_COMPASS) {
            btn_tracking.setText("Tracking Compass");
        } else if (currentMode == CameraMode.TRACKING_GPS) {
            btn_tracking.setText("Tracking GPS");
        } else if (currentMode == CameraMode.TRACKING_GPS_NORTH) {
            btn_tracking.setText("Tracking GPS North");
        }
    }


    @Override
    public void onCameraTrackingDismissed() {
        btn_tracking.setText("None");
    }
}
