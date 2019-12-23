package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R


/**
 * Created by CEINFO on 26-02-2019.
 */
class CameraActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener, MapboxMap.OnCameraMoveListener, MapboxMap.OnCameraIdleListener, MapboxMap.OnCameraMoveCanceledListener {

    private var mapboxMap: MapboxMap? = null
    private var mapView: MapView? = null
    private var moveCamera: TextView? = null
    private var easeCamera: TextView? = null
    private var animateCamera: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_activity)
        mapView = findViewById(R.id.map_view)
        mapView!!.onCreate(savedInstanceState)
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

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap

        mapboxMap.setMinZoomPreference(4.5)
        mapboxMap.setMaxZoomPreference(18.5)

        mapboxMap.setPadding(20, 20, 20, 20)

        val cameraPosition = CameraPosition.Builder().target(LatLng(
                25.321684, 82.987289)).zoom(14.0).tilt(0.0).build()
        mapboxMap.cameraPosition = cameraPosition

        //Map related listeners
        mapboxMap.addOnCameraMoveListener(this)
        mapboxMap.addOnCameraIdleListener(this)
        mapboxMap.addOnCameraMoveCancelListener(this)
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
            R.id.moveCamera -> mapboxMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
                    22.553147478403194,
                    77.23388671875), 14.0))
            R.id.easeCamera -> mapboxMap!!.easeCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
                    28.704268, 77.103045), 14.0))
            R.id.animateCamera -> mapboxMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
                    28.698791, 77.121243), 14.0))
        }
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }
}