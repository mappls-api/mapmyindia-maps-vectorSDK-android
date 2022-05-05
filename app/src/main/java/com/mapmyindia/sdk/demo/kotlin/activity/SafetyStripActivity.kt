package com.mapmyindia.sdk.demo.kotlin.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.location.Location
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.maps.MapView
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.location.LocationComponent
import com.mapmyindia.sdk.maps.location.LocationComponentActivationOptions
import com.mapmyindia.sdk.maps.location.LocationComponentOptions
import com.mapmyindia.sdk.maps.location.engine.*
import com.mapmyindia.sdk.maps.location.permissions.PermissionsListener
import com.mapmyindia.sdk.maps.location.permissions.PermissionsManager
import java.lang.Exception

class SafetyStripActivity : AppCompatActivity(), OnMapReadyCallback, LocationEngineCallback<LocationEngineResult> {

    private lateinit var mapView: MapView
    private lateinit var btnShowStrip: Button
    private lateinit var btnHideStrip: Button
    private var mapmyIndiaMap: MapmyIndiaMap? = null
    private var permissionsManager: PermissionsManager? = null
    private var locationEngine: LocationEngine? = null
    private var locationComponent: LocationComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safety_strip)
        mapView = findViewById(R.id.map_view)
        btnShowStrip = findViewById(R.id.btn_show_strip)
        btnHideStrip = findViewById(R.id.btn_hide_strip)
        mapView.onCreate(savedInstanceState)
        btnHideStrip.isEnabled = false
        btnShowStrip.setOnClickListener {
            btnShowStrip.isEnabled = false
            btnHideStrip.isEnabled = true
            mapmyIndiaMap?.showCurrentLocationSafety()
        }
        btnHideStrip.setOnClickListener {
            btnShowStrip.isEnabled = true
            btnHideStrip.isEnabled = false
            mapmyIndiaMap?.uiSettings?.hideSafetyStrip()
        }
        if(PermissionsManager.areLocationPermissionsGranted(this)) {
            mapView.getMapAsync(this)
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
                    Toast.makeText(this@SafetyStripActivity, "You need to accept location permissions.", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        mapView.getMapAsync(this@SafetyStripActivity)
                    } else {
                        finish()
                    }
                }

            })
            permissionsManager?.requestLocationPermissions(this)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapError(p0: Int, p1: String?) {

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        mapmyIndiaMap.uiSettings?.setLogoMargins(0, 0, 0, 120)
        val padding: IntArray = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            intArrayOf(0, 750, 0, 0)
        } else {
            intArrayOf(0, 250, 0, 0)
        }

        mapmyIndiaMap.getStyle {

            val request = LocationEngineRequest.Builder(100L)
                    .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                    .setMaxWaitTime(5000L).build()

            val options = LocationComponentOptions.builder(this@SafetyStripActivity)
                    .padding(padding)
                    .layerBelow("waterway-label")
                    .build()

            locationComponent = mapmyIndiaMap.locationComponent
            locationEngine = LocationEngineProvider.getBestLocationEngine(this@SafetyStripActivity)

            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this@SafetyStripActivity, it)
                    .locationComponentOptions(options)
                    .locationEngine(locationEngine)
                    .locationEngineRequest(request)
                    .build()
            locationComponent?.activateLocationComponent(locationComponentActivationOptions)
            locationComponent?.isLocationComponentEnabled = true
            val location = locationComponent!!.lastKnownLocation
            if (location != null) {
                mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 14.0))
            }
            locationEngine!!.requestLocationUpdates(request, this@SafetyStripActivity, mainLooper)
            locationEngine!!.getLastLocation(this@SafetyStripActivity)
        }
        findViewById<View>(R.id.btn_layout).visibility = View.VISIBLE
        mapmyIndiaMap.uiSettings?.safetyStripGravity = Gravity.TOP
    }


    override fun onSuccess(p0: LocationEngineResult?) {
        if(p0 != null) {
            val location = p0.lastLocation
                    ?: // no impl
                    return
            mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 14.0))

        }
    }

    override fun onFailure(p0: Exception) {
        p0.stackTrace
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()

    }

    override fun onDestroy() {
        super.onDestroy()
        locationEngine?.removeLocationUpdates(this)
        mapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}