package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.widget.Toast;

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
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.SuggestedSearchSelectionListener;
import com.mmi.services.api.OnResponseCallback;
import com.mmi.services.api.autosuggest.model.ELocation;
import com.mmi.services.api.autosuggest.model.SuggestedSearchAtlas;
import com.mmi.services.api.hateaosnearby.MapmyIndiaHateosNearby;
import com.mmi.services.api.hateaosnearby.MapmyIndiaHateosNearbyManager;
import com.mmi.services.api.nearby.model.NearbyAtlasResponse;
import com.mmi.services.api.nearby.model.NearbyAtlasResult;

import java.util.ArrayList;

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
                    .bridge(MapmyIndiaPlaceWidgetSetting.getInstance().isEnableBridge())
                    .hyperLocal(MapmyIndiaPlaceWidgetSetting.getInstance().isEnableHyperLocal())
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

        placeAutocompleteFragment.setSuggestedSearchSelectionListener(new SuggestedSearchSelectionListener() {
            @Override
            public void onSuggestedSearchSelected(SuggestedSearchAtlas suggestedSearchAtlas) {
                callHateOs(suggestedSearchAtlas.hyperLink);
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, placeAutocompleteFragment, PlaceAutocompleteFragment.class.getSimpleName())
                .commit();
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
                    Toast.makeText(CardModeFragmentAutocompleteActivity.this, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CardModeFragmentAutocompleteActivity.this, s, Toast.LENGTH_SHORT).show();

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
