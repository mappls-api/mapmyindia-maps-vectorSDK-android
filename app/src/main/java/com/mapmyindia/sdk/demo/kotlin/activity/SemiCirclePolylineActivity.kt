package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.utility.SemiCirclePointsListHelper
import com.mapmyindia.sdk.plugin.annotation.LineManager
import com.mapmyindia.sdk.plugin.annotation.LineOptions
import kotlinx.android.synthetic.main.activity_semi_circle_polyline.*

/**
 * Created by Saksham on 20/9/19.
 */
class SemiCirclePolylineActivity : AppCompatActivity(), OnMapReadyCallback {

    private var lineManager: LineManager? = null
    private var listOfLatLng: List<LatLng>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semi_circle_polyline)

        map_view?.onCreate(savedInstanceState)
        map_view?.getMapAsync(this)
        remove.setOnClickListener(View.OnClickListener {
            lineManager?.clearAll()
            remove.visibility = View.GONE
            add.visibility = View.VISIBLE
        })

        add.setOnClickListener{
            val lineOptions: LineOptions = LineOptions()
                    .points(listOfLatLng)
                    .lineColor("#FF0000")
                    .lineWidth(4f)
            lineManager?.create(lineOptions)
            add.visibility = View.GONE
            remove.visibility = View.VISIBLE
        }

        listOfLatLng = SemiCirclePointsListHelper.showCurvedPolyline(LatLng(28.7039, 77.101318), LatLng(28.704248, 77.102370), 0.5)
    }

    override fun onMapReady(mapmyIndiaMap: MapboxMap) {
        val latLngBounds = LatLngBounds.Builder()
                .includes(listOfLatLng)
                .build()

        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100))

        lineManager = LineManager(map_view!!, mapmyIndiaMap, null, GeoJsonOptions().withLineMetrics(true).withBuffer(2))
        lineManager?.lineDasharray = arrayOf(4f, 6f)
        val lineOptions: LineOptions = LineOptions()
                .points(listOfLatLng)
                .lineColor("#FF0000")
                .lineWidth(4f)
        lineManager?.create(lineOptions)

        remove.visibility = View.VISIBLE

    }

    override fun onMapError(i: Int, s: String) {

    }

    override fun onStart() {
        super.onStart()
        map_view?.onStart()
    }

    override fun onStop() {
        super.onStop()
        map_view?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view?.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        map_view?.onPause()
    }

    override fun onResume() {
        super.onResume()
        map_view?.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map_view?.onSaveInstanceState(outState)
    }
}
