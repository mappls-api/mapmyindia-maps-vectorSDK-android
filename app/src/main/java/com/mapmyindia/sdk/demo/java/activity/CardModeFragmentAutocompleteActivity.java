package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.settings.MapmyIndiaPlaceWidgetSetting;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.annotations.MarkerOptions;
import com.mapmyindia.sdk.maps.camera.CameraPosition;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment;
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceSelectionListener;
import com.mmi.services.api.autosuggest.model.ELocation;

public class CardModeFragmentAutocompleteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapmyIndiaMap mapmyIndiaMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


    }


    private void callAutoComplete() {
        PlaceOptions placeOptions;
        if (MapmyIndiaPlaceWidgetSetting.getInstance().isDefault()) {
            placeOptions = PlaceOptions.builder().build(PlaceOptions.MODE_CARDS);
        } else {

            placeOptions = PlaceOptions.builder()
                    .location(MapmyIndiaPlaceWidgetSetting.getInstance().getLocation())
                    .filter(MapmyIndiaPlaceWidgetSetting.getInstance().getFilter())
                    .saveHistory(MapmyIndiaPlaceWidgetSetting.getInstance().isEnableHistory())
                    .enableTextSearch(MapmyIndiaPlaceWidgetSetting.getInstance().isEnableTextSearch())
                    .hint(MapmyIndiaPlaceWidgetSetting.getInstance().getHint())
                    .pod(MapmyIndiaPlaceWidgetSetting.getInstance().getPod())
                    .attributionHorizontalAlignment(MapmyIndiaPlaceWidgetSetting.getInstance().getSignatureVertical())
                    .attributionVerticalAlignment(MapmyIndiaPlaceWidgetSetting.getInstance().getSignatureHorizontal())
                    .logoSize(MapmyIndiaPlaceWidgetSetting.getInstance().getLogoSize())
                    .backgroundColor(getResources().getColor(MapmyIndiaPlaceWidgetSetting.getInstance().getBackgroundColor()))
                    .build(PlaceOptions.MODE_CARDS);
        }
        PlaceAutocompleteFragment placeAutocompleteFragment = PlaceAutocompleteFragment.newInstance(placeOptions);
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(ELocation eLocation) {

                if (mapmyIndiaMap != null) {
                    mapmyIndiaMap.clear();
                    LatLng latLng = new LatLng(Double.parseDouble(eLocation.latitude), Double.parseDouble(eLocation.longitude));
                    mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                    mapmyIndiaMap.addMarker(new MarkerOptions().position(latLng).title(eLocation.placeName).snippet(eLocation.placeAddress));
                }
            }

            @Override
            public void onCancel() {

            }
        });


        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, placeAutocompleteFragment, PlaceAutocompleteFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;


        mapmyIndiaMap.setMinZoomPreference(4);
        mapmyIndiaMap.setMaxZoomPreference(18);
        mapmyIndiaMap.setCameraPosition(new CameraPosition.Builder().target(new LatLng(28, 77)).zoom(4).build());
        callAutoComplete();


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
