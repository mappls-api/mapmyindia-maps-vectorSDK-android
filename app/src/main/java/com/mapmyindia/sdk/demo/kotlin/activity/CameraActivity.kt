package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.maps.MapView
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.camera.CameraPosition
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng


/**
 * Created by CEINFO on 26-02-2019.
 */
class CameraActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener, MapmyIndiaMap.OnCameraMoveListener, MapmyIndiaMap.OnCameraIdleListener, MapmyIndiaMap.OnCameraMoveCanceledListener {

    private var mapmyIndiaMap: MapmyIndiaMap? = null
    private var mapView: MapView? = null
    private var moveCamera: TextView? = null
    private var easeCamera: TextView? = null
    private var animateCamera: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_activity)
        mapView = findViewById(R.id.map_view)
        mapView?.onCreate(savedInstanceState)
        initReferences()
        initListeners()
    }

    private fun initListeners() {
        mapView!!.getMapAsync(this)

        // Layout related listeners
        moveCamera!!.setOnClickListener(this)
        easeCamera!!.setOnClickListener(this)
        animateCamera!!.setOnClickListener(this)

    }

    private fun initReferences() {
        moveCamera = findViewById(R.id.moveCamera)
        easeCamera = findViewById(R.id.easeCamera)
        animateCamera = findViewById(R.id.animateCamera)
    }

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap


        val cameraPosition = CameraPosition.Builder().target(LatLng(
                25.321684, 82.987289)).zoom(14.0).tilt(0.0).build()
        mapmyIndiaMap.cameraPosition = cameraPosition

        //Map related listeners
        mapmyIndiaMap.addOnCameraMoveListener(this)
        mapmyIndiaMap.addOnCameraIdleListener(this)
        mapmyIndiaMap.addOnCameraMoveCancelListener(this)
    }

    override fun onMapError(i: Int, s: String) {
        // Here , Handle Map related Error on map loading
    }

    override fun onCameraMove() {

    }

    override fun onCameraIdle() {

    }

    override fun onCameraMoveCanceled() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.moveCamera -> mapmyIndiaMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
                    22.553147478403194,
                    77.23388671875), 14.0))
            R.id.easeCamera -> mapmyIndiaMap!!.easeCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
                    28.704268, 77.103045), 14.0))
            R.id.animateCamera -> mapmyIndiaMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
                    28.698791, 77.121243), 14.0))
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}