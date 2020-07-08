package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.plugin.SnakePolyLinePlugin
import com.mmi.services.api.directions.DirectionsCriteria
import com.mmi.services.api.directions.MapmyIndiaDirections
import com.mmi.services.api.directions.models.DirectionsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SnakeMotionPolylineActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    lateinit var mapmyIndiaMap: MapboxMap

    private var snakePolyLinePlugin: SnakePolyLinePlugin? = null

    companion object {
        private val ORIGIN_POINT = Point.fromLngLat(77.2667594, 28.5506561)

        private val DESTINATION_POINT = Point.fromLngLat(77.101318, 28.703900)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snake_motion_polyline)
        mapView = findViewById(R.id.map_view)
        mapView.getMapAsync(this)

    }


    override fun onMapReady(mapmyIndiaMap: MapboxMap?) {
        this.mapmyIndiaMap = mapmyIndiaMap!!
        snakePolyLinePlugin = SnakePolyLinePlugin(mapView, mapmyIndiaMap)
        getDirectionRoute()
    }


    private fun getDirectionRoute() {
        MapmyIndiaDirections.builder()
                .origin(ORIGIN_POINT)
                .steps(true)
                .resource(DirectionsCriteria.RESOURCE_ROUTE_ETA)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .destination(DESTINATION_POINT)
                .build()
                .enqueueCall(object : Callback<DirectionsResponse> {
                    override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                        //handle response
                        val currentRoute = response.body()!!.routes()[0]
                        val points = PolylineUtils.decode(currentRoute.geometry()!!, Constants.PRECISION_6)
                        val latLngs: MutableList<LatLng> = ArrayList()
                        for (point in points) {
                            latLngs.add(LatLng(point.latitude(), point.longitude()))
                        }
                        val latLngBounds = LatLngBounds.Builder()
                                .includes(latLngs)
                                .build()
                        mapmyIndiaMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10, 10, 10, 10))

                        snakePolyLinePlugin?.create(currentRoute.legs()!![0].steps())
                    }

                    override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
    }


    override fun onMapError(p0: Int, p1: String?) {

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        snakePolyLinePlugin?.removeCallback()

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


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView.onSaveInstanceState(outState)
    }


}