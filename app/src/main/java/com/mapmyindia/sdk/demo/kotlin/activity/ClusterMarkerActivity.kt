package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityClusterMarkerBinding
import com.mapmyindia.sdk.demo.kotlin.plugin.ClusterMarkerPlugin
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.geometry.LatLngBounds

class ClusterMarkerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mBinding: ActivityClusterMarkerBinding
    private val latLngList = listOf(
        LatLng(28.551635, 77.268805),
        LatLng(28.551041, 77.267979),
        LatLng(28.552115, 77.265833),
        LatLng(28.559786, 77.238859),
        LatLng(28.561535, 77.233345),
        LatLng(28.562469, 77.235072),
        LatLng(28.435931, 77.304689),
        LatLng(28.436214, 77.304936),
        LatLng(28.438827, 77.308337),
        LatLng(28.489028, 77.091252),
        LatLng(28.486831, 77.094492),
        LatLng(28.486491, 77.094374),
        LatLng(28.491510, 77.082149),
        LatLng(28.474800, 77.065233),
        LatLng(28.471245, 77.072722),
        LatLng(28.458440, 77.073179)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cluster_marker)
        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        mBinding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        mBinding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mBinding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.mapView.onDestroy()
    }

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        mapmyIndiaMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                LatLngBounds.Builder().includes(latLngList).build(), 20, 20, 20, 20
            )
        )
        val clusterMarkerPlugin = ClusterMarkerPlugin(mBinding.mapView, mapmyIndiaMap)
        clusterMarkerPlugin.setMarkers(latLngList)
    }

    override fun onMapError(p0: Int, p1: String?) {

    }
}