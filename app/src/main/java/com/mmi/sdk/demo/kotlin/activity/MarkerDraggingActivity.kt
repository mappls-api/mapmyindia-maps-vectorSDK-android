package com.mmi.sdk.demo.kotlin.activity


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng

import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mmi.sdk.demo.R
import com.mmi.sdk.demo.kotlin.plugin.MarkerPlugin
import kotlinx.android.synthetic.main.base_layout.*

/**
 * Created by Saksham on 20/9/19.
 */
class MarkerDraggingActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMapboxMap: MapboxMap? = null
    private val latLng = LatLng(28.705436, 77.100462)
    private var markerPlugin: MarkerPlugin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)

        mapBoxId!!.onCreate(savedInstanceState)
        mapBoxId!!.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mMapboxMap = mapboxMap
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0))
        initMarker()
    }

    private fun initMarker() {
        markerPlugin = MarkerPlugin(mMapboxMap!!, mapBoxId!!)
        markerPlugin!!.icon = resources.getDrawable(R.drawable.placeholder)
        markerPlugin!!.addMarker(latLng)
        markerPlugin!!.draggable(true)
    }

    override fun onMapError(i: Int, s: String) {

    }

    override fun onStart() {
        super.onStart()
        mapBoxId!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapBoxId!!.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapBoxId!!.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mapBoxId!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapBoxId!!.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapBoxId!!.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapBoxId!!.onSaveInstanceState(outState)
    }
}
