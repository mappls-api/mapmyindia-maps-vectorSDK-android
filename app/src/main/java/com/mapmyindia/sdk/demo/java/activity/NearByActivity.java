package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.adapter.NearByAdapter;
import com.mapmyindia.sdk.demo.java.utils.CheckInternet;
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog;
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

    private MapboxMap mapmyIndiaMap;
    private MapView mapView;
    private TransparentProgressDialog transparentProgressDialog;
    private RecyclerView recyclerView;
    private int count = 0;
    private FloatingActionButton floatingActionButton;
    private LinearLayoutManager mLayoutManager;
    private EditText keywordEt, locationEt;

    private Button hitAPiBtn;
    private SeekBar radiusSeekbar;
    int radius = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_by_activity);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        Toast.makeText(this, "Please click on map to get nearby.", Toast.LENGTH_SHORT).show();
        recyclerView = findViewById(R.id.nearByRecyclerview);
        mLayoutManager = new LinearLayoutManager(NearByActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        keywordEt=findViewById(R.id.keyword_et);
        hitAPiBtn=findViewById(R.id.hit_api_btn);
        locationEt= findViewById(R.id.location_et);
        radiusSeekbar = findViewById(R.id.seekBar);
       // radiusSeekbar.setMin(500);
        radiusSeekbar.setMax(10000);
        radiusSeekbar.setProgress(1000);
        radiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress<500){
                    radiusSeekbar.setProgress(500);
                }else {
                    radius=progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        hitAPiBtn.setOnClickListener(v->{
            String location = locationEt.getText().toString();
            if (!TextUtils.isEmpty(location)){
                getNearBy(locationEt.getText().toString());
            }
        });

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
    public void onMapReady(final MapboxMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;


        mapmyIndiaMap.setPadding(20, 20, 20, 20);

       mapmyIndiaMap.setCameraPosition(setCameraAndTilt(28.67,77.56));
        mapmyIndiaMap.addOnMapClickListener(latLng -> {
            mapmyIndiaMap.clear();
            locationEt.setText(latLng.getLatitude()+","+latLng.getLongitude());
            if (CheckInternet.isNetworkAvailable(NearByActivity.this)) {
                getNearBy(latLng.getLatitude()+","+latLng.getLongitude());
            } else {
                Toast.makeText(NearByActivity.this, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapError(int i, String s) {

    }

    protected CameraPosition setCameraAndTilt(double lat , double lng) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                lat, lng)).zoom(11).tilt(0).build();
        return cameraPosition;
    }

    protected void progressDialogShow() {
        transparentProgressDialog.show();
    }

    protected void progressDialogHide() {
        transparentProgressDialog.dismiss();
    }

    private void getNearBy(String location) {
        mapmyIndiaMap.clear();
        progressDialogShow();
       MapmyIndiaNearby.Builder builder =  MapmyIndiaNearby.builder();


                if (!TextUtils.isEmpty(location)){
                    if (!location.contains(",")){
                        mapmyIndiaMap.moveCamera(location,11);
                        builder.setLocation(location);
                    } else {
                        mapmyIndiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(location.split(",")[0]),Double.parseDouble(location.split(",")[1])),11));
                        builder.setLocation(Double.parseDouble(location.split(",")[0]),Double.parseDouble(location.split(",")[1]));
                    }
                }

                builder.keyword(keywordEt.getText().toString())
                .page(2);

                builder.radius(radius)
                .build()
                .enqueueCall(new Callback<NearbyAtlasResponse>() {
                    @Override
                    public void onResponse(Call<NearbyAtlasResponse> call, Response<NearbyAtlasResponse> response) {

                        if (response.code() == 200) {
                            if (response.body() != null) {
                                Log.e("NEARBY", new Gson().toJson(response.body()));
                                ArrayList<NearbyAtlasResult> nearByList = response.body().getSuggestedLocations();
                                if (nearByList.size() > 0) {
                                    addMarker(nearByList);
                                }
                            } else {
                                Toast.makeText(NearByActivity.this, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("NEARBY", new Gson().toJson(response.body()));
                            Toast.makeText(NearByActivity.this, response.code() + "", Toast.LENGTH_LONG).show();
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
//            if (marker.getLatitude() == null || marker.getLongitude() == null) {
//                mapmyIndiaMap.addMarker(new MarkerOptions().eLoc(marker.eLoc).title(marker.getPlaceName()));
//            } else {
                mapmyIndiaMap.addMarker(new MarkerOptions().position(new LatLng(marker.getLatitude(), marker.getLongitude())).title(marker.getPlaceName()));
//            }
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
