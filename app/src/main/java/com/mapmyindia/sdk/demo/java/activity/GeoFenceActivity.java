package com.mapmyindia.sdk.demo.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityGeoFenceBinding;
import com.mapmyindia.sdk.demo.java.settingscreen.GeofenceWidgetSettingsActivity;
import com.mapmyindia.sdk.demo.kotlin.activity.GeoFenceDetailActivity;


public class GeoFenceActivity extends AppCompatActivity {
    ActivityGeoFenceBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_geo_fence);

        mBinding.btnBasicGeofenceActivity.setOnClickListener(v -> {
            Intent intent = new Intent(GeoFenceActivity.this, BasicGeoFenceActivity.class);
            startActivity(intent);
        });
        mBinding.btnGeofenceActivityUi.setOnClickListener(v -> {
            Intent intent = new Intent(GeoFenceActivity.this, GeoFenceDetailActivity.class);
            startActivity(intent);
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.widget_setting_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.widget_setting) {
            startActivity(new Intent(this, GeofenceWidgetSettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }
}
