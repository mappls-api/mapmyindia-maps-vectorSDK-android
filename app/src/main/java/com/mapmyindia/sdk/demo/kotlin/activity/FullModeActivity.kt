package com.mapmyindia.sdk.demo.kotlin.activity

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.settings.MapmyIndiaPlaceWidgetSetting
import com.mapmyindia.sdk.maps.MapView
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.annotations.MarkerOptions
import com.mapmyindia.sdk.maps.camera.CameraPosition
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapmyindia.sdk.plugins.places.common.PlaceConstants
import com.mmi.services.api.autosuggest.model.ELocation


class FullModeActivity : AppCompatActivity() , OnMapReadyCallback {

    private lateinit var mapView: MapView
    private var mapmyIndiaMap: MapmyIndiaMap? = null


    private lateinit var search: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_mode_fragment)
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        search = findViewById(R.id.search)
        search.setOnClickListener {
            if (mapmyIndiaMap != null) {
                val placeOptions: PlaceOptions = PlaceOptions.builder()
                        .location(MapmyIndiaPlaceWidgetSetting.instance.location)
                        .filter(MapmyIndiaPlaceWidgetSetting.instance.filter)
                        .hint(MapmyIndiaPlaceWidgetSetting.instance.hint)
                        .saveHistory(MapmyIndiaPlaceWidgetSetting.instance.isEnableHistory)
                        .enableTextSearch(MapmyIndiaPlaceWidgetSetting.instance.isEnableTextSearch)
                        .pod(MapmyIndiaPlaceWidgetSetting.instance.pod)
                        .attributionHorizontalAlignment(MapmyIndiaPlaceWidgetSetting.instance.signatureVertical)
                        .attributionVerticalAlignment(MapmyIndiaPlaceWidgetSetting.instance.signatureHorizontal)
                        .logoSize(MapmyIndiaPlaceWidgetSetting.instance.logoSize)
                        .backgroundColor(resources.getColor(MapmyIndiaPlaceWidgetSetting.instance.backgroundColor))
                        .toolbarColor(resources.getColor(MapmyIndiaPlaceWidgetSetting.instance.toolbarColor))
                        .build(PlaceOptions.MODE_CARDS)

                val builder = PlaceAutocomplete.IntentBuilder()
                if (!MapmyIndiaPlaceWidgetSetting.instance.isDefault) {
                    builder.placeOptions(placeOptions)
                }
                val placeAutocomplete = builder.build(this@FullModeActivity)
                startActivityForResult(placeAutocomplete, 101)
            } else {
                Toast.makeText(this, "Please wait map is loading", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 101) {
            if(resultCode == Activity.RESULT_OK) {
                val eLocation : ELocation = Gson().fromJson(data?.getStringExtra(PlaceConstants.RETURNING_ELOCATION_DATA), ELocation::class.java)
                if (mapmyIndiaMap != null) {
                    mapmyIndiaMap?.clear()
                    val latLng = LatLng(eLocation.latitude?.toDouble()!!, eLocation.longitude?.toDouble()!!)
                    mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0))
                    mapmyIndiaMap?.addMarker(MarkerOptions().position(latLng).setTitle(eLocation.placeName).setSnippet(eLocation.placeAddress))
                }
                search.text = eLocation.placeName
            }
        }
    }

    override fun onMapError(p0: Int, p1: String?) {}

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap

        mapmyIndiaMap.cameraPosition = CameraPosition.Builder().target(LatLng(28.0, 77.0)).zoom(4.0).build()

    }



    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()

        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

}

