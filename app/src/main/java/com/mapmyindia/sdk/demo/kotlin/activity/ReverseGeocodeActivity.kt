package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.java.utils.CheckInternet
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog
import com.mmi.services.api.PlaceResponse
import com.mmi.services.api.reversegeocode.MapmyIndiaReverseGeoCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by CEINFO on 26-02-2019.
 */
class ReverseGeocodeActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mapmyIndiaMap: MapboxMap? = null
    private var mapView: MapView? = null
    private var transparentProgressDialog: TransparentProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)
        mapView = findViewById(R.id.map_view)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)
        transparentProgressDialog = TransparentProgressDialog(this, R.drawable.circle_loader, "")
    }

    override fun onMapReady(mapmyIndiaMap: MapboxMap) {
        this.mapmyIndiaMap = mapmyIndiaMap
        mapmyIndiaMap.cameraPosition = setCameraAndTilt()

        mapmyIndiaMap.setPadding(20, 20, 20, 20)


        mapmyIndiaMap.addOnMapClickListener { latLng ->
            mapmyIndiaMap.clear()
            if (CheckInternet.isNetworkAvailable(this@ReverseGeocodeActivity)) {
                reverseGeocode(latLng.latitude, latLng.longitude)
                addMarker(latLng.latitude, latLng.longitude)
            } else {
                Toast.makeText(this@ReverseGeocodeActivity, getString(R.string.pleaseCheckInternetConnection), Toast.LENGTH_SHORT).show()
            }
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

    private fun reverseGeocode(latitude: Double?, longitude: Double?) {
        progressDialogShow()
        MapmyIndiaReverseGeoCode.builder()
                .setLocation(latitude!!, longitude!!)
                .build().enqueueCall(object : Callback<PlaceResponse> {
                    override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                val placesList = response.body()!!.places
                                val place = placesList[0]
                                val add = place.formattedAddress
                                Toast.makeText(this@ReverseGeocodeActivity, add, Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@ReverseGeocodeActivity, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@ReverseGeocodeActivity, response.message(), Toast.LENGTH_LONG).show()
                        }

                        progressDialogHide()
                    }

                    override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                        progressDialogHide()
                        Toast.makeText(this@ReverseGeocodeActivity, t.toString(), Toast.LENGTH_LONG).show()
                    }
                })
    }

    private fun addMarker(latitude: Double, longitude: Double) {
        mapmyIndiaMap!!.addMarker(MarkerOptions().position(LatLng(
                latitude, longitude)))
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