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

class PolylineFeatureFragmentKt : Fragment(), MapFeatureListAdapter.AdapterOnClick {
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
        featuresArrayList.add(FeaturesList("Draw Polyline", "Draw a polyline with given list of latitude and longitude"))
        featuresArrayList.add(FeaturesList("Polyline with Gradient color", "Draw a polyline with given list of latitude and longitude"))
        featuresArrayList.add(FeaturesList("Semicircle polyline", "Draw a semicircle polyline on the map"))
       featuresArrayList.add(FeaturesList("SnakePolyLine","Draw a Snake Motion Polyline"))
        featuresArrayList.add(FeaturesList("Draw Polygon", "Draw a polygon on the map"))
        featureRecycleView.layoutManager = LinearLayoutManager(context)
        var mapFeaturesListAdapter: MapFeatureListAdapter = MapFeatureListAdapter(featuresArrayList, this)

        featureRecycleView.adapter = mapFeaturesListAdapter


    }

    override fun onClick(position: Int) {
        if (position == 0) {
            val addPolylineIntent = Intent(context, PolylineActivity::class.java)
            startActivity(addPolylineIntent)
        } else if (position == 1) {
            val addGradientPolylineIntent = Intent(context, GradientPolylineActivity::class.java)
            startActivity(addGradientPolylineIntent)
        } else if (position == 2) {
            val addSemiCirclePolyLineIntent = Intent(context, SemiCirclePolylineActivity::class.java)
            startActivity(addSemiCirclePolyLineIntent)
        } else if (position == 3) {
            val addSnakeMotionPolylineIntent = Intent(context, SnakeMotionPolylineActivity::class.java)
            startActivity(addSnakeMotionPolylineIntent)
        } else if (position == 4) {
            val addPolyGonIntent = Intent(context, PolygonActivity::class.java)
            startActivity(addPolyGonIntent)
        }
    }
}
