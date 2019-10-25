package com.mmi.sdk.demo.kotlin.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds

import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mmi.sdk.demo.R
import com.mmi.sdk.demo.kotlin.plugin.GradientPolylinePlugin
import kotlinx.android.synthetic.main.activity_gradient_polyline.*
import java.util.ArrayList

/**
 * Created by Saksham on 20/9/19.
 */
class GradientPolylineActivity : AppCompatActivity(), OnMapReadyCallback {

    private val listOfLatLng = ArrayList<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gradient_polyline)

        mapBoxId!!.getMapAsync(this)

        listOfLatLng.add(LatLng(28.705436, 77.100462))
        listOfLatLng.add(LatLng(28.705191, 77.100784))
        listOfLatLng.add(LatLng(28.704646, 77.101514))
        listOfLatLng.add(LatLng(28.704194, 77.101171))
        listOfLatLng.add(LatLng(28.704083, 77.101066))
        listOfLatLng.add(LatLng(28.703900, 77.101318))

    }

    override fun onMapReady(mapboxMap: MapboxMap) {

        mapboxMap.setPadding(20, 20, 20, 20)
        val latLngBounds = LatLngBounds.Builder()
                .includes(listOfLatLng)
                .build()
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10))

        val animatedPolylinePlugin = GradientPolylinePlugin(mapboxMap, mapBoxId!!)
        animatedPolylinePlugin.createPolyline(listOfLatLng)
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
