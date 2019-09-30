package com.mmi.sdk.demo.kotlin.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mmi.sdk.demo.R
import com.mmi.sdk.demo.kotlin.plugin.DashedPolylinePlugin
import com.mmi.sdk.demo.kotlin.utility.SemiCirclePointsListHelper
import kotlinx.android.synthetic.main.activity_semi_circle_polyline.*

/**
 * Created by Saksham on 20/9/19.
 */
class SemiCirclePolylineActivity : AppCompatActivity(), OnMapReadyCallback {

    private var listOfLatLng: List<LatLng>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semi_circle_polyline)

        mapBoxId!!.getMapAsync(this)

        listOfLatLng = SemiCirclePointsListHelper.showCurvedPolyline(LatLng(28.7039, 77.101318), LatLng(28.704248, 77.102370), 0.5)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        val latLngBounds = LatLngBounds.Builder()
                .includes(listOfLatLng)
                .build()

        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100))

        val dashedPolylinePlugin = DashedPolylinePlugin(mapboxMap, mapBoxId!!)
        dashedPolylinePlugin.createPolyline(listOfLatLng!!)


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
