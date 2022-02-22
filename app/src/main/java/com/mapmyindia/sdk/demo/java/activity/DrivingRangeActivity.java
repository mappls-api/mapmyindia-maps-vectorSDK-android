package com.mapmyindia.sdk.demo.java.activity;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityDrivingRangeBinding;
import com.mapmyindia.sdk.demo.java.settings.MapmyIndiaDrivingRangeSetting;
import com.mapmyindia.sdk.drivingrange.IDrivingRangeListener;
import com.mapmyindia.sdk.drivingrange.MapmyIndiaDrivingRangeContour;
import com.mapmyindia.sdk.drivingrange.MapmyIndiaDrivingRangeOption;
import com.mapmyindia.sdk.drivingrange.MapmyIndiaDrivingRangePlugin;
import com.mapmyindia.sdk.drivingrange.MapmyIndiaDrivingRangeSpeed;
import com.mapmyindia.sdk.drivingrange.MapmyIndiaDrivingRangeTypeInfo;
import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.Style;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mapmyindia.sdk.maps.location.LocationComponentActivationOptions;
import com.mapmyindia.sdk.maps.location.LocationComponentOptions;
import com.mapmyindia.sdk.maps.location.engine.LocationEngineCallback;
import com.mapmyindia.sdk.maps.location.engine.LocationEngineResult;
import com.mapmyindia.sdk.maps.location.permissions.PermissionsListener;
import com.mapmyindia.sdk.maps.location.permissions.PermissionsManager;

import java.util.ArrayList;
import java.util.List;

public class DrivingRangeActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

    private PermissionsManager mPermissionsManager;
    private MapmyIndiaMap mMapmyIndiaMap;
    private MapmyIndiaDrivingRangePlugin mapmyIndiaDrivingRangePlugin;
    private ActivityDrivingRangeBinding mBinding;
    private boolean isLocationCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_driving_range);
        mBinding.mapView.onCreate(savedInstanceState);
        mBinding.mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull MapmyIndiaMap mapmyIndiaMap) {
        this.mMapmyIndiaMap = mapmyIndiaMap;
        mapmyIndiaDrivingRangePlugin = new MapmyIndiaDrivingRangePlugin(mBinding.mapView, mapmyIndiaMap);
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            mapmyIndiaMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocation(style);
                }
            });

        } else {
            mPermissionsManager = new PermissionsManager(this);
            mPermissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void enableLocation(Style style) {
        LocationComponentOptions options = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .build();
        LocationComponentActivationOptions locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this, style)
                        .locationComponentOptions(options)
                        .build();
        mMapmyIndiaMap.getLocationComponent().activateLocationComponent(locationComponentActivationOptions);
        mMapmyIndiaMap.getLocationComponent().setLocationComponentEnabled(true);
        if(MapmyIndiaDrivingRangeSetting.getInstance().isUsingCurrentLocation()) {
            mMapmyIndiaMap.getLocationComponent().getLocationEngine().getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
                @Override
                public void onSuccess(LocationEngineResult locationEngineResult) {
                    if(locationEngineResult == null || locationEngineResult.getLastLocation() == null) {
                        return;
                    }
                    if (isLocationCall) {
                        return;
                    }
                    isLocationCall = true;
                    Location location = locationEngineResult.getLastLocation();
                    drawDrivingRange(Point.fromLngLat(location.getLongitude(), location.getLatitude()));
                }

                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            if(!isLocationCall) {
                Location location = mMapmyIndiaMap.getLocationComponent().getLastKnownLocation();
                if(location != null) {
                    isLocationCall = true;
                    drawDrivingRange(Point.fromLngLat(location.getLongitude(), location.getLatitude()));
                }
            }
        } else {
            drawDrivingRange(MapmyIndiaDrivingRangeSetting.getInstance().getLocation());
        }
    }

    private void drawDrivingRange(Point location) {
        if(mMapmyIndiaMap == null) {
            return;
        }
        mMapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude(), location.longitude()), 12));
        MapmyIndiaDrivingRangeSpeed speed;
        if(MapmyIndiaDrivingRangeSetting.getInstance().getSpeedType() == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL) {
            speed = MapmyIndiaDrivingRangeSpeed.optimal();
        } else if(MapmyIndiaDrivingRangeSetting.getInstance().getPredectiveType() == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT) {
            speed = MapmyIndiaDrivingRangeSpeed.predictiveSpeedFromCurrentTime();
        } else {
            speed = MapmyIndiaDrivingRangeSpeed.predictiveSpeedFromCustomTime(MapmyIndiaDrivingRangeSetting.getInstance().getTime());
        }
        List<MapmyIndiaDrivingRangeContour> contourList = new ArrayList<>();
        contourList.add(MapmyIndiaDrivingRangeContour.builder()
                .value(MapmyIndiaDrivingRangeSetting.getInstance().getContourValue())
                .color(MapmyIndiaDrivingRangeSetting.getInstance().getContourColor())
                .build());
        MapmyIndiaDrivingRangeTypeInfo rangeTypeInfo = MapmyIndiaDrivingRangeTypeInfo.builder()
                .rangeType(MapmyIndiaDrivingRangeSetting.getInstance().getRangeType())
                .contours(contourList)
                .build();
        MapmyIndiaDrivingRangeOption option = MapmyIndiaDrivingRangeOption.builder()
                .location(location)
                .rangeTypeInfo(rangeTypeInfo)
                .drivingProfile(MapmyIndiaDrivingRangeSetting.getInstance().getDrivingProfile())
                .showLocations(MapmyIndiaDrivingRangeSetting.getInstance().isShowLocations())
                .isForPolygons(MapmyIndiaDrivingRangeSetting.getInstance().isForPolygon())
                .denoise(MapmyIndiaDrivingRangeSetting.getInstance().getDenoise())
                .generalize(MapmyIndiaDrivingRangeSetting.getInstance().getGeneralize())
                .speedTypeInfo(speed)
                .build();
        mapmyIndiaDrivingRangePlugin.drawDrivingRange(option, new IDrivingRangeListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(DrivingRangeActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, @NonNull String s) {
                Toast.makeText(DrivingRangeActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapError(int i, String s) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mBinding.mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.mapView.onPause();
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
    public void onExplanationNeeded(List<String> list) {

    }

    @Override
    public void onPermissionResult(boolean b) {
        if (b) {
            if (mMapmyIndiaMap != null) {
                mMapmyIndiaMap.getStyle(new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocation(style);
                    }
                });
            }
        }
    }
}