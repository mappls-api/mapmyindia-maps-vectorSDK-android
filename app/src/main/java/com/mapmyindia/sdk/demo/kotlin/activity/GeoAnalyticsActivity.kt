package com.mapmyindia.sdk.demo.kotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityGeoAnalyticsBinding
import com.mapmyindia.sdk.demo.kotlin.adapter.GeoAnalyticsAdapter
import com.mapmyindia.sdk.demo.kotlin.model.GeoAnalyticsModel
import com.mapmyindia.sdk.geoanalytics.GeoAnalyticsAppearanceOption
import com.mapmyindia.sdk.geoanalytics.MapmyIndiaGeoAnalyticsPlugin
import com.mapmyindia.sdk.geoanalytics.MapmyIndiaGeoAnalyticsRequest
import com.mapmyindia.sdk.geoanalytics.MapmyIndiaGeoAnalyticsType
import com.mapmyindia.sdk.geoanalytics.listing.MapmyIndiaGeoAnalyticsList
import com.mapmyindia.sdk.geoanalytics.listing.MapmyIndiaGeoAnalyticsListManager
import com.mapmyindia.sdk.geoanalytics.listing.model.GeoAnalyticsListResponse
import com.mapmyindia.sdk.geoanalytics.listing.model.GeoAnalyticsListResult
import com.mapmyindia.sdk.geoanalytics.listing.model.GeoAnalyticsValue
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.geometry.LatLngBounds
import com.mmi.services.api.OnResponseCallback
import timber.log.Timber

class GeoAnalyticsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mBinding: ActivityGeoAnalyticsBinding
    private val models: MutableList<GeoAnalyticsModel> = mutableListOf()
    private var adapter: GeoAnalyticsAdapter? = null
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private var mapmyIndiaMap: MapmyIndiaMap? = null
    private var geoAnalyticsPlugin: MapmyIndiaGeoAnalyticsPlugin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_geo_analytics)
        mBinding.mapView.onCreate(savedInstanceState)
        bottomSheetBehavior = BottomSheetBehavior.from(mBinding.bottomSheet)
        bottomSheetBehavior!!.isHideable = false
        bottomSheetBehavior!!.peekHeight = 200
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED

        mBinding.rvGeoanalytics.layoutManager = LinearLayoutManager(this)
        adapter = GeoAnalyticsAdapter()
        mBinding.rvGeoanalytics.adapter = adapter
        mBinding.mapView.getMapAsync(this)
        adapter?.setOnLayerSelected(object : GeoAnalyticsAdapter.OnLayerSelected {
            override fun onLayerSelected(
                geoAnalyticsModel: GeoAnalyticsModel,
                isChecked: Boolean
            ) {
                if (geoAnalyticsPlugin != null) {
                    if (isChecked) {
                        geoAnalyticsPlugin!!.showGeoAnalytics(
                            geoAnalyticsModel.type,
                            geoAnalyticsModel.params
                        )
                    } else {
                        geoAnalyticsPlugin!!.removeGeoAnalytics(geoAnalyticsModel.type)
                    }
                }
                if (isChecked) {
                    val mapmyIndiaGeoAnalyticsList = MapmyIndiaGeoAnalyticsList.builder()
                        .api(geoAnalyticsModel.type.getName())
                        .attributes("b_box")
                        .geoBound(*geoAnalyticsModel.geoBound)
                        .geoBoundType(geoAnalyticsModel.geoboundType)
                        .build()
                    MapmyIndiaGeoAnalyticsListManager.newInstance(mapmyIndiaGeoAnalyticsList)
                        .call(object : OnResponseCallback<GeoAnalyticsListResponse> {
                            override fun onSuccess(geoAnalyticsListResponse: GeoAnalyticsListResponse) {
                                val result = geoAnalyticsListResponse.results
                                if (result != null && result.getAttrValues != null && result.getAttrValues.size > 0) {
                                    val latLngList: MutableList<LatLng> =
                                        ArrayList()
                                    for (value in result.getAttrValues) {
                                        if (value.getAttrValues != null && value.getAttrValues.size > 0) {
                                            for (map in value.getAttrValues) {
                                                val bBox = map["b_box"] as String?
                                                val truncateBox =
                                                    bBox!!.substring(4, bBox!!.length - 1)
                                                val start =
                                                    truncateBox.split(",".toRegex())
                                                        .toTypedArray()[0]
                                                val last =
                                                    truncateBox.split(",".toRegex())
                                                        .toTypedArray()[1]
                                                latLngList.add(
                                                    LatLng(
                                                        start.split(" ".toRegex())
                                                            .toTypedArray()[1].toDouble(),
                                                        start.split(" ".toRegex())
                                                            .toTypedArray()[0].toDouble()
                                                    )
                                                )
                                                latLngList.add(
                                                    LatLng(
                                                        last.split(" ".toRegex())
                                                            .toTypedArray()[1].toDouble(),
                                                        last.split(" ".toRegex())
                                                            .toTypedArray()[0].toDouble()
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    if (!mBinding.mapView.isDestroyed && mapmyIndiaMap != null) {
                                        mapmyIndiaMap!!.animateCamera(
                                            CameraUpdateFactory.newLatLngBounds(
                                                LatLngBounds.Builder().includes(latLngList).build(),
                                                12
                                            )
                                        )
                                    }
                                }
                            }

                            override fun onError(i: Int, s: String) {}
                        })
                }
            }

        })


    }

    private fun init() {
        models.add(
            GeoAnalyticsModel(
                MapmyIndiaGeoAnalyticsType.STATE,
                MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0")
                    .geoboundType("stt_nme")
                    .geoBound("HARYANA", "UTTAR PRADESH", "ANDHRA PRADESH", "KERALA")
                    .propertyNames("stt_nme", "stt_id", "t_p").style(
                        GeoAnalyticsAppearanceOption().fillColor("42a5f4").fillOpacity(0.5)
                            .strokeColor("000000").labelSize(10).strokeWidth(0.0)
                            .labelColor("000000")
                    ).build(),
                "stt_nme",
                arrayOf("HARYANA", "UTTAR PRADESH", "ANDHRA PRADESH", "KERALA")
            )
        )
        models.add(
            GeoAnalyticsModel(
                MapmyIndiaGeoAnalyticsType.DISTRICT,
                MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0")
                    .geoboundType("stt_nme").geoBound("BIHAR", "UTTARAKHAND")
                    .propertyNames("dst_nme", "dst_id", "t_p").style(
                        GeoAnalyticsAppearanceOption().fillColor("8bc34a").fillOpacity(0.5)
                            .strokeColor("000000").labelSize(10).strokeWidth(0.0)
                            .labelColor("000000")
                    ).build(),
                "stt_nme",
                arrayOf("BIHAR", "UTTARAKHAND")
            )
        )
        models.add(
            GeoAnalyticsModel(
                MapmyIndiaGeoAnalyticsType.SUB_DISTRICT,
                MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0")
                    .geoboundType("stt_nme").geoBound("HIMACHAL PRADESH", "TRIPURA")
                    .propertyNames("sdb_nme", "sdb_id", "t_p").style(
                        GeoAnalyticsAppearanceOption().fillColor("ffa000").fillOpacity(0.5)
                            .strokeColor("000000").labelSize(10).strokeWidth(0.0)
                            .labelColor("000000")
                    ).build(),
                "stt_nme",
                arrayOf("HIMACHAL PRADESH", "TRIPURA")
            )
        )
        models.add(
            GeoAnalyticsModel(
                MapmyIndiaGeoAnalyticsType.WARD,
                MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0")
                    .geoboundType("ward_no").geoBound("0001")
                    .propertyNames("ward_no", "t_p").style(
                        GeoAnalyticsAppearanceOption().fillColor("ff5722").fillOpacity(0.5)
                            .strokeColor("000000").labelSize(10).strokeWidth(0.0)
                            .labelColor("000000")
                    ).build(),
                "ward_no",
                arrayOf("0001")
            )
        )
        models.add(
            GeoAnalyticsModel(
                MapmyIndiaGeoAnalyticsType.LOCALITY,
                MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0")
                    .geoboundType("stt_nme").geoBound("DELHI")
                    .propertyNames("loc_nme", "loc_id", "t_p").style(
                        GeoAnalyticsAppearanceOption().fillColor("00695c").fillOpacity(0.5)
                            .strokeColor("000000").labelSize(10).strokeWidth(0.0)
                            .labelColor("000000")
                    ).build(),
                "stt_nme",
                arrayOf("DELHI")
            )
        )
        models.add(
            GeoAnalyticsModel(
                MapmyIndiaGeoAnalyticsType.PANCHAYAT,
                MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0")
                    .geoboundType("stt_nme").geoBound("ASSAM")
                    .propertyNames("pnc_nme", "pnc_id", "t_p").style(
                        GeoAnalyticsAppearanceOption().fillColor("b71c1c").fillOpacity(0.5)
                            .strokeColor("000000").labelSize(10).strokeWidth(0.0)
                            .labelColor("000000")
                    ).build(),
                "stt_nme",
                arrayOf("ASSAM")
            )
        )
        models.add(
            GeoAnalyticsModel(
                MapmyIndiaGeoAnalyticsType.BLOCK,
                MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0")
                    .geoboundType("stt_nme").geoBound("DELHI")
                    .propertyNames("blk_nme", "blk_id", "t_p").style(
                        GeoAnalyticsAppearanceOption().fillColor("3f51b5").fillOpacity(0.5)
                            .strokeColor("000000").labelSize(10).strokeWidth(0.0)
                            .labelColor("000000")
                    ).build(),
                "stt_nme",
                arrayOf("DELHI")
            )
        )
        models.add(
            GeoAnalyticsModel(
                MapmyIndiaGeoAnalyticsType.PINCODE,
                MapmyIndiaGeoAnalyticsRequest.builder().geoboundType("stt_nme")
                    .geoBound("KARNATAKA")
                    .propertyNames("pincode").style(
                        GeoAnalyticsAppearanceOption().fillColor("00bcd4").fillOpacity(0.5)
                            .strokeColor("000000").labelSize(10).strokeWidth(0.0)
                            .labelColor("000000")
                    ).build(),
                "stt_nme",
                arrayOf("KARNATAKA")
            )
        )
        models.add(
            GeoAnalyticsModel(
                MapmyIndiaGeoAnalyticsType.TOWN,
                MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0")
                    .geoboundType("stt_nme").geoBound("PUNJAB")
                    .propertyNames("twn_nme", "twn_id", "t_p").style(
                        GeoAnalyticsAppearanceOption().fillColor("9ccc65").fillOpacity(0.5)
                            .strokeColor("000000").labelSize(10).strokeWidth(0.0)
                            .labelColor("000000")
                    ).build(),
                "stt_nme",
                arrayOf("PUNJAB")
            )
        )
        models.add(
            GeoAnalyticsModel(
                MapmyIndiaGeoAnalyticsType.CITY,
                MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0")
                    .geoboundType("stt_nme").geoBound("TAMIL NADU")
                    .propertyNames("city_nme", "city_id", "t_p").style(
                        GeoAnalyticsAppearanceOption().fillColor("78909c").fillOpacity(0.5)
                            .strokeColor("000000").labelSize(10).strokeWidth(0.0)
                            .labelColor("000000")
                    ).build(),
                "stt_nme",
                arrayOf("TAMIL NADU")
            )
        )
        models.add(
            GeoAnalyticsModel(
                MapmyIndiaGeoAnalyticsType.VILLAGE,
                MapmyIndiaGeoAnalyticsRequest.builder().attribute("t_p").query(">0")
                    .geoboundType("stt_nme").geoBound("GOA")
                    .propertyNames("vil_nme", "id", "t_p").style(
                        GeoAnalyticsAppearanceOption().fillColor("f06292").fillOpacity(0.5)
                            .strokeColor("000000").labelSize(10).strokeWidth(0.0)
                            .labelColor("000000")
                    ).build(),
                "stt_nme",
                arrayOf("GOA")
            )
        )
        models.add(
            GeoAnalyticsModel(
                MapmyIndiaGeoAnalyticsType.SUB_LOCALITY,
                MapmyIndiaGeoAnalyticsRequest.builder().geoboundType("stt_nme")
                    .geoBound("UTTAR PRADESH", "BIHAR")
                    .propertyNames("subl_nme", "subl_id").style(
                        GeoAnalyticsAppearanceOption().fillColor("f06292").fillOpacity(0.5)
                            .strokeColor("000000").labelSize(10).strokeWidth(0.0)
                            .labelColor("000000")
                    ).build(),
                "stt_nme",
                arrayOf("UTTAR PRADESH", "BIHAR")
            )
        )
        models.add(
            GeoAnalyticsModel(
                MapmyIndiaGeoAnalyticsType.SUB_SUB_LOCALITY,
                MapmyIndiaGeoAnalyticsRequest.builder().geoboundType("stt_nme")
                    .geoBound("UTTAR PRADESH", "BIHAR")
                    .propertyNames("sslc_nme", "sslc_id").style(
                        GeoAnalyticsAppearanceOption().fillColor("f06292").fillOpacity(0.5)
                            .strokeColor("000000").labelSize(10).strokeWidth(0.0)
                            .labelColor("000000")
                    ).build(),
                "stt_nme",
                arrayOf("UTTAR PRADESH", "BIHAR")
            )
        )
        adapter?.setGeoAnalyticsModels(models)
    }

    override fun onStart() {
        super.onStart()
        mBinding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        mBinding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mBinding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.mapView.onLowMemory()
    }

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap
        geoAnalyticsPlugin = MapmyIndiaGeoAnalyticsPlugin(mBinding.mapView, mapmyIndiaMap)
        init()
    }

    override fun onMapError(i: Int, s: String) {
        Timber.e("$i-----$s")
    }
}