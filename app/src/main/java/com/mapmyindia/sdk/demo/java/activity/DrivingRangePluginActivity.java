package com.mapmyindia.sdk.demo.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityDrivingRangePluginBinding;
import com.mapmyindia.sdk.demo.java.settingscreen.DrivingRangeSettingActivity;

public class DrivingRangePluginActivity extends AppCompatActivity {

    private ActivityDrivingRangePluginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_driving_range_plugin);
        mBinding.btnOpenDrivingRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DrivingRangePluginActivity.this, DrivingRangeActivity.class));
            }
        });
        mBinding.btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DrivingRangePluginActivity.this, DrivingRangeSettingActivity.class));
            }
        });
    }
}