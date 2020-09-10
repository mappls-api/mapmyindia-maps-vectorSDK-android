package com.mapmyindia.sdk.demo.kotlin.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mapbox.turf.TurfConstants
import com.mapbox.turf.TurfTransformation
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityGeoFenceDetailBinding
import com.mapmyindia.sdk.demo.kotlin.adapter.GeoFenceDetailAdapter
import com.mapmyindia.sdk.demo.kotlin.model.GeofenceDetail
import com.mapmyindia.sdk.plugin.annotation.*
import java.util.concurrent.atomic.AtomicInteger

class GeoFenceDetailActivity: AppCompatActivity(), OnMapReadyCallback {

    private val ID = AtomicInteger(0)
    private var geofenceDetails: ArrayList<GeofenceDetail> = ArrayList()
    private val geoFenceDetailAdapter: GeoFenceDetailAdapter = GeoFenceDetailAdapter()
    private var symbolManager: SymbolManager? = null
    private var  fillManager: FillManager? = null
    private var lineManager: LineManager? = null
    private var mapmyIndiaMap: MapboxMap? = null

    private val symbols: MutableList<Symbol> = ArrayList()
    private val fillList: MutableList<Fill> = ArrayList()
    private val lines: MutableList<Line> = ArrayList()
    private lateinit var mBinding: ActivityGeoFenceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_geo_fence_detail)
        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.rvGeofenceDetail.layoutManager = LinearLayoutManager(this@GeoFenceDetailActivity)
        mBinding.rvGeofenceDetail.adapter = geoFenceDetailAdapter
        geoFenceDetailAdapter.setOnGeoFenceChangeListener(object : GeoFenceDetailAdapter.OnGeoFenceChangeListener {

            override fun onGeoFenceStatusChange() {
                updateCamera()
            }


            override fun onEditGeoFence(geofenceDetail: GeofenceDetail?) {
                val intent =
                    Intent(this@GeoFenceDetailActivity, GeoFenceCustomActivity::class.java)
                intent.putExtra("Geofence", Gson().toJson(geofenceDetail))
                startActivityForResult(intent, 101)
            }

            override fun onRemoveGeofence(geofenceDetail: GeofenceDetail?) {
                removeGeofence(geofenceDetail!!)
                geofenceDetails.remove(geofenceDetail)
                geoFenceDetailAdapter.setGeofenceDetailList(geofenceDetails)
            }
        })

        mBinding.mapView.getMapAsync(this)
        mBinding.tvAddGeofence.setOnClickListener(View.OnClickListener {
            if (geofenceDetails.size >= 5) {
                Toast.makeText(this@GeoFenceDetailActivity, "You have created 5 Gofences", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            startActivityForResult(
                Intent(this@GeoFenceDetailActivity, GeoFenceCustomActivity::class.java), 101)
        })

    }


    private fun updateCamera() {
        val points: MutableList<Point> = java.util.ArrayList()
        for (geofenceDetail in geofenceDetails) {
            if (geofenceDetail.active) {
                if (geofenceDetail.gfType.equals(GeofenceDetail.TYPE_POLYGON, ignoreCase = true)) {
                    points.addAll(geofenceDetail.polygonPoints?:ArrayList())
                } else {
                    val polygon = TurfTransformation.circle(Point.fromLngLat(geofenceDetail.circleCentre?.longitude!!, geofenceDetail.circleCentre?.latitude!!), geofenceDetail.circleRadius?.toDouble()!!, TurfConstants.UNIT_METERS)
                    points.addAll(polygon.coordinates()!![0])
                }
            }
        }
        val latLngBound: MutableList<LatLng> = ArrayList()
        for (point in points) {
            latLngBound.add(LatLng(point.latitude(), point.longitude()))
        }
        setBounds(latLngBound)
    }


    private fun setBounds(latLngBound: List<LatLng>) {
        if (latLngBound.size == 0) {
            return
        }
        if (latLngBound.size == 1) {
            mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngBound[0], 16.0))
        } else {
            mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.Builder()
                    .includes(latLngBound).build(), 70, 70, 70, mBinding.rootBottomSheet.height))
        }
    }




    private fun removeGeofence(geofenceDetail: GeofenceDetail) {
        val removeSymbol: MutableList<Symbol> = java.util.ArrayList()
        for (symbol in symbols) {
            val jsonObject = symbol.data as JsonObject?
            if (jsonObject != null && jsonObject.has("GeoFencetype")) {
                if (jsonObject["GeoFencetype"].asString
                        .equals(geofenceDetail.gfType, ignoreCase = true)) {
                    symbolManager?.clear(symbol)
                    removeSymbol.add(symbol)
                }
            }
        }
        symbols.removeAll(removeSymbol)
        val removeFill: MutableList<Fill> = java.util.ArrayList()
        for (fill in fillList) {
            val jsonObject = fill.data as JsonObject?
            if (jsonObject != null && jsonObject.has("GeoFencetype")) {
                if (jsonObject["GeoFencetype"].asString
                        .equals(geofenceDetail.gfLabel, ignoreCase = true)
                ) {
                    fillManager?.clear(fill)
                    removeFill.add(fill)
                }
            }
        }
        fillList.removeAll(removeFill)
        val removeLine: MutableList<Line> =
            java.util.ArrayList()
        for (line in lines) {
            val jsonObject = line.data as JsonObject?
            if (jsonObject != null && jsonObject.has("GeoFencetype")) {
                if (jsonObject["GeoFencetype"].asString
                        .equals(geofenceDetail.gfLabel, ignoreCase = true)
                ) {
                    lineManager?.clear(line)
                    removeLine.add(line)
                }
            }
        }
        lines.removeAll(removeLine)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                val newGeofenceDetail = Gson().fromJson(data?.getStringExtra("GEOFENCE"), GeofenceDetail::class.java)
                if (newGeofenceDetail.gfLabel == null) {
                    newGeofenceDetail.gfLabel = "GeoFence" + ID.getAndIncrement()
                    geofenceDetails.add(newGeofenceDetail)
                    geoFenceDetailAdapter.setGeofenceDetailList(geofenceDetails)
                    if (fillManager == null) {
                        fillManager = FillManager(mBinding.mapView, mapmyIndiaMap!!)
                    }
                    if (lineManager == null) {
                        lineManager = LineManager(mBinding.mapView, mapmyIndiaMap!!)
                    }
                    if (symbolManager == null) {
                        symbolManager = SymbolManager(mBinding.mapView, mapmyIndiaMap!!)
                    }
                    addGeoFence(newGeofenceDetail)
                } else {
                    for (geofenceDetail in geofenceDetails) {
                        if (geofenceDetail.gfLabel.equals(newGeofenceDetail.gfLabel, ignoreCase = true)) {
                            updateGeoFenceData(geofenceDetail, newGeofenceDetail)
                            updateGeoFence(geofenceDetail)
                        }
                    }
                }
                updateCamera()
            }
        }
    }


    private fun updateGeoFence(geofenceDetail: GeofenceDetail) {
        val clearSymbols: MutableList<Symbol> = ArrayList()
        for (symbol in symbols) {
            val jsonObject = symbol.data as JsonObject?
            if (jsonObject!!.has("GeoFencetype")) {
                if (jsonObject["GeoFencetype"].asString.equals(geofenceDetail.gfLabel, ignoreCase = true)) {
                    symbolManager?.clear(symbol)
                    clearSymbols.add(symbol)
                }
            }
        }
        symbols.removeAll(clearSymbols)
        addIcons(geofenceDetail)
        val polygon: Polygon
        polygon = if (geofenceDetail.gfType.equals(GeofenceDetail.TYPE_POLYGON, ignoreCase = true)) {
            val pointsList: MutableList<List<Point>> = java.util.ArrayList()
            pointsList.add(geofenceDetail.polygonPoints?: ArrayList())
            Polygon.fromLngLats(pointsList)
        } else {
            TurfTransformation.circle(Point.fromLngLat(geofenceDetail.circleCentre?.longitude!!, geofenceDetail.circleCentre?.latitude!!), geofenceDetail.circleRadius?.toDouble()!!, TurfConstants.UNIT_METERS)
        }
        for (fill in fillList) {
            val jsonObject = fill.data as JsonObject?
            if (jsonObject!!.has("GeoFencetype")) {
                if (jsonObject["GeoFencetype"].asString.equals(geofenceDetail.gfLabel, ignoreCase = true)) {
                    fill.geometry = polygon
                    fillManager?.update(fill)
                }
            }
        }
        for (line in lines) {
            val jsonObject = line.data as JsonObject?
            if (jsonObject!!.has("GeoFencetype")) {
                if (jsonObject["GeoFencetype"].asString.equals(geofenceDetail.gfLabel, ignoreCase = true)) {
                    line.geometry = LineString.fromLngLats(polygon.coordinates()!![0])
                    lineManager?.update(line)
                }
            }
        }
    }

    private fun addIcons(geofenceDetail: GeofenceDetail) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("GeoFencetype", geofenceDetail.gfLabel)
        if (geofenceDetail.gfType.equals(com.mapmyindia.sdk.demo.java.model.GeofenceDetail.TYPE_POLYGON, ignoreCase = true)) {
            addPolygonEdges(geofenceDetail.polygonPoints?: ArrayList(), jsonObject)
        } else {
            val symbolOptions = SymbolOptions().position(geofenceDetail.circleCentre)
                    .icon(BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(this, R.drawable.placeholder)))
                    .data(jsonObject)
            val symbol = symbolManager?.create(symbolOptions)
            symbols.add(symbol!!)
        }
    }


    private fun addPolygonEdges(points: List<Point>, jsonObject: JsonObject) {
        if (symbolManager != null) {
            val symbolOptions: MutableList<SymbolOptions> = ArrayList()
            for (point in points) {
                symbolOptions.add(SymbolOptions().geometry(point).data(jsonObject).icon(BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(this, R.drawable.mapbox_marker_icon_default))))
            }
            val symbols = symbolManager?.create(symbolOptions)
            this.symbols.addAll(symbols?: ArrayList())
        }
    }


    private fun updateGeoFenceData(geofenceDetail: GeofenceDetail, newGeofenceDetail: GeofenceDetail) {
        geofenceDetail.polygonPoints = newGeofenceDetail.polygonPoints
        geofenceDetail.active = newGeofenceDetail.active
        geofenceDetail.circleRadius = newGeofenceDetail.circleRadius
        geofenceDetail.circleCentre = newGeofenceDetail.circleCentre
        geofenceDetail.gfType = newGeofenceDetail.gfType
    }

    private fun addGeoFence(geofenceDetail: GeofenceDetail) {
        val polygon: Polygon
        val jsonObject = JsonObject()
        jsonObject.addProperty("GeoFencetype", geofenceDetail.gfLabel)
        if (geofenceDetail.gfType.equals(GeofenceDetail.TYPE_POLYGON, ignoreCase = true)) {
            addPolygonEdges(geofenceDetail.polygonPoints?: ArrayList(), jsonObject)
            val pointsList: MutableList<List<Point>> = java.util.ArrayList()
            pointsList.add(geofenceDetail.polygonPoints?: ArrayList())
            polygon = Polygon.fromLngLats(pointsList)
        } else {
            polygon = TurfTransformation.circle(Point.fromLngLat(geofenceDetail.circleCentre?.longitude!!, geofenceDetail.circleCentre?.latitude!!), geofenceDetail.circleRadius?.toDouble()!!, TurfConstants.UNIT_METERS)
            val symbolOptions = SymbolOptions().position(geofenceDetail.circleCentre)
                    .icon(BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(this, R.drawable.placeholder)))
                    .data(jsonObject)
            val symbol = symbolManager?.create(symbolOptions)
            symbols.add(symbol!!)
        }
        val latLngs: MutableList<LatLng> = java.util.ArrayList()
        for (point in polygon.coordinates()!![0]) {
            latLngs.add(LatLng(point.latitude(), point.longitude()))
        }
        val fillOptions = FillOptions()
                .fillColor("#D81B60")
                .fillOpacity(0.5f)
                .geometry(polygon)
                .data(jsonObject)
        val fill = fillManager?.create(fillOptions)
        fillList.add(fill!!)
        val points = polygon.coordinates()!![0]
        if (geofenceDetail.gfType.equals(GeofenceDetail.TYPE_CIRCLE, ignoreCase = true)) {
            points.add(points[0])
        }
        val lineOptions = LineOptions()
                .lineColor("#511050")
                .lineOpacity(0.9f)
                .geometry(LineString.fromLngLats(points))
                .data(jsonObject)
        val line = lineManager?.create(lineOptions)
        lines.add(line!!)
    }


    override fun onMapError(p0: Int, p1: String?) {

    }

    override fun onMapReady(mapmyIndiaMap: MapboxMap?) {
        this.mapmyIndiaMap = mapmyIndiaMap
    }


    override fun onStart() {
        super.onStart()
        mBinding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mBinding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.mapView.onPause()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.mapView.onSaveInstanceState(outState)
    }
}