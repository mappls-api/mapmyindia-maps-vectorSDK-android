package com.mmi.sdk.demo.kotlin.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mmi.sdk.demo.R
import com.mmi.sdk.demo.java.utils.CheckInternet
import com.mmi.sdk.demo.java.utils.TransparentProgressDialog
import com.mmi.services.api.directions.legacy.MapmyIndiaDirectionsLegacy
import com.mmi.services.api.directions.legacy.model.LegacyRouteResponse
import com.mmi.services.api.directions.legacy.model.Trip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by CEINFO on 26-02-2019.
 */
class DirectionActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mapboxMap: MapboxMap? = null
    private var mapView: MapView? = null
    private var transparentProgressDialog: TransparentProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)
        mapView = findViewById(R.id.mapBoxId)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)
        transparentProgressDialog = TransparentProgressDialog(this, R.drawable.circle_loader, "")
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap


        mapboxMap.setMinZoomPreference(4.5)
        mapboxMap.setMaxZoomPreference(18.5)

        mapboxMap.setPadding(20, 20, 20, 20)

        mapboxMap.cameraPosition = setCameraAndTilt()
        if (CheckInternet.isNetworkAvailable(this@DirectionActivity)) {
            getDirections()
        } else {
            Toast.makeText(this, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show()
        }
    }

    protected fun setCameraAndTilt(): CameraPosition {
        return CameraPosition.Builder().target(LatLng(
                28.551087, 77.257373)).zoom(14.0).tilt(0.0).build()
    }


    protected fun progressDialogShow() {
        transparentProgressDialog!!.show()
    }

    protected fun progressDialogHide() {
        transparentProgressDialog!!.dismiss()
    }

    private fun getDirections() {
        progressDialogShow()

        MapmyIndiaDirectionsLegacy.Builder<MapmyIndiaDirectionsLegacy.Builder<*>>()
                .setOrigin(Point.fromLngLat(77.202432, 28.594475))
                .setDestination(Point.fromLngLat(77.186982, 28.554676))
                .build().enqueueCall(object : Callback<LegacyRouteResponse> {
                    override fun onResponse(call: Call<LegacyRouteResponse>, response: Response<LegacyRouteResponse>) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                val directionsResponse = response.body()
                                val results = directionsResponse!!.results
                                val tripList = results.trips
                                if (tripList.size > 0) {
                                    drawPath(tripList)
                                }
                            }
                        } else {
                            Toast.makeText(this@DirectionActivity, response.message(), Toast.LENGTH_LONG).show()
                        }
                        progressDialogHide()
                    }

                    override fun onFailure(call: Call<LegacyRouteResponse>, t: Throwable) {
                        progressDialogHide()
                    }
                })

    }

    private fun drawPath(waypoints: List<Trip>) {
        val listOfLatlang = ArrayList<LatLng>()
        val pointList = PolylineUtils.decode(waypoints[0].pts, 6)
        for (point in pointList) {
            listOfLatlang.add(LatLng(point.latitude(), point.longitude()))
        }

        mapboxMap!!.addPolyline(PolylineOptions().addAll(listOfLatlang).color(Color.parseColor("#3bb2d0")).width(4f))
        val latLngBounds = LatLngBounds.Builder().includes(listOfLatlang).build()
        mapboxMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 30))
    }

    override fun onMapError(i: Int, s: String) {

    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }
}