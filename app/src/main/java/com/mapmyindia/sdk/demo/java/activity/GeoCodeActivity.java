package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.utils.CheckInternet;
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog;
import com.mmi.services.api.geocoding.GeoCode;
import com.mmi.services.api.geocoding.GeoCodeResponse;
import com.mmi.services.api.geocoding.MapmyIndiaGeoCoding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class GeoCodeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapboxMap mapboxMap;
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
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setMinZoomPreference(4.5);
        mapboxMap.setMaxZoomPreference(18.5);


        mapboxMap.setPadding(20, 20, 20, 20);


        mapboxMap.setCameraPosition(setCameraAndTilt());
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
        new MapmyIndiaGeoCoding.Builder()
                .setAddress(geocodeText)
                .build().enqueueCall(new Callback<GeoCodeResponse>() {
            @Override
            public void onResponse(Call<GeoCodeResponse> call, Response<GeoCodeResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        List<GeoCode> placesList = response.body().getResults();
                        GeoCode place = placesList.get(0);
                        String add = "Latitude: " + place.latitude + " longitude: " + place.longitude;
                        addMarker(place.latitude, place.longitude);
                        Toast.makeText(GeoCodeActivity.this, add, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GeoCodeActivity.this, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GeoCodeActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
                progressDialogHide();
            }

            @Override
            public void onFailure(Call<GeoCodeResponse> call, Throwable t) {
                Toast.makeText(GeoCodeActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                progressDialogHide();
            }
        });
    }

    private void addMarker(double latitude, double longitude) {
        mapboxMap.addMarker(new MarkerOptions().position(new LatLng(
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
