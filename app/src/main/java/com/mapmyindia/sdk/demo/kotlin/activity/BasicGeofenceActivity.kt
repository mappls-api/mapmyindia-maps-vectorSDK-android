package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.LayoutBasicGeofenceBinding
import com.mapmyindia.sdk.geofence.ui.GeoFence
import com.mapmyindia.sdk.geofence.ui.listeners.GeoFenceViewCallback

class BasicGeofenceActivity: AppCompatActivity(), GeoFenceViewCallback {

    private lateinit  var mBinding: LayoutBasicGeofenceBinding
    private lateinit var geoFence: GeoFence

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         mBinding = DataBindingUtil.setContentView(this, R.layout.layout_basic_geofence)
         geoFence =  GeoFence()
         geoFence.isPolygon = false
         geoFence.circleCenter = LatLng(24.6496185, 77.3062072)
         geoFence.circleRadius = 200

        mBinding.geofenceView.geoFence = geoFence
        mBinding.geofenceView.onCreate(savedInstanceState)
        mBinding.geofenceView.setGeoFenceViewCallback(this)
    }


    override fun onStart() {
        super.onStart()
        mBinding.geofenceView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mBinding.geofenceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.geofenceView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.geofenceView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.geofenceView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.geofenceView.onDestroy()
    }

    override fun onCircleRadiusChanging(p0: Int) {

    }

    override fun onUpdateGeoFence(p0: GeoFence?) {

    }

    override fun onGeoFenceReady(p0: MapboxMap?) {

    }

    override fun hasIntersectionPoints() {

    }

    override fun geoFenceType(p0: Boolean) {

    }


}