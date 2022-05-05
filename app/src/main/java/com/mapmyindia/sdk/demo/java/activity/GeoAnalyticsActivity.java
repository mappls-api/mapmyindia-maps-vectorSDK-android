package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityGeoAnalyticsBinding;
import com.mapmyindia.sdk.demo.java.adapter.GeoAnalyticsAdapter;
import com.mapmyindia.sdk.demo.java.model.GeoAnalyticsModel;
import com.mapmyindia.sdk.geoanalytics.GeoAnalyticsAppearanceOption;
import com.mapmyindia.sdk.geoanalytics.MapmyIndiaGeoAnalyticsPlugin;
import com.mapmyindia.sdk.geoanalytics.MapmyIndiaGeoAnalyticsRequest;
import com.mapmyindia.sdk.geoanalytics.MapmyIndiaGeoAnalyticsType;
import com.mapmyindia.sdk.geoanalytics.listing.MapmyIndiaGeoAnalyticsList;
import com.mapmyindia.sdk.geoanalytics.listing.MapmyIndiaGeoAnalyticsListManager;
import com.mapmyindia.sdk.geoanalytics.listing.model.GeoAnalyticsListResponse;
import com.mapmyindia.sdk.geoanalytics.listing.model.GeoAnalyticsListResult;
import com.mapmyindia.sdk.geoanalytics.listing.model.GeoAnalyticsValue;
import com.mapmyindia.sdk.maps.MapmyIndiaMap;
import com.mapmyindia.sdk.maps.OnMapReadyCallback;
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory;
import com.mapmyindia.sdk.maps.geometry.LatLng;
import com.mapmyindia.sdk.maps.geometry.LatLngBounds;
import com.mmi.services.api.OnResponseCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class GeoAnalyticsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityGeoAnalyticsBinding mBinding;
    private List<GeoAnalyticsModel> models = new ArrayList<>();
    private GeoAnalyticsAdapter adapter;
    private BottomSheetBehavior bottomSheetBehavior;
    private MapmyIndiaMap mapmyIndiaMap;
    private MapmyIndiaGeoAnalyticsPlugin geoAnalyticsPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_geo_analytics);
        mBinding.mapView.onCreate(savedInstanceState);
        bottomSheetBehavior = BottomSheetBehavior.from(mBinding.bottomSheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(200);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        mBinding.rvGeoanalytics.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GeoAnalyticsAdapter();
        mBinding.rvGeoanalytics.setAdapter(adapter);
        mBinding.mapView.getMapAsync(this);

        adapter.setOnLayerSelected(new GeoAnalyticsAdapter.OnLayerSelected() {
            @Override
            public void onLayerSelected(GeoAnalyticsModel geoAnalyticsModel, boolean isChecked) {
                if (geoAnalyticsPlugin != null) {
                    if (isChecked) {
                        geoAnalyticsPlugin.showGeoAnalytics(geoAnalyticsModel.getType(), geoAnalyticsModel.getParams());
                    } else {
                        geoAnalyticsPlugin.removeGeoAnalytics(geoAnalyticsModel.getType());
                    }

                }
                if (isChecked) {
                    MapmyIndiaGeoAnalyticsList mapmyIndiaGeoAnalyticsList = MapmyIndiaGeoAnalyticsList.builder()
                            .api(geoAnalyticsModel.getType().getName())
                            .attributes("b_box")
                            .geoBound(geoAnalyticsModel.getGeoBound())
                            .geoBoundType(geoAnalyticsModel.getGeoboundType())
                            .build();
                    MapmyIndiaGeoAnalyticsListManager.newInstance(mapmyIndiaGeoAnalyticsList).call(new OnResponseCallback<GeoAnalyticsListResponse>() {
                        @Override
                        public void onSuccess(GeoAnalyticsListResponse geoAnalyticsListResponse) {
                            GeoAnalyticsListResult result = geoAnalyticsListResponse.getResults();
                            if (result != null && result.getGetAttrValues() != null && result.getGetAttrValues().size() > 0) {
                                List<LatLng> latLngList = new ArrayList<>();
                                for (GeoAnalyticsValue value : result.getGetAttrValues()) {
                                    if (value.getGetAttrValues() != null && value.getGetAttrValues().size() > 0) {
                                        for (Map<String, Object> map : value.getGetAttrValues()) {
                                            String bBox = (String) map.get("b_box");
                                            String truncateBox = bBox.substring(4, bBox.length() - 1);
                                            String start = truncateBox.split(",")[0];
                                            String last = truncateBox.split(",")[1];
                                            latLngList.add(new LatLng(Double.parseDouble(start.split(" ")[1]), Double.parseDouble(start.split(" ")[0])));
                                            latLngList.add(new LatLng(Double.parseDouble(last.split(" ")[1]), Double.parseDouble(last.split(" ")[0])));
                                        }
                                    }
                                }
                                if (!mBinding.mapView.isDestroyed() && mapmyIndiaMap != null) {
                                    mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds.Builder().includes(latLngList).build(), 12));
                                }
                            }
                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });
                }
            }
        });
    }


    private void init() {
        models.add(new GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType.STATE, MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0").geoboundType("stt_nme").geoBound("HARYANA", "UTTAR PRADESH", "ANDHRA PRADESH", "KERALA")
                .propertyNames("stt_nme", "stt_id", "t_p").style(new GeoAnalyticsAppearanceOption().fillColor("42a5f4").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build(), "stt_nme", new String[]{"HARYANA", "UTTAR PRADESH", "ANDHRA PRADESH", "KERALA"}));
        models.add(new GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType.DISTRICT, MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0").geoboundType("stt_nme").geoBound("BIHAR", "UTTARAKHAND")
                .propertyNames("dst_nme", "dst_id", "t_p").style(new GeoAnalyticsAppearanceOption().fillColor("8bc34a").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build(), "stt_nme", new String[]{"BIHAR", "UTTARAKHAND"}));
        models.add(new GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType.SUB_DISTRICT, MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0").geoboundType("stt_nme").geoBound("HIMACHAL PRADESH", "TRIPURA")
                .propertyNames("sdb_nme", "sdb_id", "t_p").style(new GeoAnalyticsAppearanceOption().fillColor("ffa000").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build(),"stt_nme", new String[]{"HIMACHAL PRADESH", "TRIPURA"}));
        models.add(new GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType.WARD, MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0").geoboundType("ward_no").geoBound("0001")
                .propertyNames("ward_no", "t_p").style(new GeoAnalyticsAppearanceOption().fillColor("ff5722").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build(), "ward_no", new String[]{"0001"}));
        models.add(new GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType.LOCALITY, MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0").geoboundType("stt_nme").geoBound("DELHI")
                .propertyNames("loc_nme", "loc_id", "t_p").style(new GeoAnalyticsAppearanceOption().fillColor("00695c").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build(), "stt_nme", new String[]{"DELHI"}));
        models.add(new GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType.PANCHAYAT, MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0").geoboundType("stt_nme").geoBound("ASSAM")
                .propertyNames("pnc_nme", "pnc_id", "t_p").style(new GeoAnalyticsAppearanceOption().fillColor("b71c1c").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build(), "stt_nme", new String[]{"ASSAM"}));
        models.add(new GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType.BLOCK, MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0").geoboundType("stt_nme").geoBound("DELHI")
                .propertyNames("blk_nme", "blk_id", "t_p").style(new GeoAnalyticsAppearanceOption().fillColor("3f51b5").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build(), "stt_nme", new String[]{"DELHI"}));
        models.add(new GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType.PINCODE, MapmyIndiaGeoAnalyticsRequest.builder().geoboundType("stt_nme").geoBound("KARNATAKA")
                .propertyNames("pincode").style(new GeoAnalyticsAppearanceOption().fillColor("00bcd4").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build(), "stt_nme", new String[]{"KARNATAKA"}));
        models.add(new GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType.TOWN, MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0").geoboundType("stt_nme").geoBound("PUNJAB")
                .propertyNames("twn_nme", "twn_id", "t_p").style(new GeoAnalyticsAppearanceOption().fillColor("9ccc65").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build(), "stt_nme", new String[]{"PUNJAB"}));
        models.add(new GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType.CITY, MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0").geoboundType("stt_nme").geoBound("TAMIL NADU")
                .propertyNames("city_nme", "city_id", "t_p").style(new GeoAnalyticsAppearanceOption().fillColor("78909c").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build(), "stt_nme", new String[]{"TAMIL NADU"}));
        models.add(new GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType.VILLAGE, MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0").geoboundType("stt_nme").geoBound("GOA")
                .propertyNames("vil_nme", "id", "t_p").style(new GeoAnalyticsAppearanceOption().fillColor("f06292").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build(), "stt_nme", new String[]{"GOA"}));
        models.add(new GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType.SUB_LOCALITY, MapmyIndiaGeoAnalyticsRequest.builder().geoboundType("stt_nme").geoBound("UTTAR PRADESH", "BIHAR")
                .propertyNames("subl_nme", "subl_id").style(new GeoAnalyticsAppearanceOption().fillColor("f06292").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build(), "stt_nme", new String[]{"UTTAR PRADESH", "BIHAR"}));
        models.add(new GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType.SUB_SUB_LOCALITY, MapmyIndiaGeoAnalyticsRequest.builder().geoboundType("stt_nme").geoBound("UTTAR PRADESH", "BIHAR")
                .propertyNames("sslc_nme", "sslc_id").style(new GeoAnalyticsAppearanceOption().fillColor("f06292").fillOpacity(0.5).strokeColor("000000").labelSize(10).strokeWidth(0).labelColor("000000")).build(), "stt_nme", new String[]{"UTTAR PRADESH", "BIHAR"}));

        if (adapter != null) {
            adapter.setGeoAnalyticsModels(models);
        }
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
        geoAnalyticsPlugin = new MapmyIndiaGeoAnalyticsPlugin(mBinding.mapView, mapmyIndiaMap);
        if (mapmyIndiaMap.getUiSettings() != null) {
            mapmyIndiaMap.getUiSettings().setLogoMargins(0, 0, 0, 200);
        }
        init();
    }

    @Override
    public void onMapError(int i, String s) {
        Timber.e(i + "-----" + s);
    }
}