package com.mapmyindia.sdk.demo.java.activity;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.LayoutBasicGeofenceBinding;
import com.mapmyindia.sdk.demo.java.settings.MapmyIndiaGeofenceSetting;
import com.mapmyindia.sdk.geofence.ui.GeoFence;
import com.mapmyindia.sdk.geofence.ui.listeners.GeoFenceViewCallback;
import com.mapmyindia.sdk.geofence.ui.views.GeoFenceOptions;
import com.mapmyindia.sdk.geofence.ui.views.GeoFenceView;

public class BasicGeoFenceActivity extends AppCompatActivity implements GeoFenceViewCallback {
    LayoutBasicGeofenceBinding mBinding;
    GeoFenceView geofenceView;
    GeoFence geoFence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.layout_basic_geofence);
        geoFence = new GeoFence();
        if (MapmyIndiaGeofenceSetting.getInstance().isDefault()){
            geofenceView = new GeoFenceView(this);
            geoFence.setPolygon(false);
            geoFence.setCircleCenter(new LatLng(24.6496185, 77.3062072));
            geoFence.setCircleRadius(200);

        }else {
            GeoFenceOptions.Builder geoFenceOptions = GeoFenceOptions.builder();
            geoFenceOptions.circleOutlineWidth(MapmyIndiaGeofenceSetting.getInstance().getCircleOutlineWidth());
            geoFenceOptions.circleFillColor(MapmyIndiaGeofenceSetting.getInstance().getCircleFillColor());
            geoFenceOptions.circleFillOutlineColor(MapmyIndiaGeofenceSetting.getInstance().getCircleFillOutlineColor());
            geoFenceOptions.draggingLineColor(MapmyIndiaGeofenceSetting.getInstance().getDraggingLineColor());
            geoFenceOptions.maxRadius(MapmyIndiaGeofenceSetting.getInstance().getMaxRadius());
            geoFenceOptions.minRadius(MapmyIndiaGeofenceSetting.getInstance().getMinRadius());
            geoFenceOptions.polygonDrawingLineColor(MapmyIndiaGeofenceSetting.getInstance().getPolygonDrawingLineColor());
            geoFenceOptions.polygonFillColor(MapmyIndiaGeofenceSetting.getInstance().getPolygonFillColor());
            geoFenceOptions.polygonFillOutlineColor(MapmyIndiaGeofenceSetting.getInstance().getPolygonFillOutlineColor());
            geoFenceOptions.polygonOutlineWidth(MapmyIndiaGeofenceSetting.getInstance().getPolygonOutlineWidth());
            geoFenceOptions.showSeekBar(MapmyIndiaGeofenceSetting.getInstance().isShowSeekBar());
            geoFenceOptions.seekbarPrimaryColor(MapmyIndiaGeofenceSetting.getInstance().getSeekbarPrimaryColor());
            geoFenceOptions.seekbarSecondaryColor(MapmyIndiaGeofenceSetting.getInstance().getSeekbarSecondaryColor());
            geoFenceOptions.seekbarCornerRadius(MapmyIndiaGeofenceSetting.getInstance().getSeekbarCornerRadius());

            geofenceView = new GeoFenceView(this,geoFenceOptions.build());
            geoFence.setCircleRadius(200);
            geoFence.setPolygon(MapmyIndiaGeofenceSetting.getInstance().isPolygon());
            geoFence.setCircleCenter(new LatLng(24.6496185, 77.3062072));

            geofenceView.simplifyWhenIntersectingPolygonDetected(MapmyIndiaGeofenceSetting.getInstance().isSimplifyWhenIntersectingPolygonDetected());

        }


        geofenceView.setGeoFence(geoFence);
        geofenceView.onCreate(savedInstanceState);
        mBinding.rootLayout.addView(geofenceView);
        geofenceView.setGeoFenceViewCallback(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        geofenceView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        geofenceView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        geofenceView.onPause();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        geofenceView.onLowMemory();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        geofenceView.onSaveInstanceState(outState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        geofenceView.onDestroy();

    }

    @Override
    public void onGeoFenceReady(MapboxMap mapmyIndiaMap) {

    }

    @Override
    public void geoFenceType(boolean b) {

    }

    @Override
    public void onCircleRadiusChanging(int i) {

    }

    @Override
    public void onUpdateGeoFence(GeoFence geoFence) {


    }

    @Override
    public void hasIntersectionPoints() {

    }
}


