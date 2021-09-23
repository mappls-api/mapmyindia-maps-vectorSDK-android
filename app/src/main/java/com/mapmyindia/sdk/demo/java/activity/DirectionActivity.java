package com.mapmyindia.sdk.demo.java.activity;

import static java.lang.Double.parseDouble;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.plugin.DirectionPolylinePlugin;
import com.mapmyindia.sdk.demo.java.utils.CheckInternet;
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog;
import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.geojson.utils.PolylineUtils;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.annotations.MarkerOptions;
import com.mapmyindia.sdk.maps.camera.CameraPosition;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mapmyindia.sdk.maps.geometry.LatLngBounds;
import com.mmi.services.api.OnResponseCallback;
import com.mmi.services.api.directions.DirectionsCriteria;
import com.mmi.services.api.directions.MapmyIndiaDirectionManager;
import com.mmi.services.api.directions.MapmyIndiaDirections;
import com.mmi.services.api.directions.models.DirectionsResponse;
import com.mmi.services.api.directions.models.DirectionsRoute;
import com.mmi.services.api.directions.models.DirectionsWaypoint;
import com.mmi.services.utils.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by CEINFO on 26-02-2019.
 */

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback, MapmyIndiaMap.OnMapLongClickListener {

    private MapmyIndiaMap mapmyIndiaMap;
    private MapView mapView;
    private TransparentProgressDialog transparentProgressDialog;
    private String profile = DirectionsCriteria.PROFILE_DRIVING;
    private TabLayout profileTabLayout;
    private String resource = DirectionsCriteria.RESOURCE_ROUTE;
    private LinearLayout directionDetailsLayout;
    private TextView tvDistance, tvDuration;
    private DirectionPolylinePlugin directionPolylinePlugin;
    private FloatingActionButton floatingActionButton;
    private String mDestination = "MMI000";
    private String mSource = "28.594475,77.202432";
    private String wayPoints;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_layout);
        mapView = findViewById(R.id.map_view);

        profileTabLayout = findViewById(R.id.tab_layout_profile);
        RadioGroup rgResource = findViewById(R.id.rg_resource_type);

        directionDetailsLayout = findViewById(R.id.direction_details_layout);
        tvDistance = findViewById(R.id.tv_distance);
        tvDuration = findViewById(R.id.tv_duration);
        floatingActionButton = findViewById(R.id.edit_btn);
        floatingActionButton.setOnClickListener(v ->
                {
                    Intent intent = new Intent(this, InputActivity.class);
                    intent.putExtra("origin", mSource);
                    intent.putExtra("destination", mDestination);
                    intent.putExtra("waypoints", wayPoints);
                    startActivityForResult(intent, 500);
                }
        );
//        profileTabLayout.setVisibility(View.GONE);
        profileTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mapmyIndiaMap == null) {
                    if (profileTabLayout.getTabAt(0) != null) {
                        Objects.requireNonNull(profileTabLayout.getTabAt(0)).select();
                        return;
                    }
                }
                switch (tab.getPosition()) {
                    case 0:
                        profile = DirectionsCriteria.PROFILE_DRIVING;
                        rgResource.setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        profile = DirectionsCriteria.PROFILE_BIKING;
                        rgResource.check(R.id.rb_without_traffic);
                        rgResource.setVisibility(View.GONE);
                        break;

                    case 2:
                        profile = DirectionsCriteria.PROFILE_WALKING;
                        rgResource.check(R.id.rb_without_traffic);
                        rgResource.setVisibility(View.GONE);
                        break;

                    default:
                        break;
                }

                getDirections();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        rgResource.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.rb_without_traffic:
                    resource = DirectionsCriteria.RESOURCE_ROUTE;
                    break;

                case R.id.rb_with_traffic:
                    resource = DirectionsCriteria.RESOURCE_ROUTE_TRAFFIC;
                    break;

                case R.id.rb_with_route_eta:
                    resource = DirectionsCriteria.RESOURCE_ROUTE_ETA;
                    break;

                default:
                    break;
            }

            getDirections();
        });
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        transparentProgressDialog = new TransparentProgressDialog(this, R.drawable.circle_loader, "");

    }

    @Override
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;


        mapmyIndiaMap.setPadding(20, 20, 20, 20);
