package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.MapmyIndiaMapConfiguration;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.style.OnStyleLoadListener;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.StyleActivityBinding;
import com.mapmyindia.sdk.demo.java.adapter.StyleAdapter;

import timber.log.Timber;

public class StyleActivity extends AppCompatActivity implements OnMapReadyCallback {

    private StyleActivityBinding mBinding;
    private BottomSheetBehavior<RelativeLayout> bottomSheetBehavior;
    private StyleAdapter adapter;
    private MapboxMap mapmyIndiaMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.style_activity);
        mBinding.mapView.onCreate(savedInstanceState);

        mBinding.mapView.getMapAsync(this);
        bottomSheetBehavior = BottomSheetBehavior.from(mBinding.bottomSheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(200);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        mBinding.rvStyle.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StyleAdapter();
        mBinding.rvStyle.setAdapter(adapter);
        mBinding.saveLastStyle.setChecked(MapmyIndiaMapConfiguration.getInstance().isShowLastSelectedStyle());

        mBinding.saveLastStyle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapmyIndiaMapConfiguration.getInstance().setShowLastSelectedStyle(isChecked);
            }
        });

        adapter.setOnStyleSelectListener(style -> {
            if(mapmyIndiaMap != null) {
                mapmyIndiaMap.setMapmyIndiaStyle(style.getName(), new OnStyleLoadListener() {
                    @Override
                    public void onError(String error) {
                        Toast.makeText(StyleActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStyleLoaded() {
                        Toast.makeText(StyleActivity.this, "onStyleLoaded", Toast.LENGTH_SHORT).show();
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
            }
        });
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
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.mapView.onLowMemory();
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
    public void onMapReady(MapboxMap mapmyIndiaMap) {
        Timber.tag("onMapReady").e("SUCCESS");
        this.mapmyIndiaMap = mapmyIndiaMap;
        this.mapmyIndiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.6466772,76.8130614), 12));
        if (adapter != null) {
            Timber.e(new Gson().toJson(this.mapmyIndiaMap.getMapmyIndiaAvailableStyles()));
            adapter.setStyleList(this.mapmyIndiaMap.getMapmyIndiaAvailableStyles());
        }
    }

    @Override
    public void onMapError(int i, String s) {
        Timber.tag("onMapError").e(i + "------" + s);
    }
}