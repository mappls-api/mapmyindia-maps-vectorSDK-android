package com.mapmyindia.sdk.demo.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.plugins.places.placepicker.PlacePicker;
import com.mapmyindia.sdk.plugins.places.placepicker.model.PlacePickerOptions;
import com.mmi.services.api.Place;

public class PickerActivity extends AppCompatActivity {
    private TextView tvSelectedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);

        tvSelectedPlace = findViewById(R.id.selected_place);
        Button button = findViewById(R.id.place_picker);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new PlacePicker.IntentBuilder()
                        .placeOptions(PlacePickerOptions.builder()
                                .statingCameraPosition(new CameraPosition.Builder()
                                        .target(new LatLng(27.00, 75.0)).zoom(16).build())
                                .build()).build(PickerActivity.this);
                startActivityForResult(intent, 101);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {

            Place place = PlacePicker.getPlace(data);
            tvSelectedPlace.setText(place.getFormattedAddress());

        }
    }
}
