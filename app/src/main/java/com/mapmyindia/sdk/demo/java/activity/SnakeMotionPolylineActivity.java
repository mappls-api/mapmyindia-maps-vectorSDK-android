package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.PolylineUtils;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.plugin.SnakePolyLinePlugin;
import com.mmi.services.api.directions.DirectionsCriteria;
import com.mmi.services.api.directions.MapmyIndiaDirections;
import com.mmi.services.api.directions.models.DirectionsResponse;
import com.mmi.services.api.directions.models.DirectionsRoute;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SnakeMotionPolylineActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapmyIndiaMap;

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
    public void onMapReady(MapboxMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        snakePolyLinePlugin = new SnakePolyLinePlugin(mapView, mapmyIndiaMap);
// Add a source and LineLayer for the snaking directions route line


        getDirectionRoute();


    }

    private void getDirectionRoute() {
        MapmyIndiaDirections.builder()
                .origin(ORIGIN_POINT)
                .steps(true)
                .resource(DirectionsCriteria.RESOURCE_ROUTE_ETA)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .destination(DESTINATION_POINT)
                .build()
                .enqueueCall(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        //handle response
                        DirectionsRoute currentRoute = response.body().routes().get(0);
                        List<Point> points = PolylineUtils.decode(currentRoute.geometry(), Constants.PRECISION_6);
                        List<LatLng> latLngs = new ArrayList<>();
                        for(Point point: points) {
                            latLngs.add(new LatLng(point.latitude(), point.longitude()));
                        }
                        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                .includes(latLngs)
                                .build();
                        mapmyIndiaMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10 ,10 ,10 ,10));

                        if(snakePolyLinePlugin != null) {
                            snakePolyLinePlugin.create(currentRoute.legs().get(0).steps());
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        t.printStackTrace();
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
