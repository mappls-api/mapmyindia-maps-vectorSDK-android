package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.java.utils.CheckInternet
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog
import com.mapmyindia.sdk.maps.MapView
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.annotations.MarkerOptions
import com.mapmyindia.sdk.maps.camera.CameraPosition
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mmi.services.api.OnResponseCallback
import com.mmi.services.api.PlaceResponse
import com.mmi.services.api.reversegeocode.MapmyIndiaReverseGeoCode
import com.mmi.services.api.reversegeocode.MapmyIndiaReverseGeoCodeManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by CEINFO on 26-02-2019.
 */
class ReverseGeocodeActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mapmyIndiaMap: MapmyIndiaMap? = null
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

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
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
            return@addOnMapClickListener false
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
        val reverseGeoCode = MapmyIndiaReverseGeoCode.builder()
                .setLocation(latitude!!, longitude!!)
                .build()
        MapmyIndiaReverseGeoCodeManager.newInstance(reverseGeoCode).call(object : OnResponseCallback<PlaceResponse> {
            override fun onSuccess(placeResponse: PlaceResponse?) {
                if (placeResponse != null) {
                    val placesList = placeResponse.places
                    val place = placesList[0]
                    val add = place.formattedAddress
                    Toast.makeText(this@ReverseGeocodeActivity, add, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@ReverseGeocodeActivity, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(p0: Int, p1: String?) {
                Toast.makeText(this@ReverseGeocodeActivity, p1, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addMarker(latitude: Double, longitude: Double) {
        mapmyIndiaMap?.addMarker(MarkerOptions().position(LatLng(
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