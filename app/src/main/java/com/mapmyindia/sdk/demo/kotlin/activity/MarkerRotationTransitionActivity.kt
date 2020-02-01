package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.plugin.MarkerPlugin
import kotlinx.android.synthetic.main.activity_marker_rotation_transition.*

/**
 * Created by Saksham on 3/9/19.
 */
class MarkerRotationTransitionActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {


    private var mMapboxMap: MapboxMap? = null
    private val latLngStart: LatLng = LatLng(28.705436, 77.100462)
    private val latLngEnd: LatLng = LatLng(28.703800, 77.101818)
    private var markerPlugin: MarkerPlugin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marker_rotation_transition)

        map_view!!.getMapAsync(this)

        marker_rotate!!.setOnClickListener(this)
        marker_transition!!.setOnClickListener(this)
    }
    override fun onMapError(p0: Int, p1: String?) {

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onMapReady(mapmyIndiaMap: MapboxMap?) {
        this.mMapboxMap = mapmyIndiaMap

        val latLngBounds: LatLngBounds = LatLngBounds.Builder()
                .include(latLngStart)
                .include(latLngEnd)
                .build()

        mapmyIndiaMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100))

        initMarker()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initMarker() {
        markerPlugin = MarkerPlugin(mMapboxMap!!, map_view)
        markerPlugin!!.icon = resources!!.getDrawable(R.drawable.placeholder,null)
        markerPlugin!!.addMarker(latLngStart)
    }

    override fun onClick(view: View?) {
        when(view!!.id) {
            R.id.marker_rotate ->
                if(markerPlugin != null) {
                    markerPlugin!!.startRotation()
                }

            R.id.marker_transition ->
                if(markerPlugin != null) {
                    markerPlugin!!.startTransition(latLngStart, latLngEnd)
                }
        }
    }

    override fun onStart() {
        super.onStart()
        map_view.onStart()
    }

    override fun onStop() {
        super.onStop()
        map_view.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view.onDestroy()
        if (markerPlugin != null) {
            markerPlugin!!.removeCallbacks()
        }
    }

    override fun onPause() {
        super.onPause()
        map_view.onPause()
    }

    override fun onResume() {
        super.onResume()
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