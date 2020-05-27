package com.mapmyindia.sdk.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapmyindia.sdk.demo.java.activity.FeaturesListActivity;
import com.mapmyindia.sdk.demo.kotlin.activity.FeatureListActivityKotlin;

/**
 * Created by CEINFO on 19-07-2018.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        TextView java = findViewById(R.id.java);
        TextView kotlin = findViewById(R.id.kotlin);

        java.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FeaturesListActivity.class);
                startActivity(intent);
            }
        });
        kotlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FeatureListActivityKotlin.class);
                startActivity(intent);
            }
        });

    }
}
