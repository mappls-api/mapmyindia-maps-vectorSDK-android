package com.mmi.sdk.demo.kotlin.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mmi.sdk.demo.R
import com.mmi.sdk.demo.kotlin.plugin.AnimatedCarPlugin
import kotlinx.android.synthetic.main.activity_car_animation.*

/**
 * Created by Saksham on 19/9/19.
 */
class CarAnimationActivity: AppCompatActivity(),OnMapReadyCallback {

    private val listOfLatlang: MutableList<LatLng> = mutableListOf()
    private var animatedCarPlugin: AnimatedCarPlugin? = null
    private var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_animation)

        listOfLatlang.add(LatLng(28.705436, 77.100462))
        listOfLatlang.add(LatLng(28.705191, 77.100784))
        listOfLatlang.add(LatLng(28.704646, 77.101514))
        listOfLatlang.add(LatLng(28.704194, 77.101171))
        listOfLatlang.add(LatLng(28.704083, 77.101066))
        listOfLatlang.add(LatLng(28.703900, 77.101318))

        mapBoxId!!.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap?) {

        val latLngBounds: LatLngBounds = LatLngBounds.Builder()
                .includes(listOfLatlang)
                .build()

        mapboxMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100))

        animatedCarPlugin = AnimatedCarPlugin(applicationContext, mapBoxId!!, mapboxMap)
        mapboxMap.addPolyline(PolylineOptions().addAll(listOfLatlang).color(Color.parseColor("#3bb2d0")).width(4f))

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
        mapBoxId!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapBoxId.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapBoxId.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        if (animatedCarPlugin != null)
            animatedCarPlugin!!.clearAllCallBacks()
        mapBoxId.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (animatedCarPlugin != null)
            animatedCarPlugin!!.addAllCallBacks()
        mapBoxId.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapBoxId.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapBoxId.onSaveInstanceState(outState)
    }

}