package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.android.gestures.AndroidGesturesManager
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import kotlinx.android.synthetic.main.base_layout.*

class GesturesActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private var mapmyIndiaMap: MapboxMap? = null
    private var androidGesturesManager: AndroidGesturesManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)
        map_view.onCreate(savedInstanceState)
        mapView = findViewById<MapView>(R.id.map_view) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        androidGesturesManager = AndroidGesturesManager(mapView.context, false)
        addTouchGesture()
    }

    private fun addTouchGesture() {

        mapView.setOnTouchListener { v, event ->
            androidGesturesManager?.onTouchEvent(event)
            return@setOnTouchListener false
        }

        androidGesturesManager?.setMoveGestureListener(object : MoveGestureDetector.OnMoveGestureListener {
            override fun onMoveBegin(detector: MoveGestureDetector?): Boolean {
                if (mapmyIndiaMap != null && detector?.pointersCount == 1) {
                    val latLng = mapmyIndiaMap?.projection?.fromScreenLocation(detector.focalPoint)
                    Toast.makeText(this@GesturesActivity, "onMoveBegin: $latLng", Toast.LENGTH_SHORT).show()
                }

                return true
            }

            override fun onMove(detector: MoveGestureDetector?, distanceX: Float, distanceY: Float): Boolean {
                if (mapmyIndiaMap != null && detector?.pointersCount == 1) {
                    val latLng = mapmyIndiaMap?.projection?.fromScreenLocation(detector.focalPoint)
                    Toast.makeText(this@GesturesActivity, "onMove: $latLng", Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onMoveEnd(detector: MoveGestureDetector?, velocityX: Float, velocityY: Float) {
                if (mapmyIndiaMap != null) {
                    val latLng = mapmyIndiaMap?.projection?.fromScreenLocation(detector!!.focalPoint)
                    Toast.makeText(this@GesturesActivity, "onMoveEnd: $latLng", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    override fun onMapError(p0: Int, p1: String?) {}

    override fun onMapReady(mapmyIndiaMap: MapboxMap?) {
        this.mapmyIndiaMap = mapmyIndiaMap;


        mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(28.0, 77.0), 14.0))
    }

    override fun onStart() {
        super.onStart()
        map_view.onStart()
    }

    override fun onResume() {
        super.onResume()
        map_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view.onPause()
    }

    override fun onStop() {
        super.onStop()
        map_view.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map_view.onSaveInstanceState(outState)
    }
}
