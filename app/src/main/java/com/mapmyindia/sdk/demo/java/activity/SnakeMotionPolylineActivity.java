package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.plugin.SnakePolyLinePlugin;
import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.geojson.utils.PolylineUtils;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mapmyindia.sdk.maps.geometry.LatLngBounds;
import com.mmi.services.api.OnResponseCallback;
import com.mmi.services.api.directions.DirectionsCriteria;
import com.mmi.services.api.directions.MapmyIndiaDirectionManager;
import com.mmi.services.api.directions.MapmyIndiaDirections;
import com.mmi.services.api.directions.models.DirectionsResponse;
import com.mmi.services.api.directions.models.DirectionsRoute;
import com.mmi.services.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SnakeMotionPolylineActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapmyIndiaMap mapmyIndiaMap;

    private SnakePolyLinePlugin snakePolyLinePlugin;
    private static final Point ORIGIN_POINT = Point.fromLngLat(77.2667594,28.5506561);

    private static final Point DESTINATION_POINT = Point.fromLngLat(77.101318,28.703900);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake_motion_polyline);
        mapView = findViewById(R.id.map_view);
        mapView.getMapAsync(this);


    }

    @Override
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        snakePolyLinePlugin = new SnakePolyLinePlugin(mapView, mapmyIndiaMap);
// Add a source and LineLayer for the snaking directions route line


        getDirectionRoute();


    }

    private void getDirectionRoute() {
        MapmyIndiaDirections mapmyIndiaDirections = MapmyIndiaDirections.builder()
                .origin(ORIGIN_POINT)
                .steps(true)
                .resource(DirectionsCriteria.RESOURCE_ROUTE_ETA)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .destination(DESTINATION_POINT)
                .build();
        MapmyIndiaDirectionManager.newInstance(mapmyIndiaDirections).call(new OnResponseCallback<DirectionsResponse>() {
            @Override
            public void onSuccess(DirectionsResponse directionsResponse) {
                if(directionsResponse != null) {
                    DirectionsRoute currentRoute = directionsResponse.routes().get(0);
                    List<Point> points = PolylineUtils.decode(currentRoute.geometry(), Constants.PRECISION_6);
                    List<LatLng> latLngs = new ArrayList<>();
                    for (Point point : points) {
                        latLngs.add(new LatLng(point.latitude(), point.longitude()));
                    }
                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                            .includes(latLngs)
                            .build();
                    mapmyIndiaMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10, 10, 10, 10));

                    if (snakePolyLinePlugin != null) {
                        snakePolyLinePlugin.create(currentRoute.legs().get(0).steps());
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
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
        if (snakePolyLinePlugin != null) {
            snakePolyLinePlugin.removeCallback();
        }
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
