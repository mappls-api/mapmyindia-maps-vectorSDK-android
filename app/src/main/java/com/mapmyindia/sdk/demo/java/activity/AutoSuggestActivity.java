package com.mapmyindia.sdk.demo.java.activity;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.adapter.AutoSuggestAdapter;
import com.mapmyindia.sdk.demo.java.utils.CheckInternet;
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.SupportMapFragment;
import com.mapmyindia.sdk.maps.annotations.MarkerOptions;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mmi.services.api.OnResponseCallback;
import com.mmi.services.api.autosuggest.MapmyIndiaAutoSuggest;
import com.mmi.services.api.autosuggest.MapmyIndiaAutosuggestManager;
import com.mmi.services.api.autosuggest.model.AutoSuggestAtlasResponse;
import com.mmi.services.api.autosuggest.model.ELocation;
import com.mmi.services.api.textsearch.MapmyIndiaTextSearch;
import com.mmi.services.api.textsearch.MapmyIndiaTextSearchManager;

import java.util.ArrayList;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class AutoSuggestActivity extends AppCompatActivity implements OnMapReadyCallback, TextWatcher, TextView.OnEditorActionListener {

    private MapmyIndiaMap mapmyIndiaMap;
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
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);

        initReferences();
        initListeners();
    }

    private void initListeners() {
        autoSuggestText.addTextChangedListener(this);
        autoSuggestText.setOnEditorActionListener(this);
    }

    private void initReferences() {
        autoSuggestText = findViewById(R.id.auto_suggest);
        recyclerView = findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(AutoSuggestActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setVisibility(View.GONE);


        transparentProgressDialog = new TransparentProgressDialog(this, R.drawable.circle_loader, "");
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;


        mapmyIndiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                22.553147478403194,
                77.23388671875), 4));
    }

    @Override
    public void onMapError(int i, String s) {
    }

    public void selectedPlace(ELocation eLocation) {
        String add = "Latitude: " + eLocation.latitude + " longitude: " + eLocation.longitude;
        addMarker(Double.parseDouble(eLocation.latitude), Double.parseDouble(eLocation.longitude));
        showToast(add);
    }


    private void addMarker(double latitude, double longitude) {
        mapmyIndiaMap.clear();
        mapmyIndiaMap.addMarker(new MarkerOptions().position(new LatLng(
                latitude, longitude)));
    }


    private void callAutoSuggestApi(String searchString) {
        MapmyIndiaAutoSuggest mapmyIndiaAutoSuggest = MapmyIndiaAutoSuggest.builder()
                .query(searchString)
                .build();
        MapmyIndiaAutosuggestManager.newInstance(mapmyIndiaAutoSuggest).call(new OnResponseCallback<AutoSuggestAtlasResponse>() {
            @Override
            public void onSuccess(AutoSuggestAtlasResponse autoSuggestAtlasResponse) {
                if (autoSuggestAtlasResponse != null) {
                                ArrayList<ELocation> suggestedList = autoSuggestAtlasResponse.getSuggestedLocations();
                    if (suggestedList.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        AutoSuggestAdapter autoSuggestAdapter = new AutoSuggestAdapter(suggestedList, eLocation -> {
                            selectedPlace(eLocation);
                            recyclerView.setVisibility(View.GONE);
                        });
                        recyclerView.setAdapter(autoSuggestAdapter);
                    }
                } else {
                    showToast("Not able to get value, Try again.");
                }
            }

            @Override
            public void onError(int i, String s) {
                showToast(s);
            }
        });

    }


    private void callTextSearchApi(String searchString) {
        MapmyIndiaTextSearch mapmyIndiaTextSearch = MapmyIndiaTextSearch.builder()
                .query(searchString)
                .build();
        MapmyIndiaTextSearchManager.newInstance(mapmyIndiaTextSearch).call(new OnResponseCallback<AutoSuggestAtlasResponse>() {
            @Override
            public void onSuccess(AutoSuggestAtlasResponse autoSuggestAtlasResponse) {
                if (autoSuggestAtlasResponse != null) {
                    ArrayList<ELocation> suggestedList = autoSuggestAtlasResponse.getSuggestedLocations();
                    if (suggestedList.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        AutoSuggestAdapter autoSuggestAdapter = new AutoSuggestAdapter(suggestedList, eLocation -> {
                            selectedPlace(eLocation);
                            recyclerView.setVisibility(View.GONE);
                        });
                        recyclerView.setAdapter(autoSuggestAdapter);
                    }
                } else {
                    showToast("Not able to get value, Try again.");
                }
//
            }

            @Override
            public void onError(int i, String s) {
                showToast(s);
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


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            callTextSearchApi(v.getText().toString());
            autoSuggestText.clearFocus();
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(autoSuggestText.getWindowToken(), 0);
            return true;
        }
        return false;
    }
}
