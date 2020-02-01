package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.utils.CheckInternet;
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog;
import com.mmi.services.api.directions.DirectionsCriteria;
import com.mmi.services.api.distance.MapmyIndiaDistanceMatrix;
import com.mmi.services.api.distance.models.DistanceResponse;
import com.mmi.services.api.distance.models.DistanceResults;

import java.text.DecimalFormat;
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
    private TextView tvDistance, tvDuration;
    private LinearLayout directionDetailsLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance_activity);
        mapView = findViewById(R.id.map_view);
        directionDetailsLayout = findViewById(R.id.distance_details_layout);
        tvDistance = findViewById(R.id.tv_distance);
        tvDuration = findViewById(R.id.tv_duration);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        transparentProgressDialog = new TransparentProgressDialog(this, R.drawable.circle_loader, "");
    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMap) {


        mapmyIndiaMap.setPadding(20, 20, 20, 20);


        mapmyIndiaMap.setCameraPosition(setCameraAndTilt());

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
        MapmyIndiaDistanceMatrix.builder()
                .coordinates(pointList)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .resource(DirectionsCriteria.RESOURCE_DISTANCE_ETA)
                .build()
                .enqueueCall(new Callback<DistanceResponse>() {
                    @Override
                    public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                        progressDialogHide();
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                DistanceResponse legacyDistanceResponse = response.body();
                                DistanceResults distanceResults = legacyDistanceResponse.results();

                                if (distanceResults != null) {
                                    updateData(distanceResults);
                                } else {
                                    Toast.makeText(DistanceActivity.this, "Failed: " + legacyDistanceResponse.responseCode(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DistanceResponse> call, Throwable t) {
                        progressDialogHide();
                    }
                });
    }


    private void updateData(DistanceResults distanceResults) {

        directionDetailsLayout.setVisibility(View.VISIBLE);
        tvDuration.setText("(" + getFormattedDuration(distanceResults.durations().get(0)[1]) + ")");
        tvDistance.setText(getFormattedDistance(distanceResults.distances().get(0)[1]));
    }

    /**
     * Get Formatted Distance
     *
     * @param distance route distance
     * @return distance in Kms if distance > 1000 otherwise in mtr
     */
    private String getFormattedDistance(double distance) {

        if ((distance / 1000) < 1) {
            return distance + "mtr.";
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(distance / 1000) + "Km.";
    }

    /**
     * Get Formatted Duration
     *
     * @param duration route duration
     * @return formatted duration
     */
    private String getFormattedDuration(double duration) {
        long min = (long) (duration % 3600 / 60);
        long hours = (long) (duration % 86400 / 3600);
        long days = (long) (duration / 86400);
        if (days > 0L) {
            return days + " " + (days > 1L ? "Days" : "Day") + " " + hours + " " + "hr" + (min > 0L ? " " + min + " " + "min." : "");
        } else {
            return hours > 0L ? hours + " " + "hr" + (min > 0L ? " " + min + " " + "min" : "") : min + " " + "min.";
        }
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
