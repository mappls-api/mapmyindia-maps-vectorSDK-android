package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.BaseLayoutBinding
import com.mapmyindia.sdk.demo.java.plugin.TrackingPlugin
import com.mapmyindia.sdk.geojson.Point
import com.mapmyindia.sdk.geojson.utils.PolylineUtils
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

class TrackingActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mBinding: BaseLayoutBinding
    private var travelledPoints: List<Point>? = null
    private var mapmyIndiaMap: MapmyIndiaMap? = null
    private var trackingPlugin: TrackingPlugin? = null
    private var index = 0
    private val MARKER_TRANSLATION_COMPLETE = 125
    private val trackingHandler = Handler(Looper.getMainLooper())
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            sendMessageToBackgroundHandler()
            trackingHandler.postDelayed(this, 3000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.base_layout)
        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        mBinding.mapView.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        mBinding.mapView.onResume()
        if (travelledPoints != null) {
            trackingHandler.post(runnable)
        }
    }

    override fun onPause() {
        super.onPause()
        mBinding.mapView.onPause()
        trackingHandler.removeCallbacks(runnable)
    }

    override fun onStop() {
        super.onStop()
        mBinding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.mapView.onLowMemory()
    }

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap
        mapmyIndiaMap.getStyle {
            trackingPlugin = TrackingPlugin(mBinding.mapView, mapmyIndiaMap)
            callRouteETA()
        }
    }

    private fun sendMessageToBackgroundHandler() {
        try {
            // GeoPoint nextLocation = mOrderTrackingActivity.getLocationBlockingQueue().take();
            if (index < travelledPoints?.size ?: 0 - 1) {
                trackingPlugin!!.animateCar(travelledPoints!![index], travelledPoints!![index + 1])
                index++
                callTravelledRoute()
            } else {
                trackingHandler.removeCallbacks(runnable)
                //                                Toast.makeText(this, "Route END", Toast.LENGTH_SHORT).show();
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun callRouteETA() {
        mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(19.15183700, 72.93374500), 12.0))
        val builder = MapmyIndiaDirections.builder()
                .steps(true)
                .origin(Point.fromLngLat(72.93374500, 19.15183700))
                .destination(Point.fromLngLat(72.9344, 19.1478))
                .resource(DirectionsCriteria.RESOURCE_ROUTE_ETA)
                .overview(DirectionsCriteria.OVERVIEW_SIMPLIFIED)
        MapmyIndiaDirectionManager.newInstance(builder.build()).call(object: OnResponseCallback<DirectionsResponse> {
            override fun onSuccess(directionsResponse: DirectionsResponse?) {
                if (directionsResponse != null && directionsResponse.routes().size > 0) {
                    val directionsRoute = directionsResponse.routes()[0]
                    if (directionsRoute?.geometry() != null) {
                        travelledPoints = PolylineUtils.decode(directionsRoute.geometry()!!, Constants.PRECISION_6)
                        startTracking()
                    }
                }
            }

            override fun onError(p0: Int, p1: String?) {

            }

        })
    }

    private fun startTracking() {
        trackingPlugin!!.addMarker(travelledPoints!![0])
        trackingHandler.post(runnable)
    }

    private fun callTravelledRoute() {
        val direction = MapmyIndiaDirections.builder()
                .origin(travelledPoints!![index])
                .destination(Point.fromLngLat(72.9344, 19.1478))
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .steps(true)
                .routeType(DirectionsCriteria.ROUTE_TYPE_SHORTEST)
                .resource(DirectionsCriteria.RESOURCE_ROUTE)
                .build()
        MapmyIndiaDirectionManager.newInstance(direction).call(object: OnResponseCallback<DirectionsResponse> {
            override fun onSuccess(directionsResponse: DirectionsResponse?) {
                if (directionsResponse != null && directionsResponse.routes().size > 0) {
                    val directionsRoute = directionsResponse.routes()[0]
                    if (directionsRoute?.geometry() != null) {
                        trackingPlugin?.updatePolyline(directionsRoute)
                        val remainingPath = PolylineUtils.decode(directionsRoute.geometry()!!, Constants.PRECISION_6)
                        val latLngList: MutableList<LatLng> = ArrayList()
                        for (point in remainingPath) {
                            latLngList.add(LatLng(point.latitude(), point.longitude()))
                        }
                        if (latLngList.size > 0) {
                            if (latLngList.size == 1) {
                                mapmyIndiaMap?.easeCamera(CameraUpdateFactory.newLatLngZoom(latLngList[0], 12.0))
                            } else {
                                val latLngBounds = LatLngBounds.Builder()
                                        .includes(latLngList)
                                        .build()
                                mapmyIndiaMap?.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 180, 0, 180, 0))
                            }
                        }
                    }
                }
            }

            override fun onError(p0: Int, p1: String?) {

            }

        })
    }

    override fun onMapError(i: Int, s: String) {}
}