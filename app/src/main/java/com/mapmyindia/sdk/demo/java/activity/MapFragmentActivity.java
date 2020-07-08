package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.MapFragment;

public class MapFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_id, new MapFragment(), MapFragment.class.getSimpleName())
                .commit();
    }
}