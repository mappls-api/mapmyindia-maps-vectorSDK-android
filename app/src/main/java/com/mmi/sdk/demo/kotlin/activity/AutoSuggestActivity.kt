package com.mmi.sdk.demo.kotlin.activity

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.SupportMapFragment
import com.mmi.sdk.demo.R
import com.mmi.sdk.demo.java.utils.CheckInternet
import com.mmi.sdk.demo.kotlin.adapter.AutoSuggestAdapter
import com.mmi.sdk.demo.kotlin.kotlin.utility.TransparentProgressDialog
import com.mmi.services.api.auth.MapmyIndiaAuthentication
import com.mmi.services.api.auth.model.AtlasAuthToken
import com.mmi.services.api.autosuggest.MapmyIndiaAutoSuggest
import com.mmi.services.api.autosuggest.model.AutoSuggestAtlasResponse
import com.mmi.services.api.geocoding.GeoCodeResponse
import com.mmi.services.api.geocoding.MapmyIndiaGeoCoding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by CEINFO on 26-02-2019.
 */
class AutoSuggestActivity : AppCompatActivity(), OnMapReadyCallback, TextWatcher {

    lateinit var mapboxMap: MapboxMap
    private lateinit var autoSuggestText: EditText
    lateinit var recyclerView: RecyclerView
    lateinit var handler: Handler
    private var mLayoutManager: LinearLayoutManager? = null
    private lateinit var transparentDialog: TransparentProgressDialog
    lateinit var authToken: String
    lateinit var tokenType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.autosuggest_activity)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.mapBoxId) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        initReference()
    }

    override fun onMapReady(mapboxMap: MapboxMap?) {
        this.mapboxMap = mapboxMap!!
        mapboxMap.setMinZoomPreference(4.5)
        mapboxMap.setMaxZoomPreference(18.5)

        mapboxMap.setPadding(20, 20, 20, 20)
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
                22.553147478403194,
                77.23388671875), 4.0))
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        handler.postDelayed(Runnable {
            recyclerView.visibility = View.GONE
            if (s!!.length < 3)
                recyclerView.adapter = null

            if (s != null && s.toString().trim { it <= ' ' }.length < 2) {
                recyclerView.adapter = null
                return@Runnable
            }

            if (s.length > 2) {
                if (CheckInternet.isNetworkAvailable(this@AutoSuggestActivity)) {
                    callAutoSuggestApi(s.toString())
                } else {
                    showToast(getString(R.string.pleaseCheckInternetConnection))
                }
            }
        }, 300)
    }

    override fun onMapError(p0: Int, p1: String?) {}

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    private fun initReference() {
        recyclerView = findViewById(R.id.recyclerview)
        autoSuggestText = findViewById(R.id.auto_suggest)

        mLayoutManager = LinearLayoutManager(this@AutoSuggestActivity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.visibility = View.GONE

        if (CheckInternet.isNetworkAvailable(this@AutoSuggestActivity)) {
            getAuthToken()
        } else {
            showToast(getString(R.string.pleaseCheckInternetConnection))
        }

        handler = Handler()
        autoSuggestText.addTextChangedListener(this)
    }

    fun selectedPlaceName(name: String?) {
        if (name != null) {
            if (CheckInternet.isNetworkAvailable(this@AutoSuggestActivity)) {
                getGeoCode(name)
            } else {
                showToast(getString(R.string.pleaseCheckInternetConnection))
            }

        }
    }

    private fun getGeoCode(geocodeText: String) {
        show()
        MapmyIndiaGeoCoding.Builder<MapmyIndiaGeoCoding.Builder<*>>()
                .setAddress(geocodeText)
                .build().enqueueCall(object : Callback<GeoCodeResponse> {
                    override fun onResponse(call: Call<GeoCodeResponse>, response: Response<GeoCodeResponse>) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                val placesList = response.body()?.results
                                val place = placesList?.get(0)
                                addMarker(place?.latitude!!, place?.longitude!!)
                            } else {
                                showToast("Not able to get value, Try again.")
                                response.message()
                            }
                        } else {
                            showToast(response.message())
                        }
                        hide()
                    }

                    override fun onFailure(call: Call<GeoCodeResponse>, t: Throwable) {
                        showToast(t.toString())
                        hide()
                    }
                })
    }

    private fun addMarker(latitude: Double, longitude: Double) {
        mapboxMap.clear()
        mapboxMap.addMarker(MarkerOptions().position(LatLng(
                latitude, longitude)))
    }

    @Synchronized
    private fun callAutoSuggestApi(searchString: String) {

        MapmyIndiaAutoSuggest.Builder<MapmyIndiaAutoSuggest.Builder<*>>()
                .setQuery(searchString)
                .build()
                .enqueueCall(object : Callback<AutoSuggestAtlasResponse> {
                    override fun onResponse(call: Call<AutoSuggestAtlasResponse>, response: Response<AutoSuggestAtlasResponse>) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                val suggestedList = response.body()!!.suggestedLocations
                                if (suggestedList.size > 0) {
                                    recyclerView.visibility = View.VISIBLE
                                    val autoSuggestAdapter = AutoSuggestAdapter(suggestedList, object : AutoSuggestAdapter.PlaceName {
                                        override fun nameOfPlace(name: String) {
                                            selectedPlaceName(name)
                                            recyclerView.visibility = View.GONE
                                        }
                                    })
                                    recyclerView.adapter = autoSuggestAdapter
                                }
                            } else {
                                Toast.makeText(this@AutoSuggestActivity, "Not able to get value, Try again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<AutoSuggestAtlasResponse>, t: Throwable) {
                        println(t.toString())
                    }
                })
    }

    private fun getAuthToken() {

        MapmyIndiaAuthentication.Builder<MapmyIndiaAuthentication.Builder<*>>()
                .build()
                .enqueueCall(object : Callback<AtlasAuthToken> {
                    override fun onResponse(call: Call<AtlasAuthToken>, response: Response<AtlasAuthToken>) {
                        if (response.code() == 200) {
                            authToken = response.body()!!.getAccessToken()
                            tokenType = response.body()!!.getTokenType()
                        }

                    }

                    override fun onFailure(call: Call<AtlasAuthToken>, t: Throwable) {

                    }
                })
    }

    fun show() {
        transparentDialog = TransparentProgressDialog(this, R.drawable.circle_loader, "")
        transparentDialog.show()
    }

    fun hide() {
        transparentDialog.dismiss()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}