package com.mapmyindia.sdk.demo.java.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.settings.MapmyIndiaPlaceWidgetSetting;
import com.mapmyindia.sdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapmyindia.sdk.plugins.places.common.PlaceConstants;
import com.mmi.services.api.autosuggest.model.ELocation;

public class CardModeActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private MapboxMap mapmyIndiaMap;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_mode_fragment);
        mapView = findViewById(R.id.map_view);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        textView = findViewById(R.id.search);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mapmyIndiaMap != null) {
                    PlaceOptions placeOptions = PlaceOptions.builder()
                            .location(MapmyIndiaPlaceWidgetSetting.getInstance().getLocation())
                            .filter(MapmyIndiaPlaceWidgetSetting.getInstance().getFilter())
                            .hint(MapmyIndiaPlaceWidgetSetting.getInstance().getHint())
                            .saveHistory(MapmyIndiaPlaceWidgetSetting.getInstance().isEnableHistory())
                            .enableTextSearch(MapmyIndiaPlaceWidgetSetting.getInstance().isEnableTextSearch())
                            .pod(MapmyIndiaPlaceWidgetSetting.getInstance().getPod())
                            .attributionHorizontalAlignment(MapmyIndiaPlaceWidgetSetting.getInstance().getSignatureVertical())
                            .attributionVerticalAlignment(MapmyIndiaPlaceWidgetSetting.getInstance().getSignatureHorizontal())
                            .logoSize(MapmyIndiaPlaceWidgetSetting.getInstance().getLogoSize())
                            .backgroundColor(getResources().getColor(MapmyIndiaPlaceWidgetSetting.getInstance().getBackgroundColor()))
                            .build(PlaceOptions.MODE_CARDS);

                    PlaceAutocomplete.IntentBuilder builder = new PlaceAutocomplete.IntentBuilder();
                    if (!MapmyIndiaPlaceWidgetSetting.getInstance().isDefault()) {
                        builder.placeOptions(placeOptions);
                    } else {
                        builder.placeOptions(PlaceOptions.builder().build(PlaceOptions.MODE_CARDS));
                    }
                    Intent placeAutocomplete = builder.build(CardModeActivity.this);
                    startActivityForResult(placeAutocomplete, 101);
                } else {
                    Toast.makeText(CardModeActivity.this, "Please wait map is loading", Toast.LENGTH_SHORT).show();
                }
            }


        });

    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;



        mapmyIndiaMap.setMinZoomPreference(4);
        mapmyIndiaMap.setMaxZoomPreference(18);
        mapmyIndiaMap.setCameraPosition(new CameraPosition.Builder().target(new LatLng(28, 77)).zoom(4).build());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    ELocation eLocation = new Gson().fromJson(data.getStringExtra(PlaceConstants.RETURNING_ELOCATION_DATA), ELocation.class);
                    if (mapmyIndiaMap != null) {
                        textView.setText(eLocation.placeName);
                        mapmyIndiaMap.clear();
                        LatLng latLng = new LatLng(Double.parseDouble(eLocation.latitude), Double.parseDouble(eLocation.longitude));
                        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                        mapmyIndiaMap.addMarker(new MarkerOptions().position(latLng).title(eLocation.placeName).snippet(eLocation.placeAddress));

                    }
                }
            }
        }
    }

    @Override
    public void onMapError(int i, String s) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
