package com.mapmyindia.sdk.demo.java.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mapmyindia.sdk.demo.R;

public class InputActivity extends AppCompatActivity {

    private EditText originEt, destinationEt, wayPointsEt;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        originEt = findViewById(R.id.origin_et);
        destinationEt = findViewById(R.id.destination_et);
        wayPointsEt = findViewById(R.id.waypoints_et);
        btn = findViewById(R.id.set_btn);
        if(savedInstanceState == null) {
            String source = getIntent().getStringExtra("origin");
            String destination = getIntent().getStringExtra("destination");
            String waypoints = getIntent().getStringExtra("waypoints");
            if(source != null) {
                originEt.setText(source);
            }
            if(destination != null) {
                destinationEt.setText(destination);
            }
            if(waypoints != null) {
                wayPointsEt.setText(waypoints);
            }
        }

        btn.setOnClickListener(v -> {

            String origin = originEt.getText().toString();
            String destination = destinationEt.getText().toString();
            String wayPoints = wayPointsEt.getText().toString();
            Intent intent = new Intent();
            if (!TextUtils.isEmpty(origin)) {
                intent.putExtra("origin", origin);
            }
            if (!TextUtils.isEmpty(destination)) {
                intent.putExtra("destination", destination);
            }
            if (!TextUtils.isEmpty(wayPoints)) {
                intent.putExtra("waypoints", wayPoints);
            }
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}