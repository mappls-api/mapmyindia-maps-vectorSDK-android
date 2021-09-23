package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.StyleActivityBinding
import com.mapmyindia.sdk.demo.java.adapter.StyleAdapter
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.MapmyIndiaMapConfiguration
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.Style
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.style.OnStyleLoadListener
import com.mapmyindia.sdk.maps.style.model.MapmyIndiaStyle
import timber.log.Timber


class StyleActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mBinding: StyleActivityBinding
    private lateinit var adapter: StyleAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
    private var mapmyIndiaMap: MapmyIndiaMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.style_activity)
        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)

        bottomSheetBehavior = BottomSheetBehavior.from(mBinding.bottomSheet)
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.peekHeight = 200
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        mBinding.rvStyle.layoutManager = LinearLayoutManager(this)
        adapter = StyleAdapter()
        mBinding.rvStyle.adapter = adapter
        mBinding.saveLastStyle.isChecked = MapmyIndiaMapConfiguration.getInstance().isShowLastSelectedStyle


        mBinding.saveLastStyle.setOnCheckedChangeListener { _, isChecked -> MapmyIndiaMapConfiguration.getInstance().isShowLastSelectedStyle = isChecked }

        adapter.setOnStyleSelectListener { style: MapmyIndiaStyle ->

            mapmyIndiaMap?.setMapmyIndiaStyle(style.name, object : OnStyleLoadListener {
                override fun onError(error: String) {
                    Toast.makeText(this@StyleActivity, error, Toast.LENGTH_SHORT).show()
                }

                override fun onStyleLoaded(style: Style) {
                    Toast.makeText(this@StyleActivity, "onStyleLoaded", Toast.LENGTH_SHORT).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            })

        }
    }


    override fun onStart() {
        super.onStart()
        mBinding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mBinding.mapView.onResume()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onMapError(p0: Int, p1: String?) {
        Timber.tag("onMapError").e("$p0------$p1")
    }

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        Timber.tag("onMapReady").e("SUCCESS")
        this.mapmyIndiaMap = mapmyIndiaMap
        this.mapmyIndiaMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(28.6466772, 76.8130614), 12.0))
            Timber.e(Gson().toJson(this.mapmyIndiaMap?.mapmyIndiaAvailableStyles))
            adapter.setStyleList(this.mapmyIndiaMap?.mapmyIndiaAvailableStyles)
    }
}