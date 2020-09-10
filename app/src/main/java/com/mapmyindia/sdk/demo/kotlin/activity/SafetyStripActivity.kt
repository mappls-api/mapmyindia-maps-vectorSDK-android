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
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R

class SafetyStripActivity : AppCompatActivity(), OnMapReadyCallback, LocationEngineListener {

    private lateinit var mapView: MapView
    private lateinit var btnShowStrip: Button
    private lateinit var btnHideStrip: Button
    private var mapmyIndiaMap: MapboxMap? = null
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
                    if(granted) {
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
    override fun onMapReady(mapmyIndiaMap: MapboxMap?) {
        this.mapmyIndiaMap = mapmyIndiaMap;
        locationEngine = LocationEngineProvider(this).obtainBestLocationEngineAvailable()
        locationEngine?.priority = LocationEnginePriority.HIGH_ACCURACY
        locationEngine?.fastestInterval = 1000
        locationEngine?.addLocationEngineListener(this)
        locationEngine?.activate()
        val padding: IntArray
        padding = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            intArrayOf(0, 750, 0, 0)
        } else {
            intArrayOf(0, 250, 0, 0)
        }

        val options = LocationComponentOptions.builder(this)
                .padding(padding)
                .layerBelow("waterway-label")
                .build()

        locationComponent = mapmyIndiaMap?.locationComponent
        locationComponent?.activateLocationComponent(this, locationEngine, options)
        locationComponent?.isLocationComponentEnabled = true
        val location: Location? = locationComponent?.lastKnownLocation
        if(location != null) {
            mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 14.0))
        }
        findViewById<View>(R.id.btn_layout).visibility = View.VISIBLE
        mapmyIndiaMap?.uiSettings?.safetyStripGravity = Gravity.TOP
    }

    override fun onLocationChanged(location: Location?) {
        if(location == null) {
            // no impl
            return
        }
        mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 14.0))

    }

    @SuppressLint("MissingPermission")
    override fun onConnected() {
        locationEngine?.requestLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        mapView.onStart()
        locationEngine?.addLocationEngineListener(this)
        if (locationEngine?.isConnected?:false) {
            locationEngine?.requestLocationUpdates()
        } else {
            locationEngine?.activate()
        }
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        locationEngine?.removeLocationEngineListener(this)
        locationEngine?.removeLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        locationEngine?.deactivate()
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