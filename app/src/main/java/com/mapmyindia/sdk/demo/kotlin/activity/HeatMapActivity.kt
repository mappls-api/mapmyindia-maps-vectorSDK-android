package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.plugin.HeatMapPlugin

class HeatMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private var heatMapOptionList: MutableList<HeatMapPlugin.HeatMapOption> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_layout)
        mapView = findViewById<MapView>(R.id.map_view) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)


        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(77.5129, 28.1016), 2.3));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(77.5132, 28.1021), 2.0));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(76.4048, 28.1224), 1.7));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(76.4052, 28.1232), 1.7));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(77.3597, 28.0781), 1.6));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(77.3597, 28.0781), 1.6));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(74.789, 28.1725), 2.4));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat( 77.512, 28.0879), 1.8));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(74.2832, 28.674242), 1.3));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(80.8244288, 24.6778728), 2.4));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(81.637417, 24.6778728), 3.2));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(77.7372706, 27.8302397), 1.2));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(79.2973292, 28.3729432), 4.2));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(79.7477686, 26.2351935), 2.13));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(80.1762354, 26.1760509), 3.2));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(79.7367823, 27.333618), 1.2));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(80.9452784, 26.628706), 5.2));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(78.6930811, 28.9032672), 3.2));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(78.73702641, 29.3255866), 4.0));
        heatMapOptionList.add(HeatMapPlugin.HeatMapOption(Point.fromLngLat(79.4181788, 29.5838759), 5.0))

    }

    override fun onMapError(p0: Int, p1: String?) {}

    override fun onMapReady(mapmyIndiaMap: MapboxMap?) {



        val heatMapPlugin: HeatMapPlugin = HeatMapPlugin.builder(mapmyIndiaMap!!, mapView)
                .addAll(heatMapOptionList)
                .build()
        heatMapPlugin.addHeatmap()
        mapmyIndiaMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(28.0, 77.0), 4.0))
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
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