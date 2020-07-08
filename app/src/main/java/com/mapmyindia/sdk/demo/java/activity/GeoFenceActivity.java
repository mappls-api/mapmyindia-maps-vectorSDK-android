package com.mapmyindia.sdk.demo.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityGeoFenceBinding;
import com.mapmyindia.sdk.demo.kotlin.activity.GeoFenceDetailActivity;


public class GeoFenceActivity extends AppCompatActivity {
    ActivityGeoFenceBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_geo_fence);

        mBinding.btnBasicGeofenceActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeoFenceActivity.this, BasicGeoFenceActivity.class);
                startActivity(intent);
            }
        });
        mBinding.btnGeofenceActivityUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeoFenceActivity.this, GeoFenceDetailActivity.class);
                startActivity(intent);
            }
        });


    }
}
