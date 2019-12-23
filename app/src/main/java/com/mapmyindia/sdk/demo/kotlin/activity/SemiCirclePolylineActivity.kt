package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.plugin.DashedPolylinePlugin
import com.mapmyindia.sdk.demo.kotlin.utility.SemiCirclePointsListHelper
import kotlinx.android.synthetic.main.activity_semi_circle_polyline.*

/**
 * Created by Saksham on 20/9/19.
 */
class SemiCirclePolylineActivity : AppCompatActivity(), OnMapReadyCallback {

    private var listOfLatLng: List<LatLng>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semi_circle_polyline)

        map_view!!.getMapAsync(this)

        listOfLatLng = SemiCirclePointsListHelper.showCurvedPolyline(LatLng(28.7039, 77.101318), LatLng(28.704248, 77.102370), 0.5)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        val latLngBounds = LatLngBounds.Builder()
                .includes(listOfLatLng)
                .build()

        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100))

        val dashedPolylinePlugin = DashedPolylinePlugin(mapboxMap, map_view!!)
        dashedPolylinePlugin.createPolyline(listOfLatLng!!)


    }

    override fun onMapError(i: Int, s: String) {

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
