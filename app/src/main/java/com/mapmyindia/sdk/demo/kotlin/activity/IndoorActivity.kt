package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import kotlinx.android.synthetic.main.indoor_layout.*

/**
 * Created by Saksham on 4/12/19.
 */
class IndoorActivity : AppCompatActivity(), OnMapReadyCallback {

    private var map: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.indoor_layout)
        map_view?.onCreate(savedInstanceState)
        map_view?.getMapAsync(this)

    }

    override fun onMapError(i: Int, err: String?) {

    }

    override fun onMapReady(mapmyIndiaMap: MapboxMap?) {
        map = mapmyIndiaMap
        //To turn on layer control
        mapmyIndiaMap?.uiSettings?.isLayerControlEnabled = true



        val iconFactory: IconFactory = IconFactory.getInstance(this)
        val icon: Icon = iconFactory.fromResource(R.drawable.placeholder)
        mapmyIndiaMap?.addMarker(MarkerOptions()
                .position(LatLng(28.5425071, 77.1560724))
                .icon(icon))

        val cameraPosition: CameraPosition = CameraPosition.Builder()
                .target(LatLng(28.5425071, 77.1560724))
                .zoom(16.0)
                .tilt(0.0)
                .build()

        mapmyIndiaMap?.cameraPosition = cameraPosition
    }

    override fun onStart() {
        super.onStart()
        map_view!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        map_view!!.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view!!.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        map_view!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        map_view!!.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view!!.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map_view!!.onSaveInstanceState(outState)
    }
}