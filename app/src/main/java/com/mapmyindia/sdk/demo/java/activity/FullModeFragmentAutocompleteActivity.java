package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.settings.MapmyIndiaPlaceWidgetSetting;
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment;
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceSelectionListener;
import com.mmi.services.api.autosuggest.model.ELocation;

public class FullModeFragmentAutocompleteActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private MapboxMap mapmyIndiaMap;
    private TextView search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_mode_fragment);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapmyIndiaMap != null) {
                    PlaceOptions placeOptions = PlaceOptions.builder()
                            .location(MapmyIndiaPlaceWidgetSetting.getInstance().getLocation())
                            .filter(MapmyIndiaPlaceWidgetSetting.getInstance().getFilter())
                            .hint(MapmyIndiaPlaceWidgetSetting.getInstance().getHint())
                            .enableTextSearch(MapmyIndiaPlaceWidgetSetting.getInstance().isEnableTextSearch())
                            .pod(MapmyIndiaPlaceWidgetSetting.getInstance().getPod())
                            .saveHistory(MapmyIndiaPlaceWidgetSetting.getInstance().isEnableHistory())
                            .attributionHorizontalAlignment(MapmyIndiaPlaceWidgetSetting.getInstance().getSignatureVertical())
                            .attributionVerticalAlignment(MapmyIndiaPlaceWidgetSetting.getInstance().getSignatureHorizontal())
                            .logoSize(MapmyIndiaPlaceWidgetSetting.getInstance().getLogoSize())
                            .backgroundColor(getResources().getColor(MapmyIndiaPlaceWidgetSetting.getInstance().getBackgroundColor()))
                            .toolbarColor(getResources().getColor(MapmyIndiaPlaceWidgetSetting.getInstance().getToolbarColor()))
                            .build();
                    PlaceAutocompleteFragment placeAutocompleteFragment;
                    if (MapmyIndiaPlaceWidgetSetting.getInstance().isDefault()) {
                        placeAutocompleteFragment = PlaceAutocompleteFragment.newInstance();
                    } else {
                        placeAutocompleteFragment = PlaceAutocompleteFragment.newInstance(placeOptions);
                    }
                    placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                        @Override
                        public void onPlaceSelected(ELocation eLocation) {

                            search.setText(eLocation.placeName);
                            if (mapmyIndiaMap != null) {
                                mapmyIndiaMap.clear();
                                if (eLocation.latitude != null && eLocation.longitude != null) {
                                    LatLng latLng = new LatLng(Double.parseDouble(eLocation.latitude), Double.parseDouble(eLocation.longitude));
                                    mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                                    mapmyIndiaMap.addMarker(new MarkerOptions().position(latLng).title(eLocation.placeName).snippet(eLocation.placeAddress));
                                }
                                getSupportFragmentManager().popBackStack(PlaceAutocompleteFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }
                        }

                        @Override
                        public void onCancel() {
                            getSupportFragmentManager().popBackStack(PlaceAutocompleteFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    });


                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, placeAutocompleteFragment, PlaceAutocompleteFragment.class.getSimpleName())
                            .addToBackStack(PlaceAutocompleteFragment.class.getSimpleName())
                            .commit();
                } else {
                    Toast.makeText(FullModeFragmentAutocompleteActivity.this, "Please wait map is loading", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;

        mapmyIndiaMap.setPadding(20, 20, 20, 20);


        mapmyIndiaMap.setMinZoomPreference(4);
        mapmyIndiaMap.setMaxZoomPreference(18);
        mapmyIndiaMap.setCameraPosition(new CameraPosition.Builder().target(new LatLng(28, 77)).zoom(4).build());


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
