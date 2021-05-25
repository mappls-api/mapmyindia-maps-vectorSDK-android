package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityNearbyPluginBinding;
import com.mapmyindia.sdk.nearby.plugin.MapmyIndiaNearbyFragment;

public class NearbyPluginActivity extends AppCompatActivity {

    private ActivityNearbyPluginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_nearby_plugin);

        MapmyIndiaNearbyFragment nearbyFragment = MapmyIndiaNearbyFragment.newInstance();

        getSupportFragmentManager().
                beginTransaction().
                replace(mBinding.fragmentContainer.getId(), nearbyFragment, MapmyIndiaNearbyFragment.class.getSimpleName())
                .commit();


        nearbyFragment.setMapmyIndiaNearbyCallback(nearbyAtlasResult -> {
            // getSupportFragmentManager().beginTransaction().remove(nearbyFragment).commit();

            Toast.makeText(NearbyPluginActivity.this, nearbyAtlasResult.placeAddress, Toast.LENGTH_SHORT).show();
        });
    }
}