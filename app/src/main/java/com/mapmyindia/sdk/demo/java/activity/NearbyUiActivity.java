package com.mapmyindia.sdk.demo.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityNearbyUiBinding;
import com.mapmyindia.sdk.nearby.plugin.MapmyIndiaNearbyWidget;
import com.mmi.services.api.nearby.model.NearbyAtlasResult;

public class NearbyUiActivity extends AppCompatActivity {

    private ActivityNearbyUiBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_nearby_ui);
        mBinding.btnOpenNearbyActivity.setOnClickListener(v -> {
            Intent intent = new MapmyIndiaNearbyWidget.IntentBuilder().build(this);
            startActivityForResult(intent, 101);
        });

        mBinding.btnOpenNearbyFragmentUi.setOnClickListener( view -> {
            startActivity(new Intent(this, NearbyPluginActivity.class));
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            NearbyAtlasResult result = MapmyIndiaNearbyWidget.getNearbyResponse(data);
            Toast.makeText(this, result.placeAddress, Toast.LENGTH_SHORT).show();
        }
    }
}