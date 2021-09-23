package com.mapmyindia.sdk.demo.kotlin.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityLocationCameraOptionsBinding
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.Style
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.location.LocationComponent
import com.mapmyindia.sdk.maps.location.LocationComponentActivationOptions
import com.mapmyindia.sdk.maps.location.LocationComponentOptions
import com.mapmyindia.sdk.maps.location.OnCameraTrackingChangedListener
import com.mapmyindia.sdk.maps.location.engine.LocationEngine
import com.mapmyindia.sdk.maps.location.engine.LocationEngineCallback
import com.mapmyindia.sdk.maps.location.engine.LocationEngineRequest
import com.mapmyindia.sdk.maps.location.engine.LocationEngineResult
import com.mapmyindia.sdk.maps.location.modes.CameraMode
import com.mapmyindia.sdk.maps.location.modes.RenderMode
import java.lang.Exception


class LocationCameraActivity : AppCompatActivity(), OnMapReadyCallback, LocationEngineCallback<LocationEngineResult>, OnCameraTrackingChangedListener {
    private lateinit var mBinding:ActivityLocationCameraOptionsBinding
    private var mapmyIndiaMap: MapmyIndiaMap? = null
    private lateinit var locationComponent: LocationComponent
    private var locationEngine: LocationEngine? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_location_camera_options)

        mBinding.addMapView.getMapAsync(this)
        mBinding.addMapView.onCreate(savedInstanceState)
        setButtonOnClickListener()
    }

    override fun onMapError(p0: Int, p1: String?) {
    }

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap
        mapmyIndiaMap.getStyle {
            enableLocation(it)
        }

    }

    fun enableLocation(style: Style) {
        val options: LocationComponentOptions = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .build()
        locationComponent = mapmyIndiaMap?.locationComponent!!
        val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, style)
                .locationComponentOptions(options)
                .build()
        locationComponent.activateLocationComponent(locationComponentActivationOptions)
        locationComponent.addOnCameraTrackingChangedListener(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationComponent.isLocationComponentEnabled = true
        locationEngine = locationComponent.locationEngine!!
        val request = LocationEngineRequest.Builder(1000)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .build()
        locationEngine?.requestLocationUpdates(request, this, mainLooper)
        locationEngine?.getLastLocation(this)
        locationComponent.cameraMode = CameraMode.TRACKING
        locationComponent.renderMode = RenderMode.COMPASS
    }

    override fun onStart() {
        super.onStart()
        mBinding.addMapView.onStart();
    }

    override fun onResume() {
        super.onResume()

        mBinding.addMapView.onResume();

    }

    override fun onPause() {
        super.onPause()
        mBinding.addMapView.onPause();
    }

    override fun onStop() {
        super.onStop()
        mBinding.addMapView.onStop();
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.addMapView.onDestroy();
        if (locationEngine != null) {
            locationEngine?.removeLocationUpdates(this)
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.addMapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.addMapView.onSaveInstanceState(outState)
    }

    override fun onSuccess(p0: LocationEngineResult?) {
        if(p0 != null) {
            val location = p0.lastLocation
            mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude, location.longitude), 16.0))
        }
    }

    override fun onFailure(p0: Exception) {
        p0.stackTrace
    }



    private fun setButtonOnClickListener() {
        mBinding.btnLocationMode.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val popupMenu = PopupMenu(this@LocationCameraActivity, mBinding.btnLocationMode)
                popupMenu.menuInflater.inflate(R.menu.location_mode_menu, popupMenu.menu)
                popupMenu.gravity = Gravity.BOTTOM
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    mBinding.btnLocationMode.text = item.title.toString()
                    setRenderMode(item.title.toString())
                    true
                })

                popupMenu.show()

            }
        })
        mBinding.btnTracking.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val popupMenu = PopupMenu(this@LocationCameraActivity, mBinding.btnTracking)
                popupMenu.menuInflater.inflate(R.menu.tracking_mode_menu, popupMenu.menu)
                popupMenu.gravity = Gravity.BOTTOM
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    mBinding.btnTracking.text = item.title.toString()
                    setCameraMode(item.title.toString())
                    true
                })

                popupMenu.show()

            }
        })
    }

    fun setRenderMode(mode: String) {
        if (mode.equals("normal", ignoreCase = true)) {
            locationComponent.renderMode = RenderMode.NORMAL
        } else if (mode.equals("compass", ignoreCase = true)) {
            locationComponent.renderMode = RenderMode.COMPASS
        } else if (mode.equals("GPS", ignoreCase = true)) {
            locationComponent.renderMode = RenderMode.GPS
        } else
            locationComponent.renderMode = RenderMode.NORMAL

    }

    private fun setCameraMode(mode: String) {
        if (mode.equals("None", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.NONE
        } else if (mode.equals("None compass", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.NONE_COMPASS
        } else if (mode.equals("None gps", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.NONE_GPS
        } else if (mode.equals("Tracking", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.TRACKING
        } else if (mode.equals("Tracking Compass", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.TRACKING_COMPASS
        } else if (mode.equals("Tracking GPS", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.TRACKING_GPS
        } else if (mode.equals("Tracking GPS North", ignoreCase = true)) {
            locationComponent.cameraMode = CameraMode.TRACKING_GPS_NORTH
        } else locationComponent.cameraMode = CameraMode.TRACKING
    }

    override fun onCameraTrackingChanged(currentMode: Int) {
        if (currentMode == CameraMode.NONE) {
            mBinding.btnTracking.text = "None"
        } else if (currentMode == CameraMode.TRACKING) {
            mBinding.btnTracking.text = "Tracking"
        } else if (currentMode == CameraMode.TRACKING_COMPASS) {
            mBinding.btnTracking.text = "Tracking Compass"
        } else if (currentMode == CameraMode.TRACKING_GPS) {
            mBinding.btnTracking.text = "Tracking GPS"
        } else if (currentMode == CameraMode.TRACKING_GPS_NORTH) {
            mBinding.btnTracking.text = "Tracking GPS North"
        }
    }


    override fun onCameraTrackingDismissed() {
        mBinding.btnTracking.text = "None"
    }
}

