package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.maps.MapView
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.camera.CameraELocUpdateFactory


/**
 * Created by Saksham on 26-11-2020.
 */
class ELocCameraActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener, MapmyIndiaMap.OnCameraMoveListener, MapmyIndiaMap.OnCameraIdleListener, MapmyIndiaMap.OnCameraMoveCanceledListener {

    private var mapmyIndiaMap: MapmyIndiaMap? = null
    private lateinit var mapView: MapView
    private var moveCamera: TextView? = null
    private var easeCamera: TextView? = null
    private var animateCamera: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_activity)
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        initReferences()
        initListeners()
    }

    private fun initListeners() {
        mapView.getMapAsync(this)

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




        mapmyIndiaMap.setPadding(20, 20, 20, 20)

        mapmyIndiaMap.animateCamera(CameraELocUpdateFactory.newELocZoom("MMI000", 14.0))

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
            R.id.moveCamera -> mapmyIndiaMap?.moveCamera(CameraELocUpdateFactory.newELocZoom("2T7S17", 14.0))
            R.id.easeCamera -> mapmyIndiaMap?.easeCamera(CameraELocUpdateFactory.newELocZoom("5EU4EZ", 14.0))
            R.id.animateCamera -> mapmyIndiaMap?.animateCamera(CameraELocUpdateFactory.newELocZoom("IB3BR9", 14.0))
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}