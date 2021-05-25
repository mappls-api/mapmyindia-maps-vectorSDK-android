package com.mapmyindia.sdk.demo.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityDirectionUiBinding;
import com.mapmyindia.sdk.demo.java.settingscreen.DirectionWidgetSettingActivity;

public class DirectionUiActivity extends AppCompatActivity {

    private ActivityDirectionUiBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_direction_ui);

        mBinding.btnOpenDirectionFragmentUi.setOnClickListener(v ->{
            startActivity(new Intent(DirectionUiActivity.this, DirectionWidgetActivity.class));
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
            startActivity(new Intent(this, DirectionWidgetSettingActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }
}