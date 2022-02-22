package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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
import com.mmi.services.api.OnResponseCallback;
import com.mmi.services.api.autosuggest.model.ELocation;
import com.mmi.services.api.hateaosnearby.MapmyIndiaHateosNearby;
import com.mmi.services.api.hateaosnearby.MapmyIndiaHateosNearbyManager;
import com.mmi.services.api.nearby.model.NearbyAtlasResponse;
import com.mmi.services.api.nearby.model.NearbyAtlasResult;

import java.util.ArrayList;

public class FullModeFragmentAutocompleteActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private MapmyIndiaMap mapmyIndiaMap;
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
                            .hyperLocal(MapmyIndiaPlaceWidgetSetting.getInstance().isEnableHyperLocal())
                            .bridge(MapmyIndiaPlaceWidgetSetting.getInstance().isEnableBridge())
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
                    placeAutocompleteFragment.setSuggestedSearchSelectionListener(suggestedSearchAtlas -> {
                        callHateOs(suggestedSearchAtlas.hyperLink);
                        getSupportFragmentManager().popBackStack(PlaceAutocompleteFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

    private void callHateOs(String hyperlink) {
        MapmyIndiaHateosNearby hateosNearby = MapmyIndiaHateosNearby.builder()
                .hyperlink(hyperlink)
                .build();
        MapmyIndiaHateosNearbyManager.newInstance(hateosNearby).call(new OnResponseCallback<NearbyAtlasResponse>() {
            @Override
            public void onSuccess(NearbyAtlasResponse nearbyAtlasResponse) {
                if (nearbyAtlasResponse != null) {
                    ArrayList<NearbyAtlasResult> nearByList = nearbyAtlasResponse.getSuggestedLocations();
                    if (nearByList.size() > 0) {
                        addMarker(nearByList);
                    }
                } else {
                    Toast.makeText(FullModeFragmentAutocompleteActivity.this, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(FullModeFragmentAutocompleteActivity.this, s, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void addMarker(ArrayList<NearbyAtlasResult> nearByList) {
        mapmyIndiaMap.clear();
        for (NearbyAtlasResult marker : nearByList) {
            if (marker.getLatitude() != null && marker.getLongitude() != null) {
                mapmyIndiaMap.addMarker(new MarkerOptions().position(new LatLng(marker.getLatitude(), marker.getLongitude())).title(marker.getPlaceName()));
            } else {
                mapmyIndiaMap.addMarker(new MarkerOptions().eLoc(marker.eLoc).title(marker.getPlaceName()));
            }
        }

    }

    @Override
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;


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
