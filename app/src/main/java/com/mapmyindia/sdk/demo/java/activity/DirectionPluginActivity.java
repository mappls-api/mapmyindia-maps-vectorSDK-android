package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityDirectionPluginBinding;
import com.mapmyindia.sdk.direction.ui.DirectionCallback;
import com.mapmyindia.sdk.direction.ui.DirectionFragment;
import com.mapmyindia.sdk.direction.ui.model.DirectionOptions;
import com.mapmyindia.sdk.direction.ui.model.DirectionPoint;
import com.mmi.services.api.directions.models.DirectionsResponse;

import java.util.List;

public class DirectionPluginActivity extends AppCompatActivity {

    private ActivityDirectionPluginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_direction_plugin);

        mBinding.btnOpenDirectionFragmentUi.setOnClickListener(v ->{


            DirectionOptions options = DirectionOptions.builder().build();
            DirectionFragment directionFragment = DirectionFragment.newInstance(options);


            getSupportFragmentManager().
                    beginTransaction().
                    add(R.id.container, directionFragment, DirectionFragment.class.getSimpleName()).
                    addToBackStack(null).
                    commit();

            directionFragment.setDirectionCallback(new DirectionCallback() {
                @Override
                public void onCancel() {
                     getSupportFragmentManager().beginTransaction().remove(directionFragment).commit();
                }

                @Override
                public void onStartNavigation(DirectionPoint directionPoint, DirectionPoint directionPoint1, List<DirectionPoint> list, DirectionsResponse directionsResponse, int i) {
                    Toast.makeText(DirectionPluginActivity.this, "On Navigation Start", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }
}