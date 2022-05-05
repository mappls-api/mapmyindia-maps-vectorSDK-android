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
import com.mapmyindia.sdk.maps.location.engine.LocationEngine;
import com.mapmyindia.sdk.maps.location.engine.LocationEngineCallback;
import com.mapmyindia.sdk.maps.location.engine.LocationEngineProvider;
import com.mapmyindia.sdk.maps.location.engine.LocationEngineRequest;
import com.mapmyindia.sdk.maps.location.engine.LocationEngineResult;
import com.mapmyindia.sdk.maps.location.permissions.PermissionsListener;
import com.mapmyindia.sdk.maps.location.permissions.PermissionsManager;

import java.util.List;

public class SafetyStripActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationEngineCallback<LocationEngineResult> {

  @Override
  public void onMapError(int errorCode, String message) {

  }

  private MapView mapView;
  private Button btnShowStrip;
  private Button btnHideSafety;

  private PermissionsManager permissionsManager;

  private LocationComponent locationComponent;
    private LocationEngine locationEngine;
    private MapmyIndiaMap mapmyIndiaMap;



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
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;



      int[] padding;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            padding = new int[]{0, 750, 0, 0};
    } else {
      padding = new int[] {0, 250, 0, 0};
    }
      if (mapmyIndiaMap.getUiSettings() != null) {
        mapmyIndiaMap.getUiSettings().setLogoMargins(0, 0, 0, 120);
      }

      mapmyIndiaMap.getStyle(new Style.OnStyleLoaded() {
          @Override
          public void onStyleLoaded(@NonNull Style style) {

            LocationEngineRequest request = new LocationEngineRequest.Builder(100l)
                    .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                    .setMaxWaitTime(5000l).build();

            LocationComponentOptions options = LocationComponentOptions.builder(SafetyStripActivity.this)
                    .padding(padding)
                    .layerBelow("waterway-label")
                    .build();

            locationComponent = mapmyIndiaMap.getLocationComponent();
            locationEngine = LocationEngineProvider.getBestLocationEngine(SafetyStripActivity.this);

            LocationComponentActivationOptions locationComponentActivationOptions = LocationComponentActivationOptions.builder(SafetyStripActivity.this, style)
                    .locationComponentOptions(options)
                    .locationEngine(locationEngine)
                    .locationEngineRequest(request)
                    .build();
            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            locationComponent.setLocationComponentEnabled(true);
            Location location = locationComponent.getLastKnownLocation();
            if(location != null) {
              mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
            }
            locationEngine.requestLocationUpdates(request, SafetyStripActivity.this, getMainLooper());
            locationEngine.getLastLocation(SafetyStripActivity.this);
          }
        });


        findViewById(R.id.btn_layout).setVisibility(View.VISIBLE);
        mapmyIndiaMap.getUiSettings().setSafetyStripGravity(Gravity.TOP);
    }

  @Override
  @SuppressWarnings( {"MissingPermission"})
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
  public void onSuccess(LocationEngineResult locationEngineResult) {
    if(locationEngineResult.getLastLocation() != null) {
      Location location = locationEngineResult.getLastLocation();
      mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
    }
  }

  @Override
  public void onFailure(@NonNull Exception e) {
    e.printStackTrace();
  }
}