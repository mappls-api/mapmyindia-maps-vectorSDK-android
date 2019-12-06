package com.mmi.sdk.demo.kotlin.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
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
import com.mmi.services.api.directions.DirectionsCriteria
import com.mmi.services.api.distance.MapmyIndiaDistanceMatrix
import com.mmi.services.api.distance.models.DistanceResponse
import com.mmi.services.api.distance.models.DistanceResults
import kotlinx.android.synthetic.main.distance_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*

/**
 * Created by CEINFO on 26-02-2019.
 */
class DistanceActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mapView: MapView? = null
    private var transparentProgressDialog: TransparentProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.distance_activity)
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
        MapmyIndiaDistanceMatrix.builder()
                .coordinates(pointList)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .resource(DirectionsCriteria.RESOURCE_DISTANCE_ETA)
                .build()
                .enqueueCall(object : Callback<DistanceResponse> {
                    override fun onResponse(call: Call<DistanceResponse>, response: Response<DistanceResponse>) {
                        progressDialogHide()
                        if (response.code() == 200) {
                            val legacyDistanceResponse = response.body()
                            val distanceList = legacyDistanceResponse!!.results()

                            if (distanceList != null) {
                                val distances = distanceList.distances()
                                updateData(distanceList)
                                Toast.makeText(this@DistanceActivity, "Distance: " + distances!![0][1], Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@DistanceActivity, "Failed: " + legacyDistanceResponse.responseCode(), Toast.LENGTH_SHORT).show()
                            }
//
                        }
                    }

                    override fun onFailure(call: Call<DistanceResponse>, t: Throwable) {
                        progressDialogHide()
                    }
                })
    }


    private fun updateData(distanceList: DistanceResults) {
        distance_details_layout!!.visibility = View.VISIBLE
        tv_duration!!.text = "(" + getFormattedDuration(distanceList.durations()!![0][1]) + ")"
        tv_distance!!.text = getFormattedDistance(distanceList.distances()!![0][1])
    }

    /**
     * Get Formatted Distance
     *
     * @param distance route distance
     * @return distance in Kms if distance > 1000 otherwise in mtr
     */
    private fun getFormattedDistance(distance: Double): String {
        return if (distance / 1000 < 1) ("" + distance + "mtr.")
        else {
            val deimalFormatter = DecimalFormat("#.#")
            deimalFormatter.format(distance / 1000) + "Km."
        }
    }

    /**
     * Get Formatted Duration
     *
     * @param duration route duration
     * @return formatted duration
     */
    private fun getFormattedDuration(duration: Double): String {
        val min: Long = (duration % 3600 / 60).toLong()
        val hour: Long = (duration % 86400 / 3600).toLong()
        val days: Long = (duration / 86400).toLong()
        return if (days > 0L) "" + days + " " + (if (days > 1L) "Days" else "Day") + " " + hour + " hr" + (if (min > 0L) " " + min + " min" else "")
        else {
            if (hour > 0L) "" + hour + " hr" + (if (min > 0L) " " + min + "min" else "") else "" + min + "min"
        }
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
