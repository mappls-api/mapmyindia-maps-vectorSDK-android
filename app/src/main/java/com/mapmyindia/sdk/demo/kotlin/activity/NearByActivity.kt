package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.java.adapter.NearByAdapter
import com.mapmyindia.sdk.demo.java.utils.CheckInternet
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog
import com.mmi.services.api.nearby.MapmyIndiaNearby
import com.mmi.services.api.nearby.model.NearbyAtlasResponse
import com.mmi.services.api.nearby.model.NearbyAtlasResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by CEINFO on 26-02-2019.
 */
class NearByActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mapmyIndiaMap: MapboxMap? = null
    private var mapView: MapView? = null
    private var transparentProgressDialog: TransparentProgressDialog? = null
    private var recyclerView: RecyclerView? = null
    private var count = 0
    private var floatingActionButton: FloatingActionButton? = null
    private var mLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.near_by_activity)
        mapView = findViewById(R.id.map_view)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)
        Toast.makeText(this, "Please click on map to get nearby.", Toast.LENGTH_SHORT).show()
        recyclerView = findViewById(R.id.nearByRecyclerview)
        mLayoutManager = LinearLayoutManager(this@NearByActivity)
        recyclerView!!.layoutManager = mLayoutManager

        floatingActionButton = findViewById(R.id.marker_list)
        floatingActionButton!!.setImageResource(R.drawable.location_pointer)
        count = 0
        floatingActionButton!!.setOnClickListener { v ->
            if (count == 0) {
                count = 1
                floatingActionButton!!.setImageResource(R.drawable.listing_option)
                recyclerView!!.visibility = View.GONE
            } else {
                count = 0
                floatingActionButton!!.setImageResource(R.drawable.location_pointer)
                recyclerView!!.visibility = View.VISIBLE
            }
        }

        transparentProgressDialog = TransparentProgressDialog(this, R.drawable.circle_loader, "")
    }

    override fun onMapReady(mapmyIndiaMap: MapboxMap) {
        this.mapmyIndiaMap = mapmyIndiaMap






        mapmyIndiaMap.setPadding(20, 20, 20, 20)


        mapmyIndiaMap.cameraPosition = setCameraAndTilt()
        mapmyIndiaMap.addOnMapClickListener { latLng ->
            mapmyIndiaMap.clear()
            if (CheckInternet.isNetworkAvailable(this@NearByActivity)) {
                getNearBy(latLng.latitude, latLng.longitude)
            } else {
                Toast.makeText(this@NearByActivity, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapError(i: Int, s: String) {

    }

    private fun setCameraAndTilt(): CameraPosition {
        return CameraPosition.Builder().target(LatLng(
                28.551087, 77.257373)).zoom(14.0).tilt(0.0).build()
    }

    protected fun progressDialogShow() {
        transparentProgressDialog!!.show()
    }

    protected fun progressDialogHide() {
        transparentProgressDialog!!.dismiss()
    }

    private fun getNearBy(latitude: Double, longitude: Double) {
        mapmyIndiaMap!!.clear()
        progressDialogShow()
        MapmyIndiaNearby.builder()
                .setLocation(latitude, longitude)
                .keyword("Tea")
                .build()
                .enqueueCall(object : Callback<NearbyAtlasResponse> {
                    override fun onResponse(call: Call<NearbyAtlasResponse>, response: Response<NearbyAtlasResponse>) {

                        if (response.code() == 200) {
                            if (response.body() != null) {
                                val nearByList = response.body()!!.suggestedLocations
                                if (nearByList.size > 0) {
                                    addMarker(nearByList)
                                }
                            } else {
                                Toast.makeText(this@NearByActivity, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@NearByActivity, response.message(), Toast.LENGTH_LONG).show()
                        }

                        progressDialogHide()
                    }

                    override fun onFailure(call: Call<NearbyAtlasResponse>, t: Throwable) {
                        progressDialogHide()
                    }
                })
    }


    private fun addMarker(nearByList: ArrayList<NearbyAtlasResult>) {
        for (marker in nearByList) {
            mapmyIndiaMap!!.addMarker(MarkerOptions().position(LatLng(marker.latitude, marker.longitude)).title(marker.placeName))
        }

        recyclerView!!.adapter = NearByAdapter(nearByList)
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