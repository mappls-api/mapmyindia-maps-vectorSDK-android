package com.mapmyindia.sdk.demo.kotlin.activity


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng

import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.BaseLayoutBinding
import com.mapmyindia.sdk.plugin.annotation.OnSymbolDragListener
import com.mapmyindia.sdk.plugin.annotation.Symbol
import com.mapmyindia.sdk.plugin.annotation.SymbolManager
import com.mapmyindia.sdk.plugin.annotation.SymbolOptions

/**
 * Created by Saksham on 20/9/19.
 */
class MarkerDraggingActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mBinding:BaseLayoutBinding
    private var mapmyIndiaMap: MapboxMap? = null
    private val latLng = LatLng(28.705436, 77.100462)
    private var symbolManager: SymbolManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= DataBindingUtil.setContentView(this,R.layout.base_layout)

        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)
    }

    override fun onMapReady(mapmyIndiaMap: MapboxMap) {
        this.mapmyIndiaMap = mapmyIndiaMap
        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0))
        initMarker()
    }

    private fun initMarker() {
        symbolManager = SymbolManager( mBinding.mapView, mapmyIndiaMap!!)
        val symbolOptions = SymbolOptions()
                .icon(BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(this, R.drawable.placeholder)))
                .draggable(true)
                .position(latLng)
        symbolManager?.iconAllowOverlap = true
        symbolManager?.iconIgnorePlacement = false
        symbolManager?.create(symbolOptions)
        symbolManager?.addDragListener(object : OnSymbolDragListener {
            override fun onAnnotationDragStarted(p0: Symbol?) {

            }

            override fun onAnnotationDrag(p0: Symbol?) {

            }

            override fun onAnnotationDragFinished(symbol: Symbol?) {
                Toast.makeText(this@MarkerDraggingActivity, symbol?.position.toString(), Toast.LENGTH_SHORT).show()
            }
        })
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
