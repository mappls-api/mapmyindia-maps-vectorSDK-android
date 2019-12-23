package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import kotlinx.android.synthetic.main.base_layout.*

/**
 * Created by Saksham on 2/12/19.
 */
class AddCustomInfoWindowActivity : AppCompatActivity(), OnMapReadyCallback {

    private val latLngList: MutableList<LatLng> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)
        map_view.onCreate(savedInstanceState)
        map_view.getMapAsync(this)
        latLngList.add(LatLng(25.321684, 82.987289))
        latLngList.add(LatLng(25.331684, 82.997289))
        latLngList.add(LatLng(25.321684, 82.887289))
        latLngList.add(LatLng(25.311684, 82.987289))
    }

    override fun onMapError(p0: Int, p1: String?) {

    }

    override fun onMapReady(mapboxMap: MapboxMap?) {
        mapboxMap?.setMinZoomPreference(4.5)
        mapboxMap?.setMaxZoomPreference(18.5)

        latLngList.forEach {
            mapboxMap?.addMarker(MarkerOptions().position(it).setTitle("XYZ"))
        }

        mapboxMap?.setInfoWindowAdapter {
            val view: View? = LayoutInflater.from(this@AddCustomInfoWindowActivity).inflate(R.layout.custom_info_window_layout, null)
            val textView: TextView = view?.findViewById(R.id.text)!!
            textView.text = it.title
            return@setInfoWindowAdapter view
        }

        mapboxMap?.setOnMarkerClickListener {
            Toast.makeText(this, it.position.toString(), Toast.LENGTH_SHORT).show()
            return@setOnMarkerClickListener false
        }

        val latLngBounds: LatLngBounds = LatLngBounds.Builder()
                .includes(latLngList)
                .build()

        mapboxMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100, 10, 100, 10))

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