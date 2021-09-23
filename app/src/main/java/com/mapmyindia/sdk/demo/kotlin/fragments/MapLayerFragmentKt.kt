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
import java.util.ArrayList

/**
 ** Created by Saksham on 04-09-2020.
 **/
class MapLayerFragmentKt: Fragment(), MapFeatureListAdapter.AdapterOnClick {
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
        val featuresArrayList = ArrayList<FeaturesList>()
        featuresArrayList.add(FeaturesList("Show Heatmap data", "Add a heatmap to visualize data"))
        featuresArrayList.add(FeaturesList("Indoor", "Show indoor widget when focus on multi storey building"))
        featuresArrayList.add(FeaturesList("Interactive Layer", "Show Interactive CORONA Layers on the map view"))
        featuresArrayList.add(FeaturesList("Map Scalebar", "Add a scale bar on map view to determine distance based on zoom level"))
        featuresArrayList.add(FeaturesList("Map Safety Strip", "To display a user's safety status for COVID-19 on a map"))
        featureRecycleView.layoutManager = LinearLayoutManager(context)
        var mapFeaturesListAdapter: MapFeatureListAdapter = MapFeatureListAdapter(featuresArrayList, this)

        featureRecycleView.adapter = mapFeaturesListAdapter


    }

    override fun onClick(position: Int) {
        if (position == 0) {
            val heatMapIntent = Intent(context, HeatMapActivity::class.java)
            startActivity(heatMapIntent)
        } else if (position == 1) {
            val indoorIntent = Intent(context, IndoorActivity::class.java)
            startActivity(indoorIntent)
        } else if (position == 2) {
            val interactiveLayerIntent = Intent(context, InteractiveLayerActivity::class.java)
            startActivity(interactiveLayerIntent)
        } else if (position == 3) {
            val scaleBarIntent = Intent(context, ScalebarActivity::class.java)
            startActivity(scaleBarIntent)
        } else if (position == 4) {
            val safetyStripIntent = Intent(context, SafetyStripActivity::class.java)
            startActivity(safetyStripIntent)
        }
    }
}