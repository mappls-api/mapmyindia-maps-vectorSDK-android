package com.mapmyindia.sdk.demo.java.activity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;

import java.util.List;

public class SafetyStripActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationEngineListener {

  @Override
  public void onMapError(int errorCode, String message) {

  }

  private MapView mapView;
  private Button btnShowStrip;
  private Button btnHideSafety;

  private PermissionsManager permissionsManager;

  private LocationComponent locationComponent;
    private LocationEngine locationEngine;
    private MapboxMap mapmyIndiaMap;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_safety_strip);

    mapView = findViewById(R.id.map_view);

    btnShowStrip = findViewById(R.id.btn_show_strip);
      btnHideSafety = findViewById(R.id.btn_hide_strip);
      btnHideSafety.setEnabled(false);
    btnShowStrip.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          btnShowStrip.setEnabled(false);
          btnHideSafety.setEnabled(true);
          mapmyIndiaMap.showCurrentLocationSafety();
      }
    });

    btnHideSafety.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          btnShowStrip.setEnabled(true);
          btnHideSafety.setEnabled(false);
        mapmyIndiaMap.getUiSettings().hideSafetyStrip();
      }
    });


    mapView.onCreate(savedInstanceState);

    if (PermissionsManager.areLocationPermissionsGranted(this)) {
      mapView.getMapAsync(this);
    } else {
      permissionsManager = new PermissionsManager(new PermissionsListener() {
        @Override
        public void onExplanationNeeded(List<String> permissionsToExplain) {
          Toast.makeText(SafetyStripActivity.this, "You need to accept location permissions.",
            Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionResult(boolean granted) {
          if (granted) {
            mapView.getMapAsync(SafetyStripActivity.this);
          } else {
            finish();
          }
        }
      });
      permissionsManager.requestLocationPermissions(this);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(MapboxMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;

        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.setFastestInterval(1000);
        locationEngine.addLocationEngineListener(this);
        locationEngine.activate();

        int[] padding;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            padding = new int[]{0, 750, 0, 0};
    } else {
      padding = new int[] {0, 250, 0, 0};
    }

    LocationComponentOptions options = LocationComponentOptions.builder(this)
      .padding(padding)
      .layerBelow("waterway-label")
            .build();

        locationComponent = mapmyIndiaMap.getLocationComponent();
        locationComponent.activateLocationComponent(this, locationEngine, options);
        locationComponent.setLocationComponentEnabled(true);
        Location location = locationComponent.getLastKnownLocation();
        if(location != null) {
          mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
        }

        findViewById(R.id.btn_layout).setVisibility(View.VISIBLE);
        mapmyIndiaMap.getUiSettings().setSafetyStripGravity(Gravity.TOP);
    }

  @Override
  @SuppressWarnings( {"MissingPermission"})
  protected void onStart() {
    super.onStart();
    mapView.onStart();
    if (locationEngine != null) {
      locationEngine.addLocationEngineListener(this);
      if (locationEngine.isConnected()) {
        locationEngine.requestLocationUpdates();
      } else {
        locationEngine.activate();
      }
    }
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
    if (locationEngine != null) {
      locationEngine.removeLocationEngineListener(this);
      locationEngine.removeLocationUpdates();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
    if (locationEngine != null) {
      locationEngine.deactivate();
    }
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  @SuppressWarnings( {"MissingPermission"})
  public void onConnected() {
    locationEngine.requestLocationUpdates();
  }

  @Override
  public void onLocationChanged(Location location) {
    // no impl
      mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
  }

}