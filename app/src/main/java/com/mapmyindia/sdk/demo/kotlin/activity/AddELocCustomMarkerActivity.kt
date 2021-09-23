package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.maps.MapView
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.annotations.Icon
import com.mapmyindia.sdk.maps.annotations.IconFactory
import com.mapmyindia.sdk.maps.annotations.MarkerOptions
import com.mapmyindia.sdk.maps.camera.CameraELocUpdateFactory

/**
 * Created by Saksham on 26-11-2020.
 */
class AddELocCustomMarkerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)
        mapView = findViewById<MapView>(R.id.map_view) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapError(p0: Int, p1: String?) {}

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {



        var iconFactory = IconFactory.getInstance(this)
        var icon: Icon = iconFactory.fromResource(R.drawable.placeholder)
        mapmyIndiaMap.addMarker(MarkerOptions().eLoc("MMI000").icon(icon), object : MapmyIndiaMap.OnMarkerAddedListener {
            override fun onSuccess() {
                Toast.makeText(this@AddELocCustomMarkerActivity, "Marker added Successfully", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure() {
                Toast.makeText(this@AddELocCustomMarkerActivity, "Invalid ELoc", Toast.LENGTH_SHORT).show()
            }

        })

        /* this is done for animating/moving camera to particular position */
        mapmyIndiaMap.animateCamera(CameraELocUpdateFactory.newELocZoom("MMI000", 16.0))
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}