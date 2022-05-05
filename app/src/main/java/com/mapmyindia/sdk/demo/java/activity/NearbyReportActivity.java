package com.mapmyindia.sdk.demo.java.activity;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityNearbyReportBinding;
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog;
import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.annotations.MarkerOptions;
import com.mapmyindia.sdk.maps.camera.CameraELocUpdateFactory;
import com.mapmyindia.sdk.maps.camera.CameraPosition;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment;
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceSelectionListener;
import com.mmi.services.api.OnResponseCallback;
import com.mmi.services.api.autosuggest.model.ELocation;
import com.mmi.services.api.event.nearby.MapmyIndiaNearbyReport;
import com.mmi.services.api.event.nearby.MapmyIndiaNearbyReportManager;
import com.mmi.services.api.event.nearby.model.NearbyReport;
import com.mmi.services.api.event.nearby.model.NearbyReportResponse;

public class NearbyReportActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityNearbyReportBinding mBinding;
    private MapmyIndiaMap mapmyIndiaMap;
    private TransparentProgressDialog transparentProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_nearby_report);
        transparentProgressDialog = new TransparentProgressDialog(this, R.drawable.circle_loader, "");

        mBinding.mapView.onCreate(savedInstanceState);
        mBinding.mapView.getMapAsync(this);
        mBinding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapmyIndiaMap != null) {
                    PlaceOptions placeOptions = PlaceOptions.builder()
                            .backgroundColor(Color.WHITE)
                            .build();
                    PlaceAutocompleteFragment placeAutocompleteFragment = PlaceAutocompleteFragment.newInstance(placeOptions);
                    placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                        @Override
                        public void onPlaceSelected(ELocation eLocation) {
                            if(mapmyIndiaMap != null) {
                                if (eLocation.latitude != null && eLocation.longitude != null) {
                                    mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(eLocation.latitude), Double.parseDouble(eLocation.longitude)), 14));
                                } else {
                                    mapmyIndiaMap.animateCamera(CameraELocUpdateFactory.newELocZoom(eLocation.getPlaceId(), 14));
                                }
                            }
                            getSupportFragmentManager().popBackStack(PlaceAutocompleteFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }

                        @Override
                        public void onCancel() {
                            getSupportFragmentManager().popBackStack(PlaceAutocompleteFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    });
                    getSupportFragmentManager().beginTransaction().add(mBinding.fragmentContainer.getId(), placeAutocompleteFragment, PlaceAutocompleteFragment.class.getSimpleName())
                            .addToBackStack(PlaceAutocompleteFragment.class.getSimpleName())
                            .commit();
                }
            }
        });
        mBinding.tvNearbyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int top = mBinding.selectionBox.getTop() - mBinding.mapView.getTop();
                int left = mBinding.selectionBox.getLeft() - mBinding.mapView.getLeft();
                int bottom = top + mBinding.selectionBox.getHeight();
                int right = left + mBinding.selectionBox.getWidth();
                if (mapmyIndiaMap != null) {
                    LatLng topLeft = mapmyIndiaMap.getProjection().fromScreenLocation(new PointF(left, top));
                    LatLng rightBottom = mapmyIndiaMap.getProjection().fromScreenLocation(new PointF(right, bottom));

                    mapmyIndiaMap.clear();
                    MapmyIndiaNearbyReport mapmyIndiaNearbyReport = MapmyIndiaNearbyReport.builder()
                            .topLeft(Point.fromLngLat(topLeft.getLongitude(), topLeft.getLatitude()))
                            .bottomRight(Point.fromLngLat(rightBottom.getLongitude(), rightBottom.getLatitude()))
                            .build();
                    progressDialogShow();
                    MapmyIndiaNearbyReportManager.newInstance(mapmyIndiaNearbyReport).call(new OnResponseCallback<NearbyReportResponse>() {
                        @Override
                        public void onSuccess(NearbyReportResponse nearbyReportResponse) {
                            if (nearbyReportResponse != null && nearbyReportResponse.getReports() != null && nearbyReportResponse.getReports().size() > 0) {
                                for(NearbyReport nearbyReport: nearbyReportResponse.getReports()) {
                                    if(mapmyIndiaMap != null) {
                                        mapmyIndiaMap.addMarker(new MarkerOptions().position(new LatLng(nearbyReport.getLatitude(), nearbyReport.getLongitude())).title(nearbyReport.getCategory()));
                                    }
                                }
                            } else {
                                Toast.makeText(NearbyReportActivity.this, "No result found", Toast.LENGTH_SHORT).show();
                            }
                            progressDialogHide();
                        }

                        @Override
                        public void onError(int i, String s) {
                            progressDialogHide();
                            Toast.makeText(NearbyReportActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    protected void progressDialogShow() {
        transparentProgressDialog.show();
    }

    protected void progressDialogHide() {
        transparentProgressDialog.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mBinding.mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBinding.mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.mapView.onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull MapmyIndiaMap mapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        if (mapmyIndiaMap.getUiSettings() != null) {
            mapmyIndiaMap.getUiSettings().setLogoMargins(0, 0, 0, 100);
        }
        mapmyIndiaMap.setCameraPosition(new CameraPosition.Builder().target(new LatLng(28.550716, 77.268928)).zoom(12).build());
        mBinding.tvNearbyReport.setVisibility(View.VISIBLE);
        mBinding.selectionBox.setVisibility(View.VISIBLE);
        mBinding.search.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapError(int i, String s) {

    }
}