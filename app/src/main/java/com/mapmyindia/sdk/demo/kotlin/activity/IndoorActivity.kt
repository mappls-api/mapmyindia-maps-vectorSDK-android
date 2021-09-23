package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityAddMarkerBinding
import com.mapmyindia.sdk.demo.databinding.IndoorLayoutBinding
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.annotations.Icon
import com.mapmyindia.sdk.maps.annotations.IconFactory
import com.mapmyindia.sdk.maps.annotations.MarkerOptions
import com.mapmyindia.sdk.maps.camera.CameraPosition
import com.mapmyindia.sdk.maps.geometry.LatLng


/**
 * Created by Saksham on 4/12/19.
 */
class IndoorActivity : AppCompatActivity(), OnMapReadyCallback {

    private var map: MapmyIndiaMap? = null
    private lateinit var mBinding :IndoorLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.indoor_layout)
        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)

    }

    override fun onMapError(i: Int, err: String?) {

    }

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        map = mapmyIndiaMap
        //To turn on layer control
        mapmyIndiaMap.uiSettings?.isLayerControlEnabled = true



        val iconFactory: IconFactory = IconFactory.getInstance(this)
        val icon: Icon = iconFactory.fromResource(R.drawable.placeholder)
        mapmyIndiaMap.addMarker(MarkerOptions()
                .position(LatLng(28.5425071, 77.1560724))
                .icon(icon))

        val cameraPosition: CameraPosition = CameraPosition.Builder()
                .target(LatLng(28.5425071, 77.1560724))
                .zoom(16.0)
                .tilt(0.0)
                .build()

        mapmyIndiaMap.cameraPosition = cameraPosition
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