package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityAddMarkerBinding;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.annotations.Marker;
import com.mapmyindia.sdk.maps.annotations.MarkerOptions;
import com.mapmyindia.sdk.maps.camera.CameraELocUpdateFactory;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class AddELocMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapmyIndiaMap mapmyIndiaMap;
    private ActivityAddMarkerBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_marker);
        mBinding.mapView.onCreate(savedInstanceState);
        mBinding.mapView.getMapAsync(this);
        mBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eLoc = mBinding.etELoc.getText().toString();

                if (!eLoc.isEmpty()) {
                    String[] eLocList = eLoc.split(",");
                    addMarker(eLocList);
                } else {
                    Toast.makeText(AddELocMarkerActivity.this, "Please add ELoc", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addMarker(String[] eLocList) {
        if (mapmyIndiaMap != null) {
            mapmyIndiaMap.clear();
            List<MarkerOptions> markerOptions = new ArrayList<>();
            List<String> eLocs = new ArrayList<>();

            for(String eLoc: eLocList) {
                markerOptions.add(new MarkerOptions().eLoc(eLoc).title(eLoc));
                eLocs.add(eLoc);
            }

            List<Marker> markers = mapmyIndiaMap.addMarkers(markerOptions, new MapmyIndiaMap.OnMarkerAddedListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AddELocMarkerActivity.this, "Marker Added Successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(AddELocMarkerActivity.this, "Invalid ELoc", Toast.LENGTH_SHORT).show();
                }
            });

            if(eLocs.size() > 0) {
                if(eLocs.size() == 1) {
                    mapmyIndiaMap.animateCamera(CameraELocUpdateFactory.newELocZoom(eLocs.get(0), 16));
                } else {
                    mapmyIndiaMap.animateCamera(CameraELocUpdateFactory.newELocBounds(eLocs, 10 , 100, 10, 10));
                }
            }

//            mapmyIndiaMap.showMarkers(markers, 10, 100, 10, 10);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mBinding.mapView.onStart();
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
    protected void onPause() {
        super.onPause();
        mBinding.mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.mapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBinding.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        this.mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28, 77), 5));
        mBinding.layoutEloc.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapError(int i, String s) {

    }
}