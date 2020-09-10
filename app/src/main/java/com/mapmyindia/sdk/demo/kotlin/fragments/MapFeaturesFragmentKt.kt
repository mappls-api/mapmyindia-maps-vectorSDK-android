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
import com.mapmyindia.sdk.demo.kotlin.activity.GesturesActivity
import com.mapmyindia.sdk.demo.kotlin.activity.MapClickActivity
import com.mapmyindia.sdk.demo.kotlin.activity.MapFragmentActivity
import com.mapmyindia.sdk.demo.kotlin.activity.MapLongClickActivity
import com.mapmyindia.sdk.demo.kotlin.adapter.MapFeatureListAdapter
import java.util.*

class MapFeaturesFragmentKt : Fragment(), MapFeatureListAdapter.AdapterOnClick {
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
        featuresArrayList.add(FeaturesList("Map Fragment", "Way to add Map in Fragment"))
        featuresArrayList.add(FeaturesList("Map Long Click", "Location camera options for render and tracking modes"))
        featuresArrayList.add(FeaturesList("Map Tap", "Long press on map and get Latitude Longitude"))
        featuresArrayList.add(FeaturesList("Map Gestures", "Gestures detection for map view"))
        featureRecycleView.layoutManager = LinearLayoutManager(context)
        var mapFeaturesListAdapter: MapFeatureListAdapter = MapFeatureListAdapter(featuresArrayList, this)

        featureRecycleView.adapter = mapFeaturesListAdapter


    }

    override fun onClick(position: Int) {
        if (position == 0) {
            val mapFragmentIntent = Intent(context, MapFragmentActivity::class.java)
            startActivity(mapFragmentIntent)
        } else if (position == 1) {
            val mapClickIntent = Intent(context, MapLongClickActivity::class.java)
            startActivity(mapClickIntent)
        } else if (position == 2) {
            val mapLongClickIntent = Intent(context, MapClickActivity::class.java)
            startActivity(mapLongClickIntent)
        } else if (position == 3) {
            val gestureIntent = Intent(context, GesturesActivity::class.java)
            startActivity(gestureIntent)
        }
    }
}


