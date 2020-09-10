package com.mapmyindia.sdk.demo.kotlin.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.java.model.FeaturesList
import com.mapmyindia.sdk.demo.kotlin.activity.AddCustomMarkerActivitykt
import com.mapmyindia.sdk.demo.kotlin.adapter.MapFeatureListAdapter
import java.util.*

class MarkerFeatureFragmentKt : Fragment(), MapFeatureListAdapter.AdapterOnClick {
    lateinit var featureRecycleView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_mapfeatures_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        featureRecycleView = view.findViewById(R.id.addfeaturesRecyclerView)
        init()
    }

    private fun init() {
        var i = 0
        val featuresArrayList = ArrayList<FeaturesList>()
        featuresArrayList.add(FeaturesList("Add Marker", "Way to add Map in Fragment"))
        featuresArrayList.add(FeaturesList("Add Custom Infowindow", "Location camera options for render and tracking modes"))
        featuresArrayList.add(FeaturesList("Add Custom Marker", "Long press on map and get Latitude Longitude"))
        featuresArrayList.add(FeaturesList("Marker Dragging", "Drag a marker"))
        featureRecycleView.layoutManager = LinearLayoutManager(context)
        var mapFeaturesListAdapter: MapFeatureListAdapter = MapFeatureListAdapter(featuresArrayList, this)

        featureRecycleView.adapter = mapFeaturesListAdapter


    }

    override fun onClick(position: Int) {
        if (position == 0) {
            val addMarkerIntent = Intent(context, com.mapmyindia.sdk.demo.kotlin.activity.AddMarkerActivity::class.java)
            startActivity(addMarkerIntent)
        } else if (position == 1) {
            val addCustomInfowindowIntent = Intent(context, com.mapmyindia.sdk.demo.kotlin.activity.AddCustomInfoWindowActivity::class.java)
            startActivity(addCustomInfowindowIntent)
        } else if (position == 2) {
            val addCustomMarkerIntent = Intent(context, AddCustomMarkerActivitykt::class.java)
            startActivity(addCustomMarkerIntent)
        } else if (position == 3) {
            val markerDraggingIntent = Intent(context, com.mapmyindia.sdk.demo.kotlin.activity.MarkerDraggingActivity::class.java)
            startActivity(markerDraggingIntent)
        }

    }
}
