package com.mapmyindia.sdk.demo.java.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.adapter.AutoSuggestSearchesAdapter;
import com.mapmyindia.sdk.demo.java.adapter.NearByAdapter;
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog;
import com.mmi.services.api.autosuggest.MapmyIndiaAutoSuggest;
import com.mmi.services.api.autosuggest.model.AutoSuggestAtlasResponse;
import com.mmi.services.api.autosuggest.model.SuggestedSearchAtlas;
import com.mmi.services.api.hateaosnearby.MapmyIndiaHateosNearby;
import com.mmi.services.api.nearby.model.NearbyAtlasResponse;
import com.mmi.services.api.nearby.model.NearbyAtlasResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HateOsNearbyActivity extends AppCompatActivity implements OnMapReadyCallback, TextView.OnEditorActionListener {
    private MapboxMap mapmyIndiaMap;
    private EditText autoSuggestText;
    private MapView mapView;
    private TransparentProgressDialog transparentProgressDialog;
    private RecyclerView nearbyRecyclerView, autoSuggestRecyclerView;;
    private int count = 0;
    private FloatingActionButton floatingActionButton;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hate_os_nearby);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        initReferences();
        initListeners();





        floatingActionButton = findViewById(R.id.marker_list);
        floatingActionButton.setImageResource(R.drawable.location_pointer);
        count = 0;
        floatingActionButton.setOnClickListener(v -> {
            if (count == 0) {
                count = 1;
                floatingActionButton.setImageResource(R.drawable.listing_option);
                nearbyRecyclerView.setVisibility(View.GONE);
            } else {
                count = 0;
                floatingActionButton.setImageResource(R.drawable.location_pointer);
                nearbyRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        transparentProgressDialog = new TransparentProgressDialog(this, R.drawable.circle_loader, "");

    }

    private void initListeners() {
        autoSuggestText.setOnEditorActionListener(this);
    }

    private void initReferences() {

        autoSuggestText = findViewById(R.id.auto_suggest);
        nearbyRecyclerView = findViewById(R.id.nearByRecyclerview);
        autoSuggestRecyclerView = findViewById(R.id.auto_suggest_recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        nearbyRecyclerView.setLayoutManager(mLayoutManager);
        autoSuggestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        autoSuggestRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(MapboxMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;


        mapmyIndiaMap.setPadding(20, 20, 20, 20);


        mapmyIndiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                22.553147478403194,
                77.23388671875), 4));
    }

    @Override
    public void onMapError(int i, String s) {
    }


    protected void progressDialogShow() {
        transparentProgressDialog.show();
    }

    protected void progressDialogHide() {
        transparentProgressDialog.dismiss();
    }

    private void callAutoSuggestApi(String searchString) {
       progressDialogShow();
        MapmyIndiaAutoSuggest.builder()
                .query(searchString)
                .bridge(true)
                .build()
                .enqueueCall(new Callback<AutoSuggestAtlasResponse>() {
                    @Override
                    public void onResponse(Call<AutoSuggestAtlasResponse> call, Response<AutoSuggestAtlasResponse> response) {

                        if (response.code() == 200) {

                            if (response.body() != null) {
                                ArrayList<SuggestedSearchAtlas> suggestedSearches = response.body().getSuggestedSearches();
                                       if (suggestedSearches.size()>0){

                                           autoSuggestRecyclerView.setVisibility(View.VISIBLE);
                                           AutoSuggestSearchesAdapter autoSuggestAdapter = new AutoSuggestSearchesAdapter(suggestedSearches, hyperlink -> {
                                               callHateOs(hyperlink);
                                               autoSuggestRecyclerView.setVisibility(View.GONE);
                                           });
                                           autoSuggestRecyclerView.setAdapter(autoSuggestAdapter);
                                       }

                                       else {
                                           Toast.makeText(HateOsNearbyActivity.this, "No hyperlinks found...", Toast.LENGTH_SHORT).show(); }
                            } else {
                                showToast("Not able to get value, Try again.");
                            }
                        }else {

                            showToast(response.message());
                        }
                        progressDialogHide();
                    }

                    @Override
                    public void onFailure(Call<AutoSuggestAtlasResponse> call, Throwable t) {
                        showToast(t.toString());
                        progressDialogHide();
                    }
                });

    }


private void callHateOs(String hyperlink){
    MapmyIndiaHateosNearby.builder()
            .hyperlink(hyperlink)
            .build()
            .enqueueCall(new Callback<NearbyAtlasResponse>() {
                @Override
                public void onResponse(Call<NearbyAtlasResponse> call, Response<NearbyAtlasResponse> response) {

                    if (response.code() == 200) {
                        if (response.body() != null) {
                            ArrayList<NearbyAtlasResult> nearByList = response.body().getSuggestedLocations();
                            if (nearByList.size() > 0) {
                                addMarker(nearByList);
                            }
                        } else {
                            Toast.makeText(HateOsNearbyActivity.this, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(HateOsNearbyActivity.this, response.message(), Toast.LENGTH_LONG).show();
                    }

                    progressDialogHide();
                }

                @Override
                public void onFailure(Call<NearbyAtlasResponse> call, Throwable t) {
                    showToast(t.toString());
                    progressDialogHide();
                }
            });
}

    private void addMarker(ArrayList<NearbyAtlasResult> nearByList) {
        mapmyIndiaMap.clear();
        for (NearbyAtlasResult marker : nearByList) {
            mapmyIndiaMap.addMarker(new MarkerOptions().position(new LatLng(marker.getLatitude(), marker.getLongitude())).title(marker.getPlaceName()));
        }

        nearbyRecyclerView.setAdapter(new NearByAdapter(nearByList));
    }


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_SEARCH){
           callAutoSuggestApi(v.getText().toString());
            autoSuggestText.clearFocus();
            InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(autoSuggestText.getWindowToken(), 0);
            return true;
        }
        return false;
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
