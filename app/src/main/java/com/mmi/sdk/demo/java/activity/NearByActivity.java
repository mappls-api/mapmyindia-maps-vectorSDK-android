package com.mmi.sdk.demo.java.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mmi.sdk.demo.R;
import com.mmi.sdk.demo.java.adapter.NearByAdapter;
import com.mmi.sdk.demo.java.utils.CheckInternet;
import com.mmi.sdk.demo.java.utils.TransparentProgressDialog;
import com.mmi.services.api.nearby.MapmyIndiaNearby;
import com.mmi.services.api.nearby.model.NearbyAtlasResponse;
import com.mmi.services.api.nearby.model.NearbyAtlasResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class NearByActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapboxMap mapboxMap;
    private MapView mapView;
    private TransparentProgressDialog transparentProgressDialog;
    private RecyclerView recyclerView;
    private int count = 0;
    private FloatingActionButton floatingActionButton;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_by_activity);
        mapView = findViewById(R.id.mapBoxId);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        Toast.makeText(this, "Please click on map to get nearby.", Toast.LENGTH_SHORT).show();
        recyclerView = findViewById(R.id.nearByRecyclerview);
        mLayoutManager = new LinearLayoutManager(NearByActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        floatingActionButton = findViewById(R.id.marker_list);
        floatingActionButton.setImageResource(R.drawable.location_pointer);
        count = 0;
        floatingActionButton.setOnClickListener(v -> {
            if (count == 0) {
                count = 1;
                floatingActionButton.setImageResource(R.drawable.listing_option);
                recyclerView.setVisibility(View.GONE);
            } else {
                count = 0;
                floatingActionButton.setImageResource(R.drawable.location_pointer);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        transparentProgressDialog = new TransparentProgressDialog(this, R.drawable.circle_loader, "");
    }

    @Override
    public void onMapReady(final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setMinZoomPreference(4.5);
        mapboxMap.setMaxZoomPreference(18.5);


        mapboxMap.setPadding(20, 20, 20, 20);


        mapboxMap.setCameraPosition(setCameraAndTilt());
        mapboxMap.addOnMapClickListener(latLng -> {
            mapboxMap.clear();
            if (CheckInternet.isNetworkAvailable(NearByActivity.this)) {
                getNearBy(latLng.getLatitude(), latLng.getLongitude());
            } else {
                Toast.makeText(NearByActivity.this, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapError(int i, String s) {

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

    private void getNearBy(double latitude, double longitude) {
        mapboxMap.clear();
        progressDialogShow();
        new MapmyIndiaNearby.Builder()
                .setLocation(latitude, longitude)
                .setKeyword("Tea")
                .build()
                .enqueueCall(new Callback<NearbyAtlasResponse>() {
                    @Override
                    public void onResponse(Call<NearbyAtlasResponse> call, Response<NearbyAtlasResponse> response) {

                        if (response.code() == 200) {
                            if (response.body() != null) {
                                ArrayList<NearbyAtlasResult> nearByList = response.body().getSuggestedLocations();
                                if (nearByList.size() > 0) {
                                    addMarker(nearByList);
                                }
                            } else {
                                Toast.makeText(NearByActivity.this, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(NearByActivity.this, response.message(), Toast.LENGTH_LONG).show();
                        }

                        progressDialogHide();
                    }

                    @Override
                    public void onFailure(Call<NearbyAtlasResponse> call, Throwable t) {
                        progressDialogHide();
                    }
                });
    }


    private void addMarker(ArrayList<NearbyAtlasResult> nearByList) {
        for (NearbyAtlasResult marker : nearByList) {
            mapboxMap.addMarker(new MarkerOptions().position(new LatLng(marker.getLatitude(), marker.getLongitude())).title(marker.getPlaceName()));
        }

        recyclerView.setAdapter(new NearByAdapter(nearByList));
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