//        profileTabLayout.setVisibility(View.VISIBLE);
        mapmyIndiaMap.addOnMapLongClickListener(this);
        mapmyIndiaMap.setCameraPosition(setCameraAndTilt());
        if (CheckInternet.isNetworkAvailable(DirectionActivity.this)) {
            getDirections();
        } else {
            Toast.makeText(this, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Set Camera Position
     *
     * @return camera position
     */
    protected CameraPosition setCameraAndTilt() {
        return new CameraPosition.Builder().target(new LatLng(
                28.551087, 77.257373)).zoom(14).tilt(0).build();
    }

    /**
     * Show Progress Dialog
     */
    private void progressDialogShow() {
        transparentProgressDialog.show();
    }

    /**
     * Hide Progress dialog
     */
    private void progressDialogHide() {
        transparentProgressDialog.dismiss();
    }

    /**
     * Get Directions
     */
    private void getDirections() {
        progressDialogShow();

        Object dest = !mDestination.contains(",") ? mDestination : Point.fromLngLat(parseDouble(mDestination.split(",")[1]), parseDouble(mDestination.split(",")[0]));
        Object src = !mSource.contains(",") ? mSource : Point.fromLngLat(parseDouble(mSource.split(",")[1]), parseDouble(mSource.split(",")[0]));

        MapmyIndiaDirections.Builder builder = MapmyIndiaDirections.builder();

        if (src instanceof String) {
            builder.origin(String.valueOf(src));
        } else {
            builder.origin((Point) src);
        }

        if (dest instanceof String) {
            builder.destination(String.valueOf(dest));
        } else {
            builder.destination((Point) dest);
        }

        if (wayPoints != null) {
            if (!wayPoints.contains(";")) {
                if (!wayPoints.contains(",")) {
                    Log.e("taf", wayPoints);
                    builder.addWaypoint(wayPoints);
                } else {
                    Point point = Point.fromLngLat(Double.parseDouble(wayPoints.split(",")[1]), Double.parseDouble(wayPoints.split(",")[0]));
                    builder.addWaypoint(point);
                }
            } else {
                String[] wayPointsArray = wayPoints.split(";");
                for (String value : wayPointsArray) {
                    if (!value.contains(",")) {
                        builder.addWaypoint(value);
                    } else {
                        Point point = Point.fromLngLat(Double.parseDouble(value.split(",")[1]), Double.parseDouble(value.split(",")[0]));
                        builder.addWaypoint(point);
                    }
                }
            }
        }
        builder.profile(profile)
                .resource(resource)
                .steps(true)
                .alternatives(false)
                .overview(DirectionsCriteria.OVERVIEW_FULL).build();
        MapmyIndiaDirectionManager.newInstance(builder.build()).call(new OnResponseCallback<DirectionsResponse>() {
            @Override
            public void onSuccess(DirectionsResponse directionsResponse) {
                if (directionsResponse != null) {
                    List<DirectionsRoute> results = directionsResponse.routes();
                    mapmyIndiaMap.clear();

                    if (results.size() > 0) {
                        DirectionsRoute directionsRoute = results.get(0);
                        if (directionsRoute != null && directionsRoute.geometry() != null) {
                            drawPath(PolylineUtils.decode(directionsRoute.geometry(), Constants.PRECISION_6));
                            updateData(directionsRoute);
                        }
                    }
                    List<DirectionsWaypoint> directionsWaypoints = directionsResponse.waypoints();
                    if (directionsWaypoints != null && directionsWaypoints.size() > 0) {
                        for (DirectionsWaypoint directionsWaypoint : directionsWaypoints) {
                            mapmyIndiaMap.addMarker(new MarkerOptions().position(new LatLng(directionsWaypoint.location().latitude(), directionsWaypoint.location().longitude())));
                        }
                    }
                }
                progressDialogHide();
            }

            @Override
            public void onError(int i, String s) {
                progressDialogHide();
                Toast.makeText(DirectionActivity.this, s + "----" + i, Toast.LENGTH_LONG).show();
            }
        });


    }

    /**
     * Update Route data
     *
     * @param directionsRoute route data
     */
    private void updateData(@NonNull DirectionsRoute directionsRoute) {
        if (directionsRoute.distance() != null && directionsRoute.distance() != null) {
            directionDetailsLayout.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.VISIBLE);
            tvDuration.setText("(" + getFormattedDuration(directionsRoute.duration()) + ")");
            tvDistance.setText(getFormattedDistance(directionsRoute.distance()));
        }
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

    /**
     * Add polyline along the points
     *
     * @param waypoints route points
     */
    private void drawPath(@NonNull List<Point> waypoints) {
        ArrayList<LatLng> listOfLatLng = new ArrayList<>();
        for (Point point : waypoints) {
            listOfLatLng.add(new LatLng(point.latitude(), point.longitude()));
        }

        if (directionPolylinePlugin == null) {
            directionPolylinePlugin = new DirectionPolylinePlugin(mapmyIndiaMap, mapView, profile);
            directionPolylinePlugin.createPolyline(listOfLatLng);
        } else {
            directionPolylinePlugin.updatePolyline(profile, listOfLatLng);

        }
//        mapmyIndiaMap.addPolyline(new PolylineOptions().addAll(listOfLatLng).color(Color.parseColor("#3bb2d0")).width(4));
        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(listOfLatLng).build();
        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 30));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500 && resultCode == RESULT_OK) {
            if (data.hasExtra("destination")) {
                mDestination = data.getStringExtra("destination");
            }
            if (data.hasExtra("origin")) {
                mSource = data.getStringExtra("origin");
            }
            if (data.hasExtra("waypoints")) {
                wayPoints = data.getStringExtra("waypoints");
            }
            getDirections();
        }
    }

    @Override
    public boolean onMapLongClick(@NonNull LatLng latLng) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Select Point as Source or Destination");

        alertDialog.setPositiveButton("Source", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSource = latLng.getLatitude() + "," + latLng.getLongitude();
                getDirections();
            }
        });
        alertDialog.setNegativeButton("Destination", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDestination = latLng.getLatitude() + "," + latLng.getLongitude();
                getDirections();
            }
        });
        alertDialog.setNeutralButton("Waypoint", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(wayPoints)) {
                    wayPoints = latLng.getLatitude() + "," + latLng.getLongitude();
                } else {
                    String wayPoint = wayPoints + ";" + latLng.getLatitude() + "," + latLng.getLongitude();
                    wayPoints = wayPoint;
                }
                getDirections();
            }
        });

        alertDialog.setCancelable(true);
        alertDialog.show();
        return false;
    }
}
