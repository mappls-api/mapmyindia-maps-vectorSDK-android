package com.mapmyindia.sdk.demo.kotlin.activity


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.adapter.FeaturesListAdapter
import com.mapmyindia.sdk.demo.kotlin.model.Features
import java.util.*

/**
 * Created by CEINFO on 26-02-2019.
 */
class FeatureListActivityKotlin : AppCompatActivity() {

    private var featuresRecycleView: RecyclerView? = null
    private var mLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.features_list_activity)
        initReferences()
    }

    private fun initReferences() {
        val featuresArrayList = ArrayList<Features>()
        featuresArrayList.add(Features(1, "Camera Features", CameraActivity::class.java, "Description"))
        featuresArrayList.add(Features(2, "Map Tap", MapClickActivity::class.java, "Description"))
        featuresArrayList.add(Features(3, "Map Long Tap", MapLongClickActivity::class.java, "Description"))
        featuresArrayList.add(Features(4, "Add Marker", AddMarkerActivity::class.java, "Description"))
        featuresArrayList.add(Features(5, "Add Custom Infowindow", AddCustomInfoWindowActivity::class.java, "Description"))
        featuresArrayList.add(Features(5, "Add Custom Marker", AddCustomMarkerActivity::class.java, "Description"))
        featuresArrayList.add(Features(6, "Draw Polyline", PolylineActivity::class.java, "Description"))
        featuresArrayList.add(Features(7, "Draw Polygon", PolygonActivity::class.java, "Description"))
        featuresArrayList.add(Features(8, "Current Location", CurrentLocationActivity::class.java, "Description"))
        featuresArrayList.add(Features(9, "Auto Suggest", AutoSuggestActivity::class.java, "Description"))
        featuresArrayList.add(Features(10, "Geo Code", GeoCodeActivity::class.java, "Description"))
        featuresArrayList.add(Features(11, "Reverse Geocode", ReverseGeocodeActivity::class.java, "Description"))
        featuresArrayList.add(Features(12, "Nearby", NearByActivity::class.java, "Description"))
        featuresArrayList.add(Features(13, "Get Direction", DirectionActivity::class.java, "Description"))
        featuresArrayList.add(Features(14, "Get Distance", DistanceActivity::class.java, "Description"))
        featuresArrayList.add(Features(15, "Marker Rotation & Transition", MarkerRotationTransitionActivity::class.java, "Description"))
        featuresArrayList.add(Features(16, "Polyline with Gradient color", GradientPolylineActivity::class.java, "Description"))
        featuresArrayList.add(Features(17, "Semicircle Polyline", SemiCirclePolylineActivity::class.java, "Description"))
        featuresArrayList.add(Features(18, "Animate Car", CarAnimationActivity::class.java, "Description"))
        featuresArrayList.add(Features(19, "Marker Dragging", MarkerDraggingActivity::class.java, "Description"))
        featuresArrayList.add(Features(20, "Indoor", IndoorActivity::class.java, "Description"))

        featuresRecycleView = findViewById(R.id.featuresRecycleView)
        mLayoutManager = LinearLayoutManager(this)
        featuresRecycleView!!.layoutManager = mLayoutManager
        val featuresListAdapter = object : FeaturesListAdapter(featuresArrayList) {
            override fun redirectOnFeatureCallBack(features: Features) {
                switchToActivity(features.featureActivityName)
            }
        }
        featuresRecycleView!!.adapter = featuresListAdapter
    }

    private fun switchToActivity(featureActivityName: Class<*>?) {
        val intent = Intent(this, featureActivityName)
        startActivity(intent)
    }
}