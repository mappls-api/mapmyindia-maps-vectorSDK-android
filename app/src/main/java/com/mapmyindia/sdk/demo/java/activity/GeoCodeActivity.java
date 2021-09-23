package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.utils.CheckInternet;
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.annotations.MarkerOptions;
import com.mapmyindia.sdk.maps.camera.CameraPosition;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mmi.services.api.OnResponseCallback;
import com.mmi.services.api.geocoding.GeoCode;
import com.mmi.services.api.geocoding.GeoCodeResponse;
import com.mmi.services.api.geocoding.MapmyIndiaGeoCoding;
import com.mmi.services.api.geocoding.MapmyIndiaGeoCodingManager;

import java.util.List;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class GeoCodeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapmyIndiaMap mapmyIndiaMap;
    private MapView mapView;
    private TransparentProgressDialog transparentProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        transparentProgressDialog = new TransparentProgressDialog(this, R.drawable.circle_loader, "");
    }

    @Override
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;


        mapmyIndiaMap.setCameraPosition(setCameraAndTilt());
        if (CheckInternet.isNetworkAvailable(GeoCodeActivity.this)) {
            getGeoCode("saket");
        } else {
            Toast.makeText(this, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show();
        }
    }

    protected CameraPosition setCameraAndTilt() {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                28.551087, 77.257373)).zoom(14).tilt(0).build();
        return cameraPosition;
    }


    protected void progressDialogShow() {
        transparentProgressDialog.show();
    }

    protected void progressDialogHide() {
        transparentProgressDialog.dismiss();
    }


    private void getGeoCode(String geocodeText) {
        progressDialogShow();
        MapmyIndiaGeoCoding mapmyIndiaGeoCoding = MapmyIndiaGeoCoding.builder()
                .setAddress(geocodeText)
                .build();
        MapmyIndiaGeoCodingManager.newInstance(mapmyIndiaGeoCoding).call(new OnResponseCallback<GeoCodeResponse>() {
            @Override
            public void onSuccess(GeoCodeResponse geoCodeResponse) {

                if (geoCodeResponse != null) {
                    List<GeoCode> placesList = geoCodeResponse.getResults();
                    GeoCode place = placesList.get(0);
                    String add = "Latitude: " + place.latitude + " longitude: " + place.longitude;
                    addMarker(place.latitude, place.longitude);
                    Toast.makeText(GeoCodeActivity.this, add, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GeoCodeActivity.this, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show();
                }
                progressDialogHide();
            }

            @Override
            public void onError(int i, String s) {
                progressDialogHide();
                Toast.makeText(GeoCodeActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMarker(double latitude, double longitude) {
        mapmyIndiaMap.addMarker(new MarkerOptions().position(new LatLng(
                latitude, longitude)));
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
