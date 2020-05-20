package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.java.utils.CheckInternet
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog
import com.mapmyindia.sdk.demo.kotlin.plugin.DirectionPolylinePlugin
import com.mmi.services.api.directions.DirectionsCriteria
import com.mmi.services.api.directions.MapmyIndiaDirections

import com.mmi.services.api.directions.models.DirectionsResponse
import com.mmi.services.api.directions.models.DirectionsRoute
import kotlinx.android.synthetic.main.activity_direction_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*

/**
 * Created by CEINFO on 26-02-2019.
 */
class DirectionActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mapmyIndiaMap: MapboxMap? = null
    private var mapView: MapView? = null
    private var transparentProgressDialog: TransparentProgressDialog? = null

    private var profile: String = DirectionsCriteria.PROFILE_DRIVING
    private var resource: String = DirectionsCriteria.RESOURCE_ROUTE
    private var directionPolylinePlugin: DirectionPolylinePlugin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direction_layout)
        mapView = findViewById(R.id.map_view)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        transparentProgressDialog = TransparentProgressDialog(this, R.drawable.circle_loader, "")

        tab_layout_profile!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (mapmyIndiaMap == null) {
                    if (tab_layout_profile.getTabAt(0) != null) {
                        (tab_layout_profile.getTabAt(0))!!.select()
                        return
                    }
                }

                when (tab!!.position) {
                    0 -> {
                        profile = DirectionsCriteria.PROFILE_DRIVING
                        rg_resource_type!!.visibility = View.VISIBLE
                    }

                    1 -> {
                        profile = DirectionsCriteria.PROFILE_BIKING
                        rg_resource_type!!.check(R.id.rb_without_traffic)
                        rg_resource_type.visibility = View.GONE
                    }

                    2 -> {
                        profile = DirectionsCriteria.PROFILE_WALKING
                        rg_resource_type!!.check(R.id.rb_without_traffic)
                        rg_resource_type.visibility = View.GONE
                    }
                }

                getDirections()
            }


            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })

        rg_resource_type!!.setOnCheckedChangeListener { radioGroup, _ ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.rb_without_traffic -> resource = DirectionsCriteria.RESOURCE_ROUTE

                R.id.rb_with_route_eta -> resource = DirectionsCriteria.RESOURCE_ROUTE_ETA

                R.id.rb_with_traffic -> resource = DirectionsCriteria.RESOURCE_ROUTE_TRAFFIC
            }

            getDirections()
        }
    }

    override fun onMapReady(mapmyIndiaMap: MapboxMap) {
        this.mapmyIndiaMap = mapmyIndiaMap





        mapmyIndiaMap.setPadding(20, 20, 20, 20)

        mapmyIndiaMap.cameraPosition = setCameraAndTilt()
        if (CheckInternet.isNetworkAvailable(this)) {
            getDirections()
        } else {
            Toast.makeText(this, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Set Camera Position
     *
     * @return camera position
     */
    private fun setCameraAndTilt(): CameraPosition {
        return CameraPosition.Builder().target(LatLng(
                28.551087, 77.257373)).zoom(14.0).tilt(0.0).build()
    }

    /**
     * Show Progress Dialog
     */
    private fun progressDialogShow() {
        transparentProgressDialog!!.show()
    }

    /**
     * Hide Progress dialog
     */
    private fun progressDialogHide() {
        transparentProgressDialog!!.dismiss()
    }

    /**
     * Get Directions
     */
    private fun getDirections() {
        progressDialogShow()

        MapmyIndiaDirections.builder()
                .origin(Point.fromLngLat(77.202432, 28.594475))
                .destination(Point.fromLngLat(77.186982, 28.554676))
                .profile(profile)
                .resource(resource)
                .steps(true)
                .alternatives(false)
                .overview(DirectionsCriteria.OVERVIEW_FULL).build().enqueueCall(object : Callback<DirectionsResponse> {
                    override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                val directionsResponse = response.body()
                                val results = directionsResponse!!.routes()

                                if (results.size > 0) {
                                    mapmyIndiaMap!!.clear()
                                    val directionsRoute = results[0]
                                    drawPath(PolylineUtils.decode(directionsRoute.geometry()!!, Constants.PRECISION_6))
                                    updateData(directionsRoute)
                                }
                            }
                        } else {
                            Toast.makeText(this@DirectionActivity, response.message(), Toast.LENGTH_LONG).show()
                        }
                        progressDialogHide()
                    }

                    override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                        progressDialogHide()
                    }
                })
    }

    /**
     * Update Route data
     *
     * @param route route data
     */
    private fun updateData(route: DirectionsRoute) {
        direction_details_layout!!.visibility = View.VISIBLE
        tv_duration!!.text = "(" + getFormattedDuration(route.duration()!!) + ")"
        tv_distance!!.text = getFormattedDistance(route.distance()!!)
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
        return if (days > 0L) "" + days + " " + (if (days > 1L) "Days" else "Day") + " " + hour + " hr" + (if(min>0L) " " + min + " min" else "")
        else  {
            if(hour > 0L) "" + hour + " hr" + (if(min > 0L) " " + min + "min" else "") else "" + min + "min"
        }
    }

    /**
     * Add polyline along the points
     *
     * @param waypoints route points
     */
    private fun drawPath(waypoints: List<Point>) {
        val listOfLatlang = ArrayList<LatLng>()
        for (point in waypoints) {
            listOfLatlang.add(LatLng(point.latitude(), point.longitude()))
        }

        if(directionPolylinePlugin == null) {
            directionPolylinePlugin = DirectionPolylinePlugin(mapmyIndiaMap!!, mapView!!, profile)
            directionPolylinePlugin!!.createPolyline(listOfLatlang)
        } else {
            directionPolylinePlugin!!.updatePolyline(profile, listOfLatlang)
        }

//        mapmyIndiaMap?.addPolyline(PolylineOptions().addAll(listOfLatlang).color(Color.parseColor("#3bb2d0")).width(4f))
        val latLngBounds = LatLngBounds.Builder().includes(listOfLatlang).build()
        mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 30))
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