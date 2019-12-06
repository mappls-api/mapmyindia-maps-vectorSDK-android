package com.mmi.sdk.demo.kotlin.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mmi.sdk.demo.R
import kotlinx.android.synthetic.main.base_layout.*

/**
 * Created by CEINFO on 26-02-2019.
 */
class AddMarkerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)
        mapBoxId.onCreate(savedInstanceState)
        mapView = findViewById<MapView>(R.id.mapBoxId) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapError(p0: Int, p1: String?) {}

    override fun onMapReady(mapboxMap: MapboxMap?) {
        mapboxMap?.setMinZoomPreference(4.5)
        mapboxMap?.setMaxZoomPreference(18.5)



        mapboxMap?.setPadding(20, 20, 20, 20)


        mapboxMap?.addMarker(MarkerOptions().position(LatLng(
                25.321684, 82.987289)).title("XYZ"))

        /* this is done for animating/moving camera to particular position */
        val cameraPosition = CameraPosition.Builder().target(LatLng(
                25.321684, 82.987289)).zoom(10.0).tilt(0.0).build()
        mapboxMap?.cameraPosition = cameraPosition
    }

    override fun onStart() {
        super.onStart()
        mapBoxId.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapBoxId.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapBoxId.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapBoxId.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapBoxId.onDestroy()
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