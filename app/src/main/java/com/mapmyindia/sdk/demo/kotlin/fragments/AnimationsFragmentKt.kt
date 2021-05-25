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
import com.mapmyindia.sdk.demo.kotlin.activity.CarAnimationActivity
import com.mapmyindia.sdk.demo.kotlin.activity.MarkerRotationTransitionActivity
import com.mapmyindia.sdk.demo.kotlin.activity.TrackingActivity
import com.mapmyindia.sdk.demo.kotlin.adapter.MapFeatureListAdapter
import java.util.*

/**
 ** Created by Saksham on 03-05-2021.
 **/
class AnimationsFragmentKt : Fragment(), MapFeatureListAdapter.AdapterOnClick{
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
        featuresArrayList.add(FeaturesList("Animate Car", "Animate a car marker on predefined route"))
        featuresArrayList.add(FeaturesList("Marker Rotation and Transition", "Rotate a marker by given degree and animate the marker to a new position"))
        featuresArrayList.add(FeaturesList("Tracking sample", "To track a vehicle on map"))
        featureRecycleView.layoutManager = LinearLayoutManager(context)
        var mapFeaturesListAdapter: com.mapmyindia.sdk.demo.kotlin.adapter.MapFeatureListAdapter = com.mapmyindia.sdk.demo.kotlin.adapter.MapFeatureListAdapter(featuresArrayList, this)

        featureRecycleView.adapter = mapFeaturesListAdapter


    }
    override fun onClick(position: Int) {
        if(position == 0) {
            var autoSuggestIntent = Intent(context, CarAnimationActivity::class.java)
            startActivity(autoSuggestIntent)
        } else if(position==1){
            var geocodeIntent : Intent = Intent(context, MarkerRotationTransitionActivity::class.java)
            startActivity(geocodeIntent)

        }else if(position==2){
            var reversegeoCodeIntent : Intent = Intent(context, TrackingActivity::class.java)
            startActivity(reversegeoCodeIntent)

        }
    }
}