package com.mapmyindia.sdk.demo.kotlin.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.plugin.AnimatedCarPlugin
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

        map_view?.onCreate(savedInstanceState)
        map_view?.getMapAsync(this)
    }

    override fun onMapReady(mapmyIndiaMap: MapboxMap?) {

        val latLngBounds: LatLngBounds = LatLngBounds.Builder()
                .includes(listOfLatlang)
                .build()

        mapmyIndiaMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100))

        animatedCarPlugin = AnimatedCarPlugin(applicationContext, map_view!!, mapmyIndiaMap)
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
        map_view!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        map_view.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        if (animatedCarPlugin != null)
            animatedCarPlugin!!.clearAllCallBacks()
        map_view.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (animatedCarPlugin != null)
            animatedCarPlugin!!.addAllCallBacks()
        map_view.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map_view.onSaveInstanceState(outState)
    }

}