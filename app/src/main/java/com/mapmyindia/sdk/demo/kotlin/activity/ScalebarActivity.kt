package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.plugin.scalebar.ScaleBarOptions
import com.mapmyindia.sdk.plugin.scalebar.ScaleBarPlugin
import kotlinx.android.synthetic.main.base_layout.*

class ScalebarActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)
        map_view.onCreate(savedInstanceState)
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapError(p0: Int, p1: String?) {}

    override fun onMapReady(mapmyIndiaMap: MapboxMap?) {


        /* this is done for animating/moving camera to particular position */
        val cameraPosition = CameraPosition.Builder().target(LatLng(
                25.321684, 82.987289)).zoom(10.0).tilt(0.0).build()
        mapmyIndiaMap?.cameraPosition = cameraPosition

        val scaleBarPlugin = ScaleBarPlugin(mapView, mapmyIndiaMap!!)
        val scalebarOptions = ScaleBarOptions(this)
                .setTextColor(android.R.color.black)
                .setTextSize(40f)
                .setBarHeight(5f)
                .setBorderWidth(2f)
                .setRefreshInterval(15)
                .setMarginTop(R.dimen.scalebar_top_margin)
                .setMarginLeft(R.dimen.scalebar_left_margin)
                .setTextBarMargin(15f)
                .setMaxWidthRatio(0.5f)
                .setShowTextBorder(true)
                .setTextBorderWidth(5f)
        scaleBarPlugin.create(scalebarOptions)
    }

    override fun onStart() {
        super.onStart()
        map_view.onStart()
    }

    override fun onResume() {
        super.onResume()
        map_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view.onPause()
    }

    override fun onStop() {
        super.onStop()
        map_view.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view.onDestroy()
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