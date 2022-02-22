package com.mapmyindia.sdk.demo.kotlin.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityDrivingRangeBinding
import com.mapmyindia.sdk.demo.kotlin.settings.MapmyIndiaDrivingRangeSetting
import com.mapmyindia.sdk.drivingrange.*
import com.mapmyindia.sdk.geojson.Point
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.Style
import com.mapmyindia.sdk.maps.camera.CameraPosition
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.location.LocationComponentActivationOptions
import com.mapmyindia.sdk.maps.location.LocationComponentOptions
import com.mapmyindia.sdk.maps.location.engine.LocationEngineCallback
import com.mapmyindia.sdk.maps.location.engine.LocationEngineResult
import com.mapmyindia.sdk.maps.location.permissions.PermissionsListener
import com.mapmyindia.sdk.maps.location.permissions.PermissionsManager
import java.lang.Exception

class DrivingRangeActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {

    private lateinit var mPermissionsManager: PermissionsManager
    private var mMapmyIndiaMap: MapmyIndiaMap? = null
    private var mapmyIndiaDrivingRangePlugin: MapmyIndiaDrivingRangePlugin? = null
    private lateinit var mBinding: ActivityDrivingRangeBinding
    private var isLocationCall: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_driving_range)
        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mBinding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.mapView.onPause()
    }

    override fun onStart() {
        super.onStart()
        mBinding.mapView.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mBinding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.mapView.onLowMemory()
    }

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        mMapmyIndiaMap = mapmyIndiaMap

        mapmyIndiaDrivingRangePlugin = MapmyIndiaDrivingRangePlugin(
            mBinding.mapView, mMapmyIndiaMap!!
        )
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            mapmyIndiaMap.getStyle {
                enableLocation(it)
            }


        } else {
            mPermissionsManager = PermissionsManager(this)
            mPermissionsManager.requestLocationPermissions(this)
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocation(style: Style) {
        val options: LocationComponentOptions = LocationComponentOptions.builder(this)
            .trackingGesturesManagement(true)
            .build()
        val locationComponentActivationOptions =
            LocationComponentActivationOptions.builder(this, style)
                .locationComponentOptions(options)
                .build()
        mMapmyIndiaMap?.locationComponent?.activateLocationComponent(
            locationComponentActivationOptions
        )
        mMapmyIndiaMap?.locationComponent?.isLocationComponentEnabled = true
        if (MapmyIndiaDrivingRangeSetting.instance.isUsingCurrentLocation) {
            mMapmyIndiaMap?.locationComponent?.locationEngine?.getLastLocation(object :
                LocationEngineCallback<LocationEngineResult> {
                override fun onSuccess(p0: LocationEngineResult?) {
                    if (p0 == null || p0.lastLocation == null) {
                        return
                    }
                    if (isLocationCall) {
                        return
                    }
                    isLocationCall = true
                    val location = p0.lastLocation!!
                    drawDrivingRange(Point.fromLngLat(location.longitude, location.latitude))

                }

                override fun onFailure(p0: Exception) {
                    p0.stackTrace
                }
            })
            if (!isLocationCall) {
                val location = mMapmyIndiaMap?.locationComponent?.lastKnownLocation
                if (location != null) {
                    isLocationCall = true;
                    drawDrivingRange(Point.fromLngLat(location.longitude, location.latitude))
                }
            }
        } else {
            drawDrivingRange(MapmyIndiaDrivingRangeSetting.instance.location)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        mPermissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun drawDrivingRange(location: Point) {
        mMapmyIndiaMap?.cameraPosition =
            CameraPosition.Builder().target(LatLng(location.latitude(), location.longitude()))
                .zoom(12.0).build()

        val speed: MapmyIndiaDrivingRangeSpeed = if(MapmyIndiaDrivingRangeSetting.instance.speedType == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL) {
            MapmyIndiaDrivingRangeSpeed.optimal()
        } else if(MapmyIndiaDrivingRangeSetting.instance.predectiveType == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT){
            MapmyIndiaDrivingRangeSpeed.predictiveSpeedFromCurrentTime()
        } else {
            MapmyIndiaDrivingRangeSpeed.predictiveSpeedFromCustomTime(MapmyIndiaDrivingRangeSetting.instance.time)
        }

        val option = MapmyIndiaDrivingRangeOption.builder()
            .location(location)
            .rangeTypeInfo(
                MapmyIndiaDrivingRangeTypeInfo.builder()
                    .rangeType(MapmyIndiaDrivingRangeSetting.instance.rangeType)
                    .contours(
                        listOf(
                            MapmyIndiaDrivingRangeContour.builder()
                                .value(MapmyIndiaDrivingRangeSetting.instance.contourValue)
                                .color(MapmyIndiaDrivingRangeSetting.instance.contourColor)
                                .build()
                        )
                    ).build()
            )
            .drivingProfile(MapmyIndiaDrivingRangeSetting.instance.drivingProfile)
            .showLocations(MapmyIndiaDrivingRangeSetting.instance.isShowLocations)
            .isForPolygons(MapmyIndiaDrivingRangeSetting.instance.isForPolygon)
            .denoise(MapmyIndiaDrivingRangeSetting.instance.denoise)
            .generalize(MapmyIndiaDrivingRangeSetting.instance.generalize)
            .speedTypeInfo(speed)
            .build()
        mapmyIndiaDrivingRangePlugin?.drawDrivingRange(option, object :
            IDrivingRangeListener {
            override fun onSuccess() {
                Toast.makeText(this@DrivingRangeActivity, "Success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(code: Int, message: String) {
                Toast.makeText(this@DrivingRangeActivity, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onMapError(p0: Int, p1: String?) {

    }

    override fun onExplanationNeeded(p0: MutableList<String>?) {

    }

    override fun onPermissionResult(p0: Boolean) {
        if (p0) {
            mMapmyIndiaMap?.getStyle {
                enableLocation(it)
            }
        }

    }
}