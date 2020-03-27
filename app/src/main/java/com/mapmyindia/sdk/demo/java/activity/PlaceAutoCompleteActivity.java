package com.mapmyindia.sdk.demo.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mapmyindia.sdk.demo.R;

public class PlaceAutoCompleteActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_autocomplete);

        findViewById(R.id.fragment_card_mode).setOnClickListener(this);
        findViewById(R.id.fragment_full_mode).setOnClickListener(this);
        findViewById(R.id.activity_card_mode).setOnClickListener(this);
        findViewById(R.id.activity_full_mode).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_card_mode:
                Intent intent = new Intent(this, CardModeFragmentAutocompleteActivity.class);
                startActivity(intent);
                break;

            case R.id.fragment_full_mode:
                intent = new Intent(this, FullModeFragmentAutocompleteActivity.class);
                startActivity(intent);
                break;

            case R.id.activity_card_mode:
                intent = new Intent(this, CardModeActivity.class);
                startActivity(intent);
                break;

            case R.id.activity_full_mode:
                intent = new Intent(this, FullModeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
