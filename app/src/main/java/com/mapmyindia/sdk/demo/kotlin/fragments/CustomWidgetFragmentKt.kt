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
import com.mapmyindia.sdk.demo.kotlin.activity.*
import com.mapmyindia.sdk.demo.kotlin.adapter.MapFeatureListAdapter
import java.util.*

class CustomWidgetFragmentKt :Fragment(), MapFeatureListAdapter.AdapterOnClick {
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
        featuresArrayList.add(FeaturesList("Place Autocomplete Widget", "Location search functionality and UI to search a place"))
        featuresArrayList.add(FeaturesList("MapmyIndia Safety Plugin", "MapmyIndia Safety Plugin, To show you current location safety"))
        featuresArrayList.add(FeaturesList("Place Picker", "Place Picker to search and choose a specific location"))
        featuresArrayList.add(FeaturesList("GeoFence", "Highly customizable UI widget to create/edit geofence widget"))
        featuresArrayList.add(FeaturesList("Direction Step", "How to show textual instructions and maneuver icon to the user"))
        featureRecycleView.layoutManager = LinearLayoutManager(context)
        var mapFeaturesListAdapter: MapFeatureListAdapter = MapFeatureListAdapter(featuresArrayList,this)

        featureRecycleView.adapter = mapFeaturesListAdapter


    }
    override fun onClick(position: Int) {
        if(position==0){
            var placeAutoCompleteIntent : Intent = Intent(context, PlaceAutoCompleteActivity::class.java)
            startActivity(placeAutoCompleteIntent)

        }else if(position==1){
            var safetyPluginIntent :Intent= Intent(context, SafetyPluginActivity::class.java)
            startActivity(safetyPluginIntent)

        }else if(position==2){
            var placePickerIntent :Intent= Intent(context, PickerActivity::class.java)
            startActivity(placePickerIntent)

        }else if(position==3){
            var geofenceIntent :Intent= Intent(context, GeoFenceActivity::class.java)
            startActivity(geofenceIntent)

        }else if(position==4){
            var directionIntent :Intent= Intent(context, DirectionStepActivity::class.java)
            startActivity(directionIntent)

        }


    }
}