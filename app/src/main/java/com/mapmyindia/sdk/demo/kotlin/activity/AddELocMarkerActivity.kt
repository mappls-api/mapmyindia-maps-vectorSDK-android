package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityAddMarkerBinding
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.annotations.MarkerOptions
import com.mapmyindia.sdk.maps.camera.CameraELocUpdateFactory
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import java.util.*

/**
 ** Created by Saksham on 26-11-2020.
 **/

class AddELocMarkerActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mapmyIndiaMap: MapmyIndiaMap? = null
    private lateinit var mBinding: ActivityAddMarkerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_marker)
        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)
        mBinding.btnSearch.setOnClickListener(View.OnClickListener {
            val eLoc: String = mBinding.etELoc.getText().toString()
            if (!eLoc.isEmpty()) {
                val eLocList = eLoc.split(",".toRegex())
                addMarker(eLocList)
            } else {
                Toast.makeText(this@AddELocMarkerActivity, "Please add ELoc", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addMarker(eLocList: List<String>) {
        if (mapmyIndiaMap != null) {
            mapmyIndiaMap?.clear()
            val markerOptions: MutableList<MarkerOptions> = ArrayList()
            val eLocs: MutableList<String> = ArrayList()
            for (eLoc in eLocList) {
                markerOptions.add(MarkerOptions().eLoc(eLoc).title(eLoc))
                eLocs.add(eLoc)
            }
            mapmyIndiaMap?.addMarkers(markerOptions, object : MapmyIndiaMap.OnMarkerAddedListener {
                override fun onSuccess() {
                    Toast.makeText(this@AddELocMarkerActivity, "Marker Added Successfully", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure() {
                    Toast.makeText(this@AddELocMarkerActivity, "Invalid ELoc", Toast.LENGTH_SHORT).show()
                }
            })
            if (eLocs.size > 0) {
                if (eLocs.size == 1) {
                    mapmyIndiaMap?.animateCamera(CameraELocUpdateFactory.newELocZoom(eLocs[0], 16.0))
                } else {
                    mapmyIndiaMap?.animateCamera(CameraELocUpdateFactory.newELocBounds(eLocs, 10, 100, 10, 10))
                }
            }

//            mapmyIndiaMap.showMarkers(markers, 10, 100, 10, 10);
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

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap
        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(28.0, 77.0), 5.0))
        mBinding.layoutEloc.visibility = View.VISIBLE
    }

    override fun onMapError(i: Int, s: String?) {}
}