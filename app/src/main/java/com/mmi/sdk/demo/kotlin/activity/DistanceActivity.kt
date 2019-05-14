package com.mmi.sdk.demo.kotlin.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mmi.sdk.demo.R
import com.mmi.sdk.demo.java.utils.CheckInternet
import com.mmi.sdk.demo.java.utils.TransparentProgressDialog
import com.mmi.services.api.distance.legacy.MapmyIndiaDistanceLegacy
import com.mmi.services.api.distance.legacy.model.LegacyDistanceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by CEINFO on 26-02-2019.
 */
class DistanceActivity : AppCompatActivity(), OnMapReadyCallback {
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
        mapboxMap.setMinZoomPreference(4.5)
        mapboxMap.setMaxZoomPreference(18.5)
        mapboxMap.setPadding(20, 20, 20, 20)

        mapboxMap.cameraPosition = setCameraAndTilt()
        val coordinatesPoint = ArrayList<Point>()
        coordinatesPoint.add(Point.fromLngLat(77.257373, 28.551087))
        coordinatesPoint.add(Point.fromLngLat(77.234230, 28.582864))
        if (CheckInternet.isNetworkAvailable(this@DistanceActivity)) {
            calculateDistance(coordinatesPoint)
        } else {
            Toast.makeText(this, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapError(i: Int, s: String) {}

    private fun setCameraAndTilt(): CameraPosition {
        return CameraPosition.Builder().target(LatLng(
                28.551087, 77.257373)).zoom(14.0).tilt(0.0).build()
    }

    private fun progressDialogShow() {
        transparentProgressDialog!!.show()
    }

    private fun progressDialogHide() {
        transparentProgressDialog!!.dismiss()
    }

    private fun calculateDistance(pointList: List<Point>) {
        progressDialogShow()
        MapmyIndiaDistanceLegacy.Builder<MapmyIndiaDistanceLegacy.Builder<*>>()
                .setCoordinates(pointList)
                .setCenter(Point.fromLngLat(77.234230, 28.582864))
                .build()
                .enqueueCall(object : Callback<LegacyDistanceResponse> {
                    override fun onResponse(call: Call<LegacyDistanceResponse>, response: Response<LegacyDistanceResponse>) {
                        progressDialogHide()
                        if (response.code() == 200) {
                            val legacyDistanceResponse = response.body()
                            val distanceList = legacyDistanceResponse!!.results
                            if (distanceList.size > 0) {
                                val distance = distanceList[0]
                                Toast.makeText(this@DistanceActivity, "Distance: " + distance.length, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@DistanceActivity, "Failed: " + legacyDistanceResponse.responseCode, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<LegacyDistanceResponse>, t: Throwable) {
                        progressDialogHide()
                    }
                })
    }
}
