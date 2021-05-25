package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.PolylineUtils;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.BaseLayoutBinding;
import com.mapmyindia.sdk.demo.java.plugin.TrackingPlugin;
import com.mmi.services.api.directions.DirectionsCriteria;
import com.mmi.services.api.directions.MapmyIndiaDirections;
import com.mmi.services.api.directions.models.DirectionsResponse;
import com.mmi.services.api.directions.models.DirectionsRoute;
import com.mmi.services.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private BaseLayoutBinding mBinding;
    private List<Point> travelledPoints;
    private MapboxMap mapmyIndiaMap;
    private TrackingPlugin trackingPlugin;
    private int index = 0;


    private final int MARKER_TRANSLATION_COMPLETE = 125;

    private final Handler trackingHandler = new Handler(Looper.getMainLooper());
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sendMessageToBackgroundHandler();

            trackingHandler.postDelayed(runnable, 3000);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.base_layout);
        mBinding.mapView.onCreate(savedInstanceState);
        mBinding.mapView.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.mapView.onStart();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mBinding.mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.mapView.onResume();
        if(travelledPoints != null) {
            trackingHandler.post(runnable);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.mapView.onPause();
        trackingHandler.removeCallbacks(runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBinding.mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.mapView.onLowMemory();
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapmyIndiaMap = mapboxMap;
        mapmyIndiaMap.setMaxZoomPreference(16);
        mapboxMap.setPadding(0,0,0,0);
        trackingPlugin = new TrackingPlugin(mBinding.mapView, mapmyIndiaMap);
        callRouteETA();
    }


    private void sendMessageToBackgroundHandler() {
        try {
            // GeoPoint nextLocation = mOrderTrackingActivity.getLocationBlockingQueue().take();
            if (index < travelledPoints.size() - 1) {
                trackingPlugin.animateCar(travelledPoints.get(index), travelledPoints.get(index + 1));
                index++;
                callTravelledRoute();

            } else {
                trackingHandler.removeCallbacks(runnable);
//                                Toast.makeText(this, "Route END", Toast.LENGTH_SHORT).show();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void callRouteETA() {
        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.15183700, 72.93374500), 12));
        MapmyIndiaDirections.Builder builder = MapmyIndiaDirections.builder()
                .steps(true)
                .origin(Point.fromLngLat(72.93374500, 19.15183700))
                .destination(Point.fromLngLat(72.9344, 19.1478))
                .resource(DirectionsCriteria.RESOURCE_ROUTE_ETA)
                .overview(DirectionsCriteria.OVERVIEW_SIMPLIFIED);

        builder.build().enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.isSuccessful()) {
                    DirectionsResponse directionsResponse = response.body();
                    if (directionsResponse != null && directionsResponse.routes() != null && directionsResponse.routes().size() > 0) {
                        DirectionsRoute directionsRoute = directionsResponse.routes().get(0);
                        if (directionsRoute != null && directionsRoute.geometry() != null) {
                            travelledPoints = PolylineUtils.decode(directionsRoute.geometry(), Constants.PRECISION_6);
                            startTracking();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });


    }

    private void startTracking() {

        trackingPlugin.addMarker(travelledPoints.get(0));
        trackingHandler.post(runnable);
    }

    private void callTravelledRoute() {
        MapmyIndiaDirections.builder()
                .origin(travelledPoints.get(index))
                .destination(Point.fromLngLat(72.9344, 19.1478))
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .steps(true)
                .routeType(DirectionsCriteria.DISTANCE_ROUTE_TYPE_SHORTEST)
                .resource(DirectionsCriteria.RESOURCE_ROUTE)
                .build().enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if(response.isSuccessful()) {
                    DirectionsResponse directionsResponse = response.body();
                    if (directionsResponse != null && directionsResponse.routes() != null && directionsResponse.routes().size() > 0) {
                        DirectionsRoute directionsRoute = directionsResponse.routes().get(0);
                        if (directionsRoute != null && directionsRoute.geometry() != null) {
                            trackingPlugin.updatePolyline(directionsRoute);
                            List<Point> remainingPath = PolylineUtils.decode(directionsRoute.geometry(), Constants.PRECISION_6);
                            List<LatLng> latLngList = new ArrayList<>();
                            for(Point point: remainingPath) {
                                latLngList.add(new LatLng(point.latitude(), point.longitude()));
                            }
                            if(latLngList.size() > 0) {
                                if(latLngList.size() == 1) {
                                    mapmyIndiaMap.easeCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get(0), 12));
                                } else {
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .includes(latLngList)
                                            .build();
                                    mapmyIndiaMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 180, 0, 180, 0));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onMapError(int i, String s) {

    }
}