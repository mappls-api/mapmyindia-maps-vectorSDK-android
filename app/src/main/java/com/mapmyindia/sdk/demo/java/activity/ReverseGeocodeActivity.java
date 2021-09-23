package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.mmi.services.api.Place;
import com.mmi.services.api.PlaceResponse;
import com.mmi.services.api.reversegeocode.MapmyIndiaReverseGeoCode;
import com.mmi.services.api.reversegeocode.MapmyIndiaReverseGeoCodeManager;

import java.util.List;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class ReverseGeocodeActivity extends AppCompatActivity implements OnMapReadyCallback {

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
    public void onMapReady(final MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;

        mapmyIndiaMap.setCameraPosition(setCameraAndTilt());
        mapmyIndiaMap.addOnMapClickListener(new MapmyIndiaMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull LatLng latLng) {
                mapmyIndiaMap.clear();
                if (CheckInternet.isNetworkAvailable(ReverseGeocodeActivity.this)) {
                    reverseGeocode(latLng.getLatitude(), latLng.getLongitude());
                    addMarker(latLng.getLatitude(), latLng.getLongitude());
                } else {
                    Toast.makeText(ReverseGeocodeActivity.this, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show();
                }
                return false;
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
        MapmyIndiaReverseGeoCode mapmyIndiaReverseGeoCode = MapmyIndiaReverseGeoCode.builder()
                .setLocation(latitude, longitude)
                .build();
        MapmyIndiaReverseGeoCodeManager.newInstance(mapmyIndiaReverseGeoCode).call(new OnResponseCallback<PlaceResponse>() {
            @Override
            public void onSuccess(PlaceResponse placeResponse) {
                if (placeResponse != null) {
                    List<Place> placesList = placeResponse.getPlaces();
                    Place place = placesList.get(0);
                    String add = place.getFormattedAddress();
                    Toast.makeText(ReverseGeocodeActivity.this, add, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ReverseGeocodeActivity.this, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(ReverseGeocodeActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
//        .enqueueCall(new Callback<PlaceResponse>() {
//            @Override
//            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
//                if (response.code() == 200) {
//                    if (response.body() != null) {
//                        List<Place> placesList = response.body().getPlaces();
//                        Place place = placesList.get(0);
//                        String add = place.getFormattedAddress();
//                        Toast.makeText(ReverseGeocodeActivity.this, add, Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(ReverseGeocodeActivity.this, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(ReverseGeocodeActivity.this, response.message(), Toast.LENGTH_LONG).show();
//                }
//
//                progressDialogHide();
//
//            }
//
//            @Override
//            public void onFailure(Call<PlaceResponse> call, Throwable t) {
//                progressDialogHide();
//                Toast.makeText(ReverseGeocodeActivity.this, t.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
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
