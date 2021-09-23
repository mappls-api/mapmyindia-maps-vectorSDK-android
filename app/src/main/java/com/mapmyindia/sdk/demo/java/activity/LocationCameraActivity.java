package com.mapmyindia.sdk.demo.java.activity;

import android.Manifest;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.Style;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mapmyindia.sdk.maps.location.LocationComponent;
import com.mapmyindia.sdk.maps.location.LocationComponentActivationOptions;
import com.mapmyindia.sdk.maps.location.LocationComponentOptions;
import com.mapmyindia.sdk.maps.location.OnCameraTrackingChangedListener;
import com.mapmyindia.sdk.maps.location.engine.LocationEngine;
import com.mapmyindia.sdk.maps.location.engine.LocationEngineCallback;
import com.mapmyindia.sdk.maps.location.engine.LocationEngineRequest;
import com.mapmyindia.sdk.maps.location.engine.LocationEngineResult;
import com.mapmyindia.sdk.maps.location.modes.CameraMode;
import com.mapmyindia.sdk.maps.location.modes.RenderMode;

public class LocationCameraActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineCallback<LocationEngineResult>, OnCameraTrackingChangedListener {

    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000l;
    MapView mapView;
    MapmyIndiaMap mapmyIndiaMap;
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
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        mapmyIndiaMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocation(style);
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
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Prevent leaks
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(this);
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


    private void enableLocation(Style style) {
        LocationComponentOptions options = LocationComponentOptions.builder(LocationCameraActivity.this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .build();
        // Get an instance of the component LocationComponent
        locationComponent = mapmyIndiaMap.getLocationComponent();
        LocationComponentActivationOptions locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, style)
                .locationComponentOptions(options)
                .build();
// Activate with options
        locationComponent.activateLocationComponent(locationComponentActivationOptions);
// Enable to make component visiblelocationEngine
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationComponent.setLocationComponentEnabled(true);
        locationEngine = locationComponent.getLocationEngine();
        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .build();
        locationEngine.requestLocationUpdates(request, this, getMainLooper());
        locationEngine.getLastLocation(this);
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

    @Override
    public void onSuccess(LocationEngineResult locationEngineResult) {
        if(locationEngineResult.getLastLocation() != null) {
            Location location = locationEngineResult.getLastLocation();
            mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        e.printStackTrace();
    }
}
