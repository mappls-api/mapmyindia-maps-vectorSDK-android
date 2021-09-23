package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.BaseLayoutBinding
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.annotations.MarkerOptions
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.geometry.LatLngBounds

/**
 * Created by Saksham on 2/12/19.
 */
class AddCustomInfoWindowActivity : AppCompatActivity(), OnMapReadyCallback {

    private val latLngList: MutableList<LatLng> = ArrayList()
    private lateinit var mBinding: BaseLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.base_layout)
        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)
        latLngList.add(LatLng(25.321684, 82.987289))
        latLngList.add(LatLng(25.331684, 82.997289))
        latLngList.add(LatLng(25.321684, 82.887289))
        latLngList.add(LatLng(25.311684, 82.987289))
    }

    override fun onMapError(p0: Int, p1: String?) {

    }

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {



        latLngList.forEach {
            mapmyIndiaMap.addMarker(MarkerOptions().position(it).setTitle("XYZ"))
        }

        mapmyIndiaMap.setInfoWindowAdapter {
            val view: View? = LayoutInflater.from(this@AddCustomInfoWindowActivity).inflate(R.layout.custom_info_window_layout, null)
            val textView: TextView = view?.findViewById(R.id.text)!!
            textView.text = it.title
            return@setInfoWindowAdapter view
        }

        mapmyIndiaMap.setOnMarkerClickListener {
            Toast.makeText(this, it.position.toString(), Toast.LENGTH_SHORT).show()
            return@setOnMarkerClickListener false
        }

        val latLngBounds: LatLngBounds = LatLngBounds.Builder()
                .includes(latLngList)
                .build()

        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100, 10, 100, 10))

    }

    override fun onStart() {
        super.onStart()
        mBinding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mBinding.mapView.onResume()
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

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.mapView.onSaveInstanceState(outState)
    }

}