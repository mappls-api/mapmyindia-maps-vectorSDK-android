package com.mmi.sdk.demo.java.activity;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mmi.sdk.demo.R;
import com.mmi.sdk.demo.java.adapter.AutoSuggestAdapter;
import com.mmi.sdk.demo.java.utils.CheckInternet;
import com.mmi.sdk.demo.java.utils.TransparentProgressDialog;
import com.mmi.services.api.auth.MapmyIndiaAuthentication;
import com.mmi.services.api.auth.model.AtlasAuthToken;
import com.mmi.services.api.autosuggest.MapmyIndiaAutoSuggest;
import com.mmi.services.api.autosuggest.model.AutoSuggestAtlasResponse;
import com.mmi.services.api.autosuggest.model.ELocation;
import com.mmi.services.api.geocoding.GeoCode;
import com.mmi.services.api.geocoding.GeoCodeResponse;
import com.mmi.services.api.geocoding.MapmyIndiaGeoCoding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class AutoSuggestActivity extends AppCompatActivity implements OnMapReadyCallback, TextWatcher {

    private MapboxMap mapboxMap;
    private EditText autoSuggestText;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private TransparentProgressDialog transparentProgressDialog;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autosuggest_activity);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapBoxId);
        mapFragment.getMapAsync(this);

        initReferences();
        initListeners();
    }

    private void initListeners() {
        autoSuggestText.addTextChangedListener(this);
    }

    private void initReferences() {
        autoSuggestText = findViewById(R.id.auto_suggest);
        recyclerView = findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(AutoSuggestActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setVisibility(View.GONE);

        if (CheckInternet.isNetworkAvailable(AutoSuggestActivity.this)) {
            getAuthToken();
        } else {
            showToast(getString(R.string.pleaseCheckInternetConnection));
        }

        transparentProgressDialog = new TransparentProgressDialog(this, R.drawable.circle_loader, "");
        handler = new Handler();
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setMinZoomPreference(4.5);
        mapboxMap.setMaxZoomPreference(18.5);


        mapboxMap.setPadding(20, 20, 20, 20);


        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                22.553147478403194,
                77.23388671875), 4));
    }

    @Override
    public void onMapError(int i, String s) {
    }

    public void selectedPlaceName(String name) {
        if (CheckInternet.isNetworkAvailable(AutoSuggestActivity.this)) {
            getGeoCode(name);
        } else {
            showToast(getString(R.string.pleaseCheckInternetConnection));
        }
    }

    private void getGeoCode(String geocodeText) {
        show();
        new MapmyIndiaGeoCoding.Builder()
                .setAddress(geocodeText)
                .build().enqueueCall(new Callback<GeoCodeResponse>() {
            @Override
            public void onResponse(Call<GeoCodeResponse> call, Response<GeoCodeResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        List<GeoCode> placesList = response.body().getResults();
                        GeoCode place = placesList.get(0);
                        String add = "Latitude: " + place.latitude + " longitude: " + place.longitude;
                        addMarker(place.latitude, place.longitude);
                        showToast(add);
                    } else {
                        showToast("Not able to get value, Try again.");
                    }
                } else {
                    showToast(response.message());
                }
                hide();
            }

            @Override
            public void onFailure(Call<GeoCodeResponse> call, Throwable t) {
                showToast(t.toString());
                hide();
            }
        });
    }

    private void addMarker(double latitude, double longitude) {
        mapboxMap.clear();
        mapboxMap.addMarker(new MarkerOptions().position(new LatLng(
                latitude, longitude)));
    }


    private void callAutoSuggestApi(String searchString) {
        new MapmyIndiaAutoSuggest.Builder()
                .setQuery(searchString)
                .build()
                .enqueueCall(new Callback<AutoSuggestAtlasResponse>() {
                    @Override
                    public void onResponse(Call<AutoSuggestAtlasResponse> call, Response<AutoSuggestAtlasResponse> response) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                ArrayList<ELocation> suggestedList = response.body().getSuggestedLocations();
                                if (suggestedList.size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    AutoSuggestAdapter autoSuggestAdapter = new AutoSuggestAdapter(suggestedList, name -> {
                                        selectedPlaceName(name);
                                        recyclerView.setVisibility(View.GONE);
                                    });
                                    recyclerView.setAdapter(autoSuggestAdapter);
                                }
                            } else {
                                showToast("Not able to get value, Try again.");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AutoSuggestAtlasResponse> call, Throwable t) {
                        showToast(t.toString());
                    }
                });

    }

    private void show() {
        transparentProgressDialog.show();
    }

    private void hide() {
        transparentProgressDialog.dismiss();
    }

    private void showToast(String msg) {
        Toast.makeText(AutoSuggestActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        handler.postDelayed(() -> {
            recyclerView.setVisibility(View.GONE);
            if (s.length() < 3)
                recyclerView.setAdapter(null);

            if (s != null && s.toString().trim().length() < 2) {
                recyclerView.setAdapter(null);
                return;
            }

            if (s.length() > 2) {
                if (CheckInternet.isNetworkAvailable(AutoSuggestActivity.this)) {
                    callAutoSuggestApi(s.toString());
                } else {
                    showToast(getString(R.string.pleaseCheckInternetConnection));
                }
            }
        }, 300);

    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private void getAuthToken() {

        new MapmyIndiaAuthentication.Builder().build().enqueueCall(new Callback<AtlasAuthToken>() {
            @Override
            public void onResponse(Call<AtlasAuthToken> call, Response<AtlasAuthToken> response) {
            }

            @Override
            public void onFailure(Call<AtlasAuthToken> call, Throwable t) {

            }
        });

    }

}
