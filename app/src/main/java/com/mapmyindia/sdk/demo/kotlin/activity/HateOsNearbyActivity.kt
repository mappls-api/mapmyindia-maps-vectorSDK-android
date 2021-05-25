package com.mapmyindia.sdk.demo.kotlin.activity

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.java.adapter.AutoSuggestSearchesAdapter
import com.mapmyindia.sdk.demo.java.adapter.NearByAdapter
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog
import com.mmi.services.api.autosuggest.MapmyIndiaAutoSuggest
import com.mmi.services.api.autosuggest.model.AutoSuggestAtlasResponse
import com.mmi.services.api.hateaosnearby.MapmyIndiaHateosNearby
import com.mmi.services.api.nearby.model.NearbyAtlasResponse
import com.mmi.services.api.nearby.model.NearbyAtlasResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HateOsNearbyActivity : AppCompatActivity(), OnMapReadyCallback,TextView.OnEditorActionListener {

    private lateinit var mapmyIndiaMap: MapboxMap
    private lateinit var autoSuggestText: EditText
    private lateinit var mapView: MapView
    private lateinit var transparentProgressDialog: TransparentProgressDialog
    private lateinit var nearbyRecyclerView: RecyclerView
    private  lateinit var autoSuggestRecyclerView:RecyclerView
    private var count = 0
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hate_os_nearby)
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initReferences()
        initListeners()


        floatingActionButton = findViewById(R.id.marker_list)
        floatingActionButton.setImageResource(R.drawable.location_pointer)
        count = 0
        floatingActionButton.setOnClickListener { v: View? ->
            if (count == 0) {
                count = 1
                floatingActionButton.setImageResource(R.drawable.listing_option)
                nearbyRecyclerView.visibility = View.GONE
            } else {
                count = 0
                floatingActionButton.setImageResource(R.drawable.location_pointer)
                nearbyRecyclerView.visibility = View.VISIBLE
            }
        }

        transparentProgressDialog = TransparentProgressDialog(this, R.drawable.circle_loader, "")
    }

    private fun initListeners() {
        autoSuggestText.setOnEditorActionListener(this)
    }

    private fun initReferences() {
        autoSuggestText = findViewById(R.id.auto_suggest)
        nearbyRecyclerView = findViewById<RecyclerView>(R.id.nearByRecyclerview)
        autoSuggestRecyclerView = findViewById<RecyclerView>(R.id.auto_suggest_recyclerView)
        mLayoutManager = LinearLayoutManager(this)
        nearbyRecyclerView.layoutManager = mLayoutManager
        autoSuggestRecyclerView.layoutManager = LinearLayoutManager(this)
        autoSuggestRecyclerView.visibility = View.GONE
    }

    private fun progressDialogShow() {
        transparentProgressDialog.show()
    }

    private fun progressDialogHide() {
        transparentProgressDialog.dismiss()
    }


    private fun callAutoSuggestApi(searchString: String) {
        progressDialogShow()
        MapmyIndiaAutoSuggest.builder()
                .query(searchString)
                .bridge(true)
                .build()
                .enqueueCall(object : Callback<AutoSuggestAtlasResponse?> {
                    override fun onResponse(call: Call<AutoSuggestAtlasResponse?>, response: Response<AutoSuggestAtlasResponse?>) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                val suggestedSearches = response.body()!!.suggestedSearches
                                if (suggestedSearches.size > 0) {
                                    autoSuggestRecyclerView.visibility = View.VISIBLE
                                    val autoSuggestAdapter = AutoSuggestSearchesAdapter(suggestedSearches, AutoSuggestSearchesAdapter.SuggestedSearches { hyperlink: String? ->
                                        callHateOs(hyperlink!!)
                                        autoSuggestRecyclerView.visibility = View.GONE
                                    })
                                    autoSuggestRecyclerView.adapter = autoSuggestAdapter
                                } else {
                                    Toast.makeText(this@HateOsNearbyActivity, "No hyperlinks found...", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                showToast("Not able to get value, Try again.")
                            }
                        } else {

                            showToast(response.message())
                        }
                        progressDialogHide()
                    }

                    override fun onFailure(call: Call<AutoSuggestAtlasResponse?>, t: Throwable) {
                        showToast(t.toString())
                        progressDialogHide()
                    }
                })
    }

    private fun callHateOs(hyperlink: String) {
        MapmyIndiaHateosNearby.builder()
                .hyperlink(hyperlink)
                .build()
                .enqueueCall(object : Callback<NearbyAtlasResponse?> {
                    override fun onResponse(call: Call<NearbyAtlasResponse?>, response: Response<NearbyAtlasResponse?>) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                val nearByList = response.body()!!.suggestedLocations
                                if (nearByList.size > 0) {
                                    addMarker(nearByList)
                                }
                            } else {
                                Toast.makeText(this@HateOsNearbyActivity, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@HateOsNearbyActivity, response.message(), Toast.LENGTH_LONG).show()
                        }
                        progressDialogHide()
                    }

                    override fun onFailure(call: Call<NearbyAtlasResponse?>, t: Throwable) {
                        showToast(t.toString())
                        progressDialogHide()
                    }
                })
    }

    private fun addMarker(nearByList: ArrayList<NearbyAtlasResult>) {
        mapmyIndiaMap.clear()
        for (marker in nearByList) {
            mapmyIndiaMap.addMarker(MarkerOptions().position(LatLng(marker.getLatitude(), marker.getLongitude())).title(marker.getPlaceName()))
        }
        nearbyRecyclerView.adapter = NearByAdapter(nearByList)
    }


    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    override fun onMapError(p0: Int, p1: String?) {
    }

    override fun onMapReady(mapmyIndiaMap: MapboxMap) {
        this.mapmyIndiaMap = mapmyIndiaMap


        mapmyIndiaMap.setPadding(20, 20, 20, 20)


        mapmyIndiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
                22.553147478403194,
                77.23388671875), 4.0))
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            callAutoSuggestApi(v!!.text.toString())
            autoSuggestText.clearFocus()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(autoSuggestText.windowToken, 0)
            return true
        }
        return false
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