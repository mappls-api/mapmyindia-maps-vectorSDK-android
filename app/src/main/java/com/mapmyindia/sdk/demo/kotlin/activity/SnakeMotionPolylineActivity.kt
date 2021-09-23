package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.java.plugin.SnakePolyLinePlugin
import com.mapmyindia.sdk.geojson.Point
import com.mapmyindia.sdk.geojson.utils.PolylineUtils
import com.mapmyindia.sdk.maps.MapView
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.maps.geometry.LatLngBounds
import com.mmi.services.api.OnResponseCallback
import com.mmi.services.api.directions.DirectionsCriteria
import com.mmi.services.api.directions.MapmyIndiaDirectionManager
import com.mmi.services.api.directions.MapmyIndiaDirections
import com.mmi.services.api.directions.models.DirectionsResponse
import com.mmi.services.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SnakeMotionPolylineActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    lateinit var mapmyIndiaMap: MapmyIndiaMap

    private lateinit var snakePolyLinePlugin: SnakePolyLinePlugin

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


    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap
        mapmyIndiaMap.getStyle {
            snakePolyLinePlugin = SnakePolyLinePlugin(mapView, mapmyIndiaMap)
            getDirectionRoute()
        }
    }


    private fun getDirectionRoute() {
        val directions = MapmyIndiaDirections.builder()
                .origin(ORIGIN_POINT)
                .steps(true)
                .resource(DirectionsCriteria.RESOURCE_ROUTE_ETA)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .destination(DESTINATION_POINT)
                .build()

        MapmyIndiaDirectionManager.newInstance(directions).call(object : OnResponseCallback<DirectionsResponse> {
            override fun onSuccess(directionsResponse: DirectionsResponse?) {
                if (directionsResponse != null) {
                    //handle response
                    val currentRoute = directionsResponse.routes()[0]
                    val points = PolylineUtils.decode(currentRoute.geometry()!!, Constants.PRECISION_6)
                    val latLngs: MutableList<LatLng> = ArrayList()
                    for (point in points) {
                        latLngs.add(LatLng(point.latitude(), point.longitude()))
                    }
                    val latLngBounds = LatLngBounds.Builder()
                            .includes(latLngs)
                            .build()
                    mapmyIndiaMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10, 10, 10, 10))

                    snakePolyLinePlugin.create(currentRoute.legs()!![0].steps())

                }
            }

            override fun onError(p0: Int, p1: String?) {

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
        snakePolyLinePlugin.removeCallback()

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