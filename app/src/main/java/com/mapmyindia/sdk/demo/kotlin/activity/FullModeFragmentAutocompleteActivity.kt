package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.settings.MapmyIndiaPlaceWidgetSetting
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import com.mmi.services.api.autosuggest.model.ELocation


class FullModeFragmentAutocompleteActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private var mapmyIndiaMap: MapboxMap? = null



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
                        .enableTextSearch(MapmyIndiaPlaceWidgetSetting.instance.isEnableTextSearch)
                        .pod(MapmyIndiaPlaceWidgetSetting.instance.pod)
                        .saveHistory(MapmyIndiaPlaceWidgetSetting.instance.isEnableHistory)
                        .attributionHorizontalAlignment(MapmyIndiaPlaceWidgetSetting.instance.signatureVertical)
                        .attributionVerticalAlignment(MapmyIndiaPlaceWidgetSetting.instance.signatureHorizontal)
                        .logoSize(MapmyIndiaPlaceWidgetSetting.instance.logoSize)
                        .backgroundColor(resources.getColor(MapmyIndiaPlaceWidgetSetting.instance.backgroundColor))
                        .toolbarColor(resources.getColor(MapmyIndiaPlaceWidgetSetting.instance.toolbarColor))
                        .build()

                val placeAutocompleteFragment: PlaceAutocompleteFragment = if (MapmyIndiaPlaceWidgetSetting.instance.isDefault) {
                    PlaceAutocompleteFragment.newInstance()
                } else {
                    PlaceAutocompleteFragment.newInstance(placeOptions)
                }
                placeAutocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
                    override fun onCancel() {
                        supportFragmentManager.popBackStack(PlaceAutocompleteFragment::class.java.simpleName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }

                    override fun onPlaceSelected(eLocation: ELocation?) {
                        if (mapmyIndiaMap != null) {
                            mapmyIndiaMap?.clear()
                            val latLng = LatLng(eLocation?.latitude?.toDouble()!!, eLocation.longitude?.toDouble()!!)
                            mapmyIndiaMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0))
                            mapmyIndiaMap?.addMarker(MarkerOptions().position(latLng).setTitle(eLocation.placeName).setSnippet(eLocation.placeAddress))
                        }
                        search.text = eLocation?.placeName
                        supportFragmentManager.popBackStack(PlaceAutocompleteFragment::class.java.simpleName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }

                })
                supportFragmentManager.beginTransaction().add(R.id.fragment_container, placeAutocompleteFragment, PlaceAutocompleteFragment::class.java.simpleName)
                        .addToBackStack(PlaceAutocompleteFragment::class.java.simpleName)
                        .commit()
            } else {
                Toast.makeText(this, "Please wait map is loading", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onMapError(p0: Int, p1: String?) {}

    override fun onMapReady(mapmyIndiaMap: MapboxMap?) {
        this.mapmyIndiaMap = mapmyIndiaMap

        mapmyIndiaMap?.setPadding(20, 20, 20, 20)

        mapmyIndiaMap!!.setMinZoomPreference(4.0)
        mapmyIndiaMap.setMaxZoomPreference(18.0)

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
