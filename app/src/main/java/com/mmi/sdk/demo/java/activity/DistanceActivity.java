package com.mmi.sdk.demo.java.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mmi.sdk.demo.R;
import com.mmi.sdk.demo.java.utils.CheckInternet;
import com.mmi.sdk.demo.java.utils.TransparentProgressDialog;
import com.mmi.services.api.distance.legacy.MapmyIndiaDistanceLegacy;
import com.mmi.services.api.distance.legacy.model.Distance;
import com.mmi.services.api.distance.legacy.model.LegacyDistanceResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class DistanceActivity extends AppCompatActivity implements OnMapReadyCallback {

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

        mapboxMap.setMinZoomPreference(4.5);
        mapboxMap.setMaxZoomPreference(18.5);


        mapboxMap.setPadding(20, 20, 20, 20);


        mapboxMap.setCameraPosition(setCameraAndTilt());

        List<Point> coordinatesPoint = new ArrayList<Point>();
        coordinatesPoint.add(Point.fromLngLat(77.257373, 28.551087));
        coordinatesPoint.add(Point.fromLngLat(77.234230, 28.582864));
        if (CheckInternet.isNetworkAvailable(DistanceActivity.this)) {
            calculateDistance(coordinatesPoint);
        } else {
            Toast.makeText(this, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapError(int i, String s) {
    }

    private CameraPosition setCameraAndTilt() {
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

    private void calculateDistance(List<Point> pointList) {
        progressDialogShow();
        new MapmyIndiaDistanceLegacy.Builder()
                .setCoordinates(pointList)
                .setCenter(Point.fromLngLat(77.234230, 28.582864))
                .build()
                .enqueueCall(new Callback<LegacyDistanceResponse>() {
                    @Override
                    public void onResponse(Call<LegacyDistanceResponse> call, Response<LegacyDistanceResponse> response) {
                        progressDialogHide();
                        if (response.code() == 200) {
                            LegacyDistanceResponse legacyDistanceResponse = response.body();
                            List<Distance> distanceList = legacyDistanceResponse.getResults();
                            if (distanceList.size() > 0) {
                                Distance distance = distanceList.get(0);
                                Toast.makeText(DistanceActivity.this, "Distance: " + distance.getLength(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DistanceActivity.this, "Failed: " + legacyDistanceResponse.getResponseCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LegacyDistanceResponse> call, Throwable t) {
                        progressDialogHide();
                    }
                });
    }
}
