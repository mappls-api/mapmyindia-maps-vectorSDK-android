package com.mapmyindia.sdk.demo.kotlin.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityCarAnimationBinding
import com.mapmyindia.sdk.demo.kotlin.plugin.AnimatedCarPlugin
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.annotations.PolylineOptions
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.geometry.LatLngBounds

/**
 * Created by Saksham on 19/9/19.
 */
class CarAnimationActivity: AppCompatActivity(), OnMapReadyCallback {

    private val listOfLatlang: MutableList<LatLng> = mutableListOf()
    private var animatedCarPlugin: AnimatedCarPlugin? = null
    private var index: Int = 0
    private lateinit var mBinding: ActivityCarAnimationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_car_animation)

        listOfLatlang.add(LatLng(28.705436, 77.100462))
        listOfLatlang.add(LatLng(28.705191, 77.100784))
        listOfLatlang.add(LatLng(28.704646, 77.101514))
        listOfLatlang.add(LatLng(28.704194, 77.101171))
        listOfLatlang.add(LatLng(28.704083, 77.101066))
        listOfLatlang.add(LatLng(28.703900, 77.101318))

        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)
    }

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {

        val latLngBounds: LatLngBounds = LatLngBounds.Builder()
                .includes(listOfLatlang)
                .build()

        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100))

        animatedCarPlugin = AnimatedCarPlugin(applicationContext, mBinding.mapView, mapmyIndiaMap)
        mapmyIndiaMap.addPolyline(PolylineOptions().addAll(listOfLatlang).color(Color.parseColor("#3bb2d0")).width(4f))

        animatedCarPlugin!!.addMainCar(listOfLatlang[index], true)
        animatedCarPlugin!!.animateCar()

        animatedCarPlugin!!.setOnUpdateNextPoint(object : AnimatedCarPlugin.OnUpdatePoint {
            override fun updateNextPoint() {
                if(index < listOfLatlang.size - 1)
                    index++

                animatedCarPlugin!!.updateNextPoint(listOfLatlang[index])
                animatedCarPlugin!!.animateCar()
            }

        })
    }

    override fun onMapError(i: Int, s: String?) {

    }


    override fun onStart() {
        super.onStart()
        mBinding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mBinding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.mapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        if (animatedCarPlugin != null)
            animatedCarPlugin!!.clearAllCallBacks()
        mBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (animatedCarPlugin != null)
            animatedCarPlugin!!.addAllCallBacks()
        mBinding.mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.mapView.onSaveInstanceState(outState)
    }

}