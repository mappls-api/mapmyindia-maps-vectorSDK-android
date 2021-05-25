package com.mapmyindia.sdk.demo.kotlin.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapbox.mapboxsdk.annotations.Polygon
import com.mapbox.mapboxsdk.annotations.PolygonOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityPolygonBinding
import java.util.*

/**
 * Created by CEINFO on 26-02-2019.
 */
class PolygonActivity : AppCompatActivity(), OnMapReadyCallback {
    private val listOfLatlang = ArrayList<LatLng>()
    private var polygon: Polygon?=null
    private lateinit var mBinding: ActivityPolygonBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_polygon)
        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)
    }

    override fun onMapReady(mapmyIndiaMap: MapboxMap) {

        mapmyIndiaMap.cameraPosition = setCameraAndTilt()
        mapmyIndiaMap.setPadding(20, 20, 20, 20)
        listOfLatlang.add(LatLng(28.703900, 77.101318))
        listOfLatlang.add(LatLng(28.703331, 77.102155))
        listOfLatlang.add(LatLng(28.703905, 77.102761))
        listOfLatlang.add(LatLng(28.704248, 77.102370))



        /* this is done for move camera focus to particular position */
        val latLngBounds = LatLngBounds.Builder().includes(listOfLatlang).build()
        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 70))
        mBinding.btnAddPolygon.setOnClickListener(View.OnClickListener {
            polygon = mapmyIndiaMap.addPolygon(PolygonOptions().addAll(listOfLatlang).fillColor(Color.parseColor("#753bb2d0")))

            mBinding.btnAddPolygon.visibility= View.GONE
            mBinding.btnRemovePolygon.visibility= View.VISIBLE

        })
        mBinding.btnRemovePolygon.setOnClickListener(View.OnClickListener {
            mapmyIndiaMap.removePolygon(polygon!!)
            mBinding.btnAddPolygon.visibility= View.VISIBLE
            mBinding.btnRemovePolygon.visibility= View.GONE

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