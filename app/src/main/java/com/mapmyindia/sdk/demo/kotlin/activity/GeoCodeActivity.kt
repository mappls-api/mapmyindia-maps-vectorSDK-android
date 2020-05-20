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
import com.mmi.services.api.geocoding.GeoCodeResponse
import com.mmi.services.api.geocoding.MapmyIndiaGeoCoding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by CEINFO on 26-02-2019.
 */
class GeoCodeActivity : AppCompatActivity(), OnMapReadyCallback {


    private var mapmyIndiaMap: MapboxMap? = null
    private var mapView: MapView? = null
    private var transparentProgressDialog: TransparentProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)
        mapView = findViewById(R.id.map_view)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        transparentProgressDialog = TransparentProgressDialog(this, R.drawable.circle_loader, "")
    }

    override fun onMapReady(mapmyIndiaMap: MapboxMap) {
        this.mapmyIndiaMap = mapmyIndiaMap




        mapmyIndiaMap.setPadding(20, 20, 20, 20)

        mapmyIndiaMap.cameraPosition = setCameraAndTilt()
        if (CheckInternet.isNetworkAvailable(this@GeoCodeActivity)) {
            getGeoCode("saket")
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


    private fun getGeoCode(geocodeText: String) {
        progressDialogShow()
        MapmyIndiaGeoCoding.builder()
                .setAddress(geocodeText)
                .build().enqueueCall(object : Callback<GeoCodeResponse> {
                    override fun onResponse(call: Call<GeoCodeResponse>, response: Response<GeoCodeResponse>) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                val placesList = response.body()!!.results
                                val place = placesList[0]
                                val add = "Latitude: " + place.latitude + " longitude: " + place.longitude
                                addMarker(java.lang.Double.valueOf(place.latitude), java.lang.Double.valueOf(place.longitude))
                                Toast.makeText(this@GeoCodeActivity, add, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@GeoCodeActivity, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@GeoCodeActivity, response.message(), Toast.LENGTH_SHORT).show()
                        }
                        progressDialogHide()
                    }

                    override fun onFailure(call: Call<GeoCodeResponse>, t: Throwable) {
                        Toast.makeText(this@GeoCodeActivity, t.toString(), Toast.LENGTH_SHORT).show()
                        progressDialogHide()
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