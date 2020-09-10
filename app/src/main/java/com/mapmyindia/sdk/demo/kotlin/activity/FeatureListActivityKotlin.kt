package com.mapmyindia.sdk.demo.kotlin.activity


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.java.activity.AddCustomMarkerActivity
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
        var i = 0
        val featuresArrayList = ArrayList<Features>()
        featuresArrayList.add(Features(++i, "Camera Features", CameraActivity::class.java, "Animate, Move or Ease Camera Position"))
        featuresArrayList.add(Features(++i, "Map Tap", MapClickActivity::class.java, "Tap on map and get tapped Latitude Longitude"))
        featuresArrayList.add(Features(++i, "Map Long Tap", MapLongClickActivity::class.java, "Long press on map and get Latitude Longitude"))
        featuresArrayList.add(Features(++i, "Add Marker", AddMarkerActivity::class.java, "Add a marker on the map"))
        featuresArrayList.add(Features(++i, "Add Custom Infowindow", AddCustomInfoWindowActivity::class.java, "Show custom info window when user click on a marker"))
        featuresArrayList.add(Features(++i, "Add Custom Marker", AddCustomMarkerActivity::class.java, "Customize a marker, change its marker image"))
        featuresArrayList.add(Features(++i, "Draw Polyline", PolylineActivity::class.java, "Draw a polyline with given list of latitude and longitude"))
        featuresArrayList.add(Features(++i, "Draw Polygon", PolygonActivity::class.java, "Draw a polygon on the map"))
        featuresArrayList.add(Features(++i, "Current Location", CurrentLocationActivity::class.java, "Show current location on the map"))
        featuresArrayList.add(Features(++i, "Auto  Suggest", AutoSuggestActivity::class.java, "Auto suggest places on the map"))
        featuresArrayList.add(Features(++i, "Location Camera Options", LocationCameraActivity::class.java, "Location camera options for render and tracking modes"))
        featuresArrayList.add(Features(++i, "Geo Code", GeoCodeActivity::class.java, "Geocode rest API call"))
        featuresArrayList.add(Features(++i, "Reverse Geocode", ReverseGeocodeActivity::class.java, "Reverse Geocode rest API call"))
        featuresArrayList.add(Features(++i, "Nearby", NearByActivity::class.java, "Show nearby results on the map"))
        featuresArrayList.add(Features(++i, "Get Direction", DirectionActivity::class.java, "get directions between two points and show on the map"))
        featuresArrayList.add(Features(++i, "Get Distance", DistanceActivity::class.java, "Get distance between points"))
        featuresArrayList.add(Features(++i, "Marker Rotation and Transition", MarkerRotationTransitionActivity::class.java, "Rotate a marker by given degree and animate the marker to a new position"))
        featuresArrayList.add(Features(++i, "Polyline with Gradient color", GradientPolylineActivity::class.java, "Draw a gradient color polyline"))
        featuresArrayList.add(Features(++i, "Semicircle polyline", SemiCirclePolylineActivity::class.java, "Draw a semicircle polyline on the map"))
        featuresArrayList.add(Features(++i, "Animate Car", CarAnimationActivity::class.java, "Animate a car marker on predefined route"))
        featuresArrayList.add(Features(++i, "Marker Dragging", MarkerDraggingActivity::class.java, "Drag a marker"))
        featuresArrayList.add(Features(++i, "Indoor", IndoorActivity::class.java, "Show indoor widget when focus on multi storey building"))
        featuresArrayList.add(Features(++i, "Show Heatmap data", HeatMapActivity::class.java, "Add a heatmap to visualize data"))
        featuresArrayList.add(Features(++i, "Place Autocomplete Widget", PlaceAutoCompleteActivity::class.java, "Location search functionality and UI to search a place"))
        featuresArrayList.add(Features(++i, "MapmyIndia Safety Plugin", SafetyPluginActivity::class.java, "MapmyIndia Safety Plugin, To show you current location safety"))
        featuresArrayList.add(Features(++i, "Map Gestures", GesturesActivity::class.java, "Gestures detection for map view"))
        featuresArrayList.add(Features(++i, "Snake Polyline", SnakeMotionPolylineActivity::class.java, "Snake a polyline from the origin to the destination"))
        featuresArrayList.add(Features(++i, "Interactive Layer", InteractiveLayerActivity::class.java, "Show Interactive CORONA Layers on the map view"))
        featuresArrayList.add(Features(++i, "Direction Step", DirectionStepActivity::class.java, "How to show textual instructions and maneuver icon to the user"))
        featuresArrayList.add(Features(++i, "Map Scalebar", ScalebarActivity::class.java, "Add a scale bar on map view to determine distance based on zoom level"))
        featuresArrayList.add(Features(++i, "Place Picker", PickerActivity::class.java, "Place Picker to search and choose a specific location"))
        featuresArrayList.add(Features(++i, "GeoFence", GeoFenceActivity::class.java, "Highly customizable UI widget to create/edit geofence widget"))
        featuresArrayList.add(Features(++i, "Map in Fragment", MapFragmentActivity::class.java, "Way to use mapview in Fragment"))
        featuresArrayList.add(Features(++i, "Hateos Nearby Api", HateOsNearbyActivity::class.java, "Nearby places using hateos api"))
        featuresArrayList.add(Features(++i, "POI Along Route Api", PoiAlongRouteActivity::class.java, "user will be able to get the details of POIs of a particular category along his set route"))
        featuresRecycleView = findViewById(R.id.featuresRecycleView)
        mLayoutManager = LinearLayoutManager(this)
        featuresRecycleView?.layoutManager = mLayoutManager
        val featuresListAdapter = object : FeaturesListAdapter(featuresArrayList) {
            override fun redirectOnFeatureCallBack(features: Features) {
                switchToActivity(features.featureActivityName)
            }
        }
        featuresRecycleView?.adapter = featuresListAdapter
    }

    private fun switchToActivity(featureActivityName: Class<*>?) {
        val intent = Intent(this, featureActivityName)
        startActivity(intent)
    }
}