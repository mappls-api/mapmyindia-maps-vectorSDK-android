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
import com.mapmyindia.sdk.demo.java.activity.CustomCurrentLocationIconActivity
import com.mapmyindia.sdk.demo.java.model.FeaturesList
import com.mapmyindia.sdk.demo.kotlin.activity.CurrentLocationActivity
import com.mapmyindia.sdk.demo.kotlin.activity.PlaceAutoCompleteActivity
import com.mapmyindia.sdk.demo.kotlin.activity.SafetyPluginActivity
import com.mapmyindia.sdk.demo.kotlin.adapter.MapFeatureListAdapter
import java.util.*

class LocationFragmentKt: Fragment(), MapFeatureListAdapter.AdapterOnClick {
    lateinit var featureRecycleView: RecyclerView

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
        featuresArrayList.add(FeaturesList("Current Location", "Location camera options for render and tracking modes"))
        featuresArrayList.add(FeaturesList("Customize Current Location Icon", "To Change the default Current Location Icon"))
        featureRecycleView.layoutManager = LinearLayoutManager(context)
        var mapFeaturesListAdapter = MapFeatureListAdapter(featuresArrayList,this)

        featureRecycleView.adapter = mapFeaturesListAdapter


    }
    override fun onClick(position: Int) {
        if (position == 0) {
            var currentLocationIntent: Intent = Intent(context, CurrentLocationActivity::class.java)
            startActivity(currentLocationIntent)
        }
else if(position==1){
            var iconLocationIntent: Intent = Intent(context, CustomCurrentLocationIconActivity::class.java)
            startActivity(iconLocationIntent)

        }

    }}