package com.mapmyindia.sdk.demo.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.utils.CheckInternet;
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog;
import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.camera.CameraPosition;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mmi.services.api.OnResponseCallback;
import com.mmi.services.api.directions.DirectionsCriteria;
import com.mmi.services.api.distance.MapmyIndiaDistanceMatrix;
import com.mmi.services.api.distance.MapmyIndiaDistanceMatrixManager;
import com.mmi.services.api.distance.models.DistanceResponse;
import com.mmi.services.api.distance.models.DistanceResults;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class DistanceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private TransparentProgressDialog transparentProgressDialog;
    private TextView tvDistance, tvDuration;
    private LinearLayout directionDetailsLayout;

    private FloatingActionButton floatingActionButton;
    private String mDestination="28.551087,77.257373";
    private String mSource ="28.582864,77.234230";
    private String waypoints;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance_activity);
        mapView = findViewById(R.id.map_view);
        floatingActionButton = findViewById(R.id.edit_btn);
        directionDetailsLayout = findViewById(R.id.distance_details_layout);
        tvDistance = findViewById(R.id.tv_distance);
        tvDuration = findViewById(R.id.tv_duration);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        transparentProgressDialog = new TransparentProgressDialog(this, R.drawable.circle_loader, "");
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this,InputActivity.class);
            intent.putExtra("origin", mSource);
            intent.putExtra("destination", mDestination);
            intent.putExtra("waypoints", waypoints);
            startActivityForResult(intent,501);
        });
    }

    @Override
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {




        mapmyIndiaMap.setCameraPosition(setCameraAndTilt());

       /* List<Point> coordinatesPoint = new ArrayList<Point>();
        coordinatesPoint.add(Point.fromLngLat(77.257373, 28.551087));
        coordinatesPoint.add(Point.fromLngLat(77.234230, 28.582864));*/
        if (CheckInternet.isNetworkAvailable(DistanceActivity.this)) {
            calculateDistance(null,null);
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

    private void calculateDistance(List<Point> pointList, List<String> elocs) {
        progressDialogShow();
       MapmyIndiaDistanceMatrix.Builder builder =   MapmyIndiaDistanceMatrix.builder();
            if (mSource!=null){
                if (!mSource.contains(",")){
                    builder.coordinate(mSource);
                }else {
                    Point point =Point.fromLngLat(Double.parseDouble(mSource.split(",")[1]),Double.parseDouble(mSource.split(",")[0]));
                  builder.coordinate(point);
                }
            }

                 if (elocs!=null&&elocs.size()>0){
                     builder.coordinateList(elocs);
                 }
                 if (pointList!=null&&pointList.size()>0){
                     builder.coordinates(pointList);
                 }
        if (mDestination!=null){
            if (!mDestination.contains(",")){
                builder.coordinate(mDestination);
            }else {
                Point point =Point.fromLngLat(Double.parseDouble(mDestination.split(",")[1]),Double.parseDouble(mDestination.split(",")[0]));
                builder.coordinate(point);
            }
        }
                builder.profile(DirectionsCriteria.PROFILE_DRIVING)
                .resource(DirectionsCriteria.RESOURCE_DISTANCE_ETA)
                .build();
        MapmyIndiaDistanceMatrixManager.newInstance(builder.build()).call(new OnResponseCallback<DistanceResponse>() {
            @Override
            public void onSuccess(DistanceResponse distanceResponse) {
                progressDialogHide();
                if (distanceResponse != null) {
                    DistanceResults distanceResults = distanceResponse.results();

                    if (distanceResults != null) {
                        updateData(distanceResults);
                    } else {
                        Toast.makeText(DistanceActivity.this, "Failed: " + distanceResponse.responseCode(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                progressDialogHide();
                Toast.makeText(DistanceActivity.this, "Failed: " + s, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateData(DistanceResults distanceResults) {

        directionDetailsLayout.setVisibility(View.VISIBLE);
        floatingActionButton.setVisibility(View.VISIBLE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==501&&resultCode==RESULT_OK){

            List<String> elocs= new ArrayList<>();
            List<Point> points = new ArrayList<>();
            if (data.hasExtra("origin")){
               mSource = data.getStringExtra("origin");

            }
            if (data.hasExtra("waypoints")){
               String wayPoints=  data.getStringExtra("waypoints");
                if (!wayPoints.contains(";")){
                    if (!wayPoints.contains(",")){
                        elocs.add(wayPoints);
                    }else{
                        Point point = Point.fromLngLat(Double.parseDouble(wayPoints.split(",")[1]),Double.parseDouble(wayPoints.split(",")[0]));
                        points.add(point);
                    }
                }else {
                    String [] wayPointsArray = wayPoints.split(";");
                    for (String value :wayPointsArray){
                        if (!value.contains(",")){
                            elocs.add(value);
                        }else{
                            Point point = Point.fromLngLat(Double.parseDouble(value.split(",")[1]),Double.parseDouble(value.split(",")[0]));
                           points.add(point);
                        }
                    }
                }
                this.waypoints = wayPoints;
            }
            if (data.hasExtra("destination")){
                mDestination = data.getStringExtra("destination");
            }

            calculateDistance(points,elocs);
        }
    }
}
