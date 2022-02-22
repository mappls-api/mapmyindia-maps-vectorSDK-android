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

class ApiCallFragmentKt : Fragment(), MapFeatureListAdapter.AdapterOnClick{
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
        featuresArrayList.add(FeaturesList("Autosuggest","Auto suggest places on the map"))
        featuresArrayList.add(FeaturesList("Geo Code", "Geocode rest API call"))
        featuresArrayList.add(FeaturesList("Reverse Geocode", "Reverse Geocode rest API call"))
        featuresArrayList.add(FeaturesList("Nearby", "Show nearby results on the map"))
        featuresArrayList.add(FeaturesList("Get Direction", "Get directions between two points and show on the map"))
        featuresArrayList.add(FeaturesList("Get Distance", "Get distance between points"))
        featuresArrayList.add(FeaturesList("Hateos Nearby Api", "Nearby places using hateos api"))
        featuresArrayList.add(FeaturesList("POI Along Route Api", "user will be able to get the details of POIs of a particular category along his set route"))
        featuresArrayList.add(FeaturesList("Place Detail", "To get the place details from eLoc"))
        featuresArrayList.add(FeaturesList("Nearby Report", "To get the nearby reports on the map"))
        featureRecycleView.layoutManager = LinearLayoutManager(context)
        var mapFeaturesListAdapter: com.mapmyindia.sdk.demo.kotlin.adapter.MapFeatureListAdapter = com.mapmyindia.sdk.demo.kotlin.adapter.MapFeatureListAdapter(featuresArrayList,this)

        featureRecycleView.adapter = mapFeaturesListAdapter


    }
    override fun onClick(position: Int) {
        if(position == 0) {
            var autoSuggestIntent = Intent(context, AutoSuggestActivity::class.java)
            startActivity(autoSuggestIntent)
        } else if(position==1){
            var geocodeIntent = Intent(context, GeoCodeActivity::class.java)
            startActivity(geocodeIntent)

        }else if(position==2){
            var reversegeoCodeIntent = Intent(context,ReverseGeocodeActivity::class.java)
            startActivity(reversegeoCodeIntent)

        }else if(position==3){
            var nearByIntent = Intent(context,NearByActivity::class.java)
            startActivity(nearByIntent)

        }else if(position==4){
            var directionIntent = Intent(context,DirectionActivity::class.java)
            startActivity(directionIntent)

        }else if(position==5){
            var distanceIntent = Intent(context,DistanceActivity::class.java)
            startActivity(distanceIntent)

        }else if(position==6){
            var distanceIntent = Intent(context,HateOsNearbyActivity::class.java)
            startActivity(distanceIntent)

        }else if(position==7){
            var distanceIntent = Intent(context,PoiAlongRouteActivity::class.java)
            startActivity(distanceIntent)

        }else if(position==8){
            var distanceIntent = Intent(context,PlaceDetailActivity::class.java)
            startActivity(distanceIntent)

        }else if(position==9){
            var distanceIntent = Intent(context,NearbyReportActivity::class.java)
            startActivity(distanceIntent)

        }

    }
}