package com.mapmyindia.sdk.demo.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.settings.MapmyIndiaPlacePickerSetting;
import com.mapmyindia.sdk.demo.java.settingscreen.PlacePickerSettingsActivity;
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions;
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
                PlacePicker.IntentBuilder intentBuilder =  new PlacePicker.IntentBuilder();

                if (!MapmyIndiaPlacePickerSetting.getInstance().isDefault()){

                    PlaceOptions options = PlaceOptions.builder()
                            .zoom(MapmyIndiaPlacePickerSetting.getInstance().getZoom())
                            .hint(MapmyIndiaPlacePickerSetting.getInstance().getHint())
                            .location(MapmyIndiaPlacePickerSetting.getInstance().getLocation())
                            .filter(MapmyIndiaPlacePickerSetting.getInstance().getFilter())
                            .saveHistory(MapmyIndiaPlacePickerSetting.getInstance().isEnableHistory())
                            .pod(MapmyIndiaPlacePickerSetting.getInstance().getPod())
                            .attributionHorizontalAlignment(MapmyIndiaPlacePickerSetting.getInstance().getSignatureVertical())
                            .attributionVerticalAlignment(MapmyIndiaPlacePickerSetting.getInstance().getSignatureHorizontal())
                            .logoSize(MapmyIndiaPlacePickerSetting.getInstance().getLogoSize())
                            .tokenizeAddress(MapmyIndiaPlacePickerSetting.getInstance().isTokenizeAddress())
                            .historyCount(MapmyIndiaPlacePickerSetting.getInstance().getHistoryCount())
                            .backgroundColor(getResources().getColor(MapmyIndiaPlacePickerSetting.getInstance().getBackgroundColor()))
                            .toolbarColor(getResources().getColor(MapmyIndiaPlacePickerSetting.getInstance().getToolbarColor()))
                            .build();

                    intentBuilder.placeOptions(PlacePickerOptions.builder()
                            .toolbarColor(MapmyIndiaPlacePickerSetting.getInstance().getPickerToolbarColor())
                            .includeDeviceLocationButton(MapmyIndiaPlacePickerSetting.getInstance().isIncludeDeviceLocation())
                            .includeSearch(MapmyIndiaPlacePickerSetting.getInstance().isIncludeSearch())
                            .searchPlaceOption(options)
                            .statingCameraPosition(new CameraPosition.Builder()
                                    .target(new LatLng(27.00, 75.0)).zoom(16).build())
                            .build());
                }else {
                    intentBuilder.placeOptions(PlacePickerOptions.builder()
                            .statingCameraPosition(new CameraPosition.Builder()
                                    .target(new LatLng(27.00, 75.0)).zoom(16).build())
                            .build());
                }

                Intent intent = intentBuilder.build(PickerActivity.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.widget_setting_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.widget_setting) {
            startActivity(new Intent(this, PlacePickerSettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }
}
