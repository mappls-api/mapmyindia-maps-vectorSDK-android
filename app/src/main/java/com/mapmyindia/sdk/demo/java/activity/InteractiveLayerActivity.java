package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.adapter.InteractiveLayerAdapter;
import com.mapmyindia.sdk.maps.InteractiveLayer;
import com.mapmyindia.sdk.maps.MapView;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.covid.InteractiveItemDetails;
import com.mapmyindia.sdk.maps.geometry.LatLng;

import java.util.List;


public class InteractiveLayerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private BottomSheetBehavior bottomSheetBehavior;
    private MapmyIndiaMap mapmyIndiaMap;
    private RecyclerView recyclerView;
    private InteractiveLayerAdapter adapter;
    private SwitchCompat toggleInfoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interactive_layer);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        View view = findViewById(R.id.bottomSheet);
        toggleInfoWindow = findViewById(R.id.toggle_info_window);
        toggleInfoWindow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mapmyIndiaMap != null) {
                    mapmyIndiaMap.showInteractiveLayerInfoWindow(isChecked);
                }
            }
        });
        bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(200);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        recyclerView = findViewById(R.id.rv_interactive_layer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InteractiveLayerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnLayerSelected(new InteractiveLayerAdapter.OnLayerSelected() {
            @Override
            public void onLayerSelected(InteractiveLayer interactiveLayer, boolean isSelected) {
                if(isSelected) {
                    mapmyIndiaMap.showInteractiveLayer(interactiveLayer);
                } else {
                    mapmyIndiaMap.hideInteractiveLayer(interactiveLayer);
                }
            }
        });


    }

    @Override
    public void onMapReady(MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28, 77), 5));
        if (mapmyIndiaMap.getUiSettings() != null) {
            mapmyIndiaMap.getUiSettings().setLogoMargins(0, 0, 0, 200);
        }

        mapmyIndiaMap.setOnInteractiveLayerClickListener(new MapmyIndiaMap.OnInteractiveLayerClickListener() {
            @Override
            public void onInteractiveLayerClicked(InteractiveItemDetails interactiveItemDetails) {

            }
        });
        mapmyIndiaMap.showInteractiveLayerInfoWindow(toggleInfoWindow.isChecked());

        mapmyIndiaMap.getInteractiveLayer(new MapmyIndiaMap.InteractiveLayerLoadingListener() {
            @Override
            public void onLayersLoaded(List<InteractiveLayer> list) {
                adapter.setCovidLayers(list);
            }
        });

    }

    @Override
    public void onMapError(int i, String s) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapView != null) {
            mapView.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null)
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
