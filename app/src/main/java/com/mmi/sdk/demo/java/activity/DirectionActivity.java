package com.mmi.sdk.demo.java.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.PolylineUtils;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mmi.sdk.demo.R;
import com.mmi.sdk.demo.java.utils.CheckInternet;
import com.mmi.sdk.demo.java.utils.TransparentProgressDialog;
import com.mmi.services.api.directions.legacy.MapmyIndiaDirectionsLegacy;
import com.mmi.services.api.directions.legacy.model.LegacyRouteResponse;
import com.mmi.services.api.directions.legacy.model.Results;
import com.mmi.services.api.directions.legacy.model.Trip;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by CEINFO on 26-02-2019.
 */

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapboxMap mapboxMap;
    private MapView mapView;
    private TransparentProgressDialog transparentProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        mapView = findViewById(R.id.mapBoxId);
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
        if (CheckInternet.isNetworkAvailable(DirectionActivity.this)) {
            getDirections();
        } else {
            Toast.makeText(this, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show();
        }
    }

    protected CameraPosition setCameraAndTilt() {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                28.551087, 77.257373)).zoom(14).tilt(0).build();
        return cameraPosition;
    }


    private void progressDialogShow() {
        transparentProgressDialog.show();
    }

    private void progressDialogHide() {
        transparentProgressDialog.dismiss();
    }

    private void getDirections() {
        progressDialogShow();

        new MapmyIndiaDirectionsLegacy.Builder()
                .setOrigin(Point.fromLngLat(77.202432, 28.594475))
                .setDestination(Point.fromLngLat(77.186982, 28.554676))
                .build().enqueueCall(new Callback<LegacyRouteResponse>() {
            @Override
            public void onResponse(Call<LegacyRouteResponse> call, Response<LegacyRouteResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        LegacyRouteResponse directionsResponse = response.body();
                        Results results = directionsResponse.getResults();
                        List<Trip> tripList = results.getTrips();
                        if (tripList.size() > 0) {
                            drawPath(tripList);
                        }
                    }
                } else {
                    Toast.makeText(DirectionActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
                progressDialogHide();
            }

            @Override
            public void onFailure(Call<LegacyRouteResponse> call, Throwable t) {
                progressDialogHide();
            }
        });

    }

    private void drawPath(List<Trip> waypoints) {
        ArrayList<LatLng> listOfLatlang = new ArrayList<>();
        List<Point> pointList = PolylineUtils.decode(waypoints.get(0).getPts(), 6);
        for (Point point : pointList) {
            listOfLatlang.add(new LatLng(point.latitude(), point.longitude()));
        }

        mapboxMap.addPolyline(new PolylineOptions().addAll(listOfLatlang).color(Color.parseColor("#3bb2d0")).width(4));
        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(listOfLatlang).build();
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 30));
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
