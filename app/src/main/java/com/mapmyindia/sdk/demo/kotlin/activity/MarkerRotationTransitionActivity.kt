package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityMarkerRotationTransitionBinding
import com.mapmyindia.sdk.demo.databinding.BaseLayoutBinding
import com.mapmyindia.sdk.demo.kotlin.plugin.MarkerPlugin
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.geometry.LatLngBounds

/**
 * Created by Saksham on 3/9/19.
 */
class MarkerRotationTransitionActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    private lateinit var mBinding: ActivityMarkerRotationTransitionBinding
    private var mapmyIndiaMap: MapmyIndiaMap? = null
    private val latLngStart: LatLng = LatLng(28.705436, 77.100462)
    private val latLngEnd: LatLng = LatLng(28.703800, 77.101818)
    private var markerPlugin: MarkerPlugin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_marker_rotation_transition)

        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)

        mBinding.markerRotate.setOnClickListener(this)
        mBinding.markerTransition.setOnClickListener(this)
    }
    override fun onMapError(p0: Int, p1: String?) {

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap

        val latLngBounds: LatLngBounds = LatLngBounds.Builder()
                .include(latLngStart)
                .include(latLngEnd)
                .build()

        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100))

        mapmyIndiaMap.getStyle {
            initMarker()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initMarker() {
        markerPlugin = MarkerPlugin(mapmyIndiaMap!!, mBinding.mapView)
        markerPlugin?.icon = resources?.getDrawable(R.drawable.placeholder,null)
        markerPlugin?.addMarker(latLngStart)
        markerPlugin?.addTitle("Title")
        markerPlugin?.addDescription("Description")
    }

    override fun onClick(view: View?) {
        when(view!!.id) {
            R.id.marker_rotate ->
                if(markerPlugin != null) {
                    markerPlugin!!.startRotation()
                }

            R.id.marker_transition ->
                if(markerPlugin != null) {
                    markerPlugin!!.startTransition(latLngStart, latLngEnd)
                }
        }
    }

    override fun onStart() {
        super.onStart()
        mBinding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mBinding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.mapView.onDestroy()
        if (markerPlugin != null) {
            markerPlugin!!.removeCallbacks()
        }
    }

    override fun onPause() {
        super.onPause()
        mBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mBinding.mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.mapView.onSaveInstanceState(outState)
    }

}