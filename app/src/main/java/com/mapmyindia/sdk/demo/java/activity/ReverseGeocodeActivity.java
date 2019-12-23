package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.mmi.services.api.Place;
import com.mmi.services.api.PlaceResponse;
import com.mmi.services.api.reversegeocode.MapmyIndiaReverseGeoCode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class ReverseGeocodeActivity extends AppCompatActivity implements OnMapReadyCallback {

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
    public void onMapReady(final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setMinZoomPreference(4.5);
        mapboxMap.setMaxZoomPreference(18.5);


        mapboxMap.setPadding(20, 20, 20, 20);

        mapboxMap.setCameraPosition(setCameraAndTilt());
        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mapboxMap.clear();
                if (CheckInternet.isNetworkAvailable(ReverseGeocodeActivity.this)) {
                    reverseGeocode(latLng.getLatitude(), latLng.getLongitude());
                    addMarker(latLng.getLatitude(), latLng.getLongitude());
                } else {
                    Toast.makeText(ReverseGeocodeActivity.this, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show();
                }

            }
        });
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

    private void reverseGeocode(Double latitude, Double longitude) {
        progressDialogShow();
        new MapmyIndiaReverseGeoCode.Builder()
                .setLocation(latitude, longitude)
                .build().enqueueCall(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        List<Place> placesList = response.body().getPlaces();
                        Place place = placesList.get(0);
                        String add = place.getFormattedAddress();
                        Toast.makeText(ReverseGeocodeActivity.this, add, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ReverseGeocodeActivity.this, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ReverseGeocodeActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }

                progressDialogHide();

            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                progressDialogHide();
                Toast.makeText(ReverseGeocodeActivity.this, t.toString(), Toast.LENGTH_LONG).show();
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
