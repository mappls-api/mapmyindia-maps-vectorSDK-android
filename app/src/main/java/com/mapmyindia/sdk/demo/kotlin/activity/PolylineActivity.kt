package com.mapmyindia.sdk.demo.kotlin.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityPolylineBinding
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.annotations.Polyline
import com.mapmyindia.sdk.maps.annotations.PolylineOptions
import com.mapmyindia.sdk.maps.camera.CameraPosition
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.geometry.LatLngBounds
import java.util.*

/**
 * Created by CEINFO on 26-02-2019.
 */
class PolylineActivity : AppCompatActivity(), OnMapReadyCallback {

    private val listOfLatlang = ArrayList<LatLng>()
    private var polyLine: Polyline?=null
    private lateinit var mBinding:ActivityPolylineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_polyline)
        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)
    }

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        mapmyIndiaMap.cameraPosition = setCameraAndTilt()






        mapmyIndiaMap.setPadding(20, 20, 20, 20)


        listOfLatlang.add(LatLng(28.705436, 77.100462))
        listOfLatlang.add(LatLng(28.705191, 77.100784))
        listOfLatlang.add(LatLng(28.704646, 77.101514))
        listOfLatlang.add(LatLng(28.704194, 77.101171))
        listOfLatlang.add(LatLng(28.704083, 77.101066))
        listOfLatlang.add(LatLng(28.703900, 77.101318))

        /* this is done for animating/moving camera to particular position */

        val latLngBounds = LatLngBounds.Builder().includes(listOfLatlang).build()
        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 70))

        mBinding.btnAddPolyline.setOnClickListener(View.OnClickListener {
            polyLine =   mapmyIndiaMap.addPolyline(PolylineOptions().addAll(listOfLatlang).color(Color.parseColor("#3bb2d0")).width(4f))
            mBinding.btnAddPolyline.visibility= View.GONE
            mBinding.btnRemovePolyline.visibility= View.VISIBLE

        })
        mBinding.btnRemovePolyline.setOnClickListener(View.OnClickListener {
            mapmyIndiaMap.removePolyline(polyLine!!)
            mBinding.btnAddPolyline.visibility= View.VISIBLE
            mBinding.btnRemovePolyline.visibility= View.GONE

        })

    }

    fun setCameraAndTilt(): CameraPosition {
        return CameraPosition.Builder().target(LatLng(
                28.551087, 77.257373)).zoom(14.0).tilt(0.0).build()
    }


    override fun onMapError(i: Int, s: String) {

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