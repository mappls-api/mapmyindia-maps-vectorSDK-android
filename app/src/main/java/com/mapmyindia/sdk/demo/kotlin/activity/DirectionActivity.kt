package com.mapmyindia.sdk.demo.kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityDirectionLayoutBinding
import com.mapmyindia.sdk.demo.java.activity.InputActivity
import com.mapmyindia.sdk.demo.java.utils.CheckInternet
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog
import com.mapmyindia.sdk.demo.kotlin.plugin.DirectionPolylinePlugin
import com.mmi.services.api.directions.DirectionsCriteria
import com.mmi.services.api.directions.MapmyIndiaDirections
import com.mmi.services.api.directions.models.DirectionsResponse
import com.mmi.services.api.directions.models.DirectionsRoute
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*

/**
 * Created by CEINFO on 26-02-2019.
 */
class DirectionActivity : AppCompatActivity(), OnMapReadyCallback, MapboxMap.OnMapLongClickListener{

    private var mapmyIndiaMap: MapboxMap? = null
    private var transparentProgressDialog: TransparentProgressDialog? = null

    private var profile: String = DirectionsCriteria.PROFILE_DRIVING
    private var resource: String = DirectionsCriteria.RESOURCE_ROUTE
    private var directionPolylinePlugin: DirectionPolylinePlugin? = null
    private lateinit var mBinding: ActivityDirectionLayoutBinding
    private lateinit var floatingActionButton: FloatingActionButton
    private var mDestination = "MMI000"
    private var mSource = "28.594475,77.202432"
    private var wayPoints: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_direction_layout)
        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)
        transparentProgressDialog = TransparentProgressDialog(this, R.drawable.circle_loader, "")
        floatingActionButton = findViewById(R.id.edit_btn)
        floatingActionButton.setOnClickListener { v: View? ->
            val intent = Intent(this, InputActivity::class.java)
            intent.putExtra("origin", mSource);
            intent.putExtra("destination", mDestination);
            intent.putExtra("waypoints", wayPoints);
            startActivityForResult(intent, 500)
        }
        mBinding.tabLayoutProfile.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (mapmyIndiaMap == null) {
                    if (mBinding.tabLayoutProfile.getTabAt(0) != null) {
                        (mBinding.tabLayoutProfile.getTabAt(0))!!.select()
                        return
                    }
                }

                when (tab!!.position) {
                    0 -> {
                        profile = DirectionsCriteria.PROFILE_DRIVING
                        mBinding.rgResourceType.visibility = View.VISIBLE
                    }

                    1 -> {
                        profile = DirectionsCriteria.PROFILE_BIKING
                        mBinding.rgResourceType.check(R.id.rb_without_traffic)
                        mBinding.rgResourceType.visibility = View.GONE
                    }

                    2 -> {
                        profile = DirectionsCriteria.PROFILE_WALKING
                        mBinding.rgResourceType.check(R.id.rb_without_traffic)
                        mBinding.rgResourceType.visibility = View.GONE
                    }
                }

                getDirections()
            }


            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })

        mBinding.rgResourceType.setOnCheckedChangeListener { radioGroup, _ ->
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
        mapmyIndiaMap.addOnMapLongClickListener(this)
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
        val dest: Any = if (!mDestination.contains(",")) mDestination else Point.fromLngLat(mDestination.split(",").toTypedArray()[1].toDouble(), mDestination.split(",").toTypedArray()[0].toDouble())
        val src: Any = if (!mSource.contains(",")) mSource else Point.fromLngLat(mSource.split(",").toTypedArray()[1].toDouble(), mSource.split(",").toTypedArray()[0].toDouble())

        val builder = MapmyIndiaDirections.builder()

        if (src is String) {
            builder.origin(src.toString())
        } else {
            builder.origin((src as Point))
        }

        if (dest is String) {
            builder.destination(dest.toString())
        } else {
            builder.destination((dest as Point))
        }

        if (wayPoints != null) {
            if (!wayPoints!!.contains(";")) {
                if (!wayPoints!!.contains(",")) {
                    Log.e("taf", wayPoints!!)
                    builder.addWaypoint(wayPoints!!)
                } else {
                    val point = Point.fromLngLat(wayPoints!!.split(",").toTypedArray()[1].toDouble(), wayPoints!!.split(",").toTypedArray()[0].toDouble())
                    builder.addWaypoint(point)
                }
            } else {
                val wayPointsArray = wayPoints!!.split(";").toTypedArray()
                for (value in wayPointsArray) {
                    if (!value.contains(",")) {
                        builder.addWaypoint(value)
                    } else {
                        val point = Point.fromLngLat(value.split(",").toTypedArray()[1].toDouble(), value.split(",").toTypedArray()[0].toDouble())
                        builder.addWaypoint(point)
                    }
                }
            }
        }
         builder.profile(profile)
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
        mBinding.directionDetailsLayout.visibility = View.VISIBLE
        mBinding.editBtn.visibility=View.VISIBLE
        mBinding.tvDuration.text = "(" + getFormattedDuration(route.duration()!!) + ")"
        mBinding.tvDistance.text = getFormattedDistance(route.distance()!!)
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
            directionPolylinePlugin = DirectionPolylinePlugin(mapmyIndiaMap!!, mBinding.mapView, profile)
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
        mBinding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mBinding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.mapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mBinding.mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 500 && resultCode == RESULT_OK) {
            if (data!!.hasExtra("destination")) {
                mDestination = data.getStringExtra("destination")!!
            }
            if (data.hasExtra("origin")) {
                mSource = data.getStringExtra("origin")!!
            }
            if (data.hasExtra("waypoints")) {
                wayPoints = data.getStringExtra("waypoints")
            }
            getDirections()
        }
    }

    override fun onMapLongClick(latlng: LatLng) {
       val alertDialog = AlertDialog.Builder(this);
        alertDialog.setMessage("Select Point as Source or Destination");

        alertDialog.setPositiveButton("Source") { dialog, which ->

            mSource = "${latlng.latitude},${latlng.longitude}"
            getDirections(); }

        alertDialog.setNegativeButton("Destination") { dialog, which ->

            mDestination = "${latlng.latitude},${latlng.longitude}"
            getDirections(); }
        alertDialog.setNeutralButton("Waypoint") { dialog, which ->

           if (TextUtils.isEmpty(wayPoints)){
               wayPoints = "${latlng.latitude},${latlng.longitude}"
           }else{
               wayPoints = wayPoints+";"+ "${latlng.latitude},${latlng.longitude}"
           }
            getDirections(); }

        alertDialog.setCancelable(true);
        alertDialog.show();

    }
}