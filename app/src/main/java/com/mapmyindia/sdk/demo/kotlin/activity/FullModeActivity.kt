package com.mapmyindia.sdk.demo.kotlin.activity

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapmyindia.sdk.plugins.places.common.PlaceConstants
import com.mmi.services.api.autosuggest.model.ELocation

class FullModeActivity : AppCompatActivity() , OnMapReadyCallback, PermissionsListener, LocationEngineListener {

    private lateinit var mapView: MapView
    private var mapmyIndiaMap: MapboxMap? = null
    private var permissionManager: PermissionsManager? = null
    private var locationComponent: LocationComponent? = null
    private var locationEngine: LocationEngine? = null


    private lateinit var search: TextView

    private var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_mode_fragment)
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        search = findViewById(R.id.search)
        search.setOnClickListener {
            if (location != null) {
                val placeOptions: PlaceOptions = PlaceOptions.builder()
                        .location(Point.fromLngLat(location?.longitude!!, location?.latitude!!))
                        .backgroundColor(ContextCompat.getColor(this, android.R.color.white))
                        .build()

                val placeAutocompleteActivity: Intent = PlaceAutocomplete.IntentBuilder()
                        .placeOptions(placeOptions)
                        .build(this)
                startActivityForResult(placeAutocompleteActivity, 101)
            } else {
                Toast.makeText(this,"Please wait for getting current location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 101) {
            if(resultCode == Activity.RESULT_OK) {
                val eLocation : ELocation = Gson().fromJson(data?.getStringExtra(PlaceConstants.RETURNING_ELOCATION_DATA), ELocation::class.java)
                if (mapmyIndiaMap != null) {
                    mapmyIndiaMap?.clear()
                    val latLng = LatLng(eLocation.latitude?.toDouble()!!, eLocation.longitude?.toDouble()!!)
                    mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0))
                    mapmyIndiaMap?.addMarker(MarkerOptions().position(latLng).setTitle(eLocation.placeName).setSnippet(eLocation.placeAddress))
                }
                search.text = eLocation.placeName
            }
        }
    }

    override fun onMapError(p0: Int, p1: String?) {}

    override fun onMapReady(mapmyIndiaMap: MapboxMap?) {
        this.mapmyIndiaMap = mapmyIndiaMap

        mapmyIndiaMap?.setPadding(20, 20, 20, 20)

        mapmyIndiaMap!!.setMinZoomPreference(4.0)
        mapmyIndiaMap.setMaxZoomPreference(18.0)

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            enableLocation()
        } else {
            permissionManager = PermissionsManager(this)
            permissionManager?.requestLocationPermissions(this)
        }
    }

    private fun enableLocation() {
        val options: LocationComponentOptions = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.colorAccent))
                .build()
        locationComponent = mapmyIndiaMap?.locationComponent
        locationComponent?.activateLocationComponent(this, options)
        locationComponent?.isLocationComponentEnabled = true
        locationEngine = locationComponent?.locationEngine!!
        locationEngine?.addLocationEngineListener(this)
        locationComponent?.cameraMode = CameraMode.TRACKING
        locationComponent?.renderMode = RenderMode.COMPASS
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        if (locationEngine != null) {
            locationEngine?.removeLocationEngineListener(this)
            locationEngine?.addLocationEngineListener(this)
        }
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (locationEngine != null)
            locationEngine?.removeLocationEngineListener(this)
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        if (locationEngine != null) {
            locationEngine?.removeLocationEngineListener(this)
            locationEngine?.removeLocationUpdates()
        }
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationEngine?.deactivate()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {

    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocation()
        }
    }

    override fun onLocationChanged(location: Location?) {
        this.location = location
    }

    override fun onConnected() {
        locationEngine?.requestLocationUpdates()
    }
}

