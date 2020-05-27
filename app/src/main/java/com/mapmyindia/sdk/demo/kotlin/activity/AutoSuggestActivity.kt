package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.SupportMapFragment
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.java.utils.CheckInternet
import com.mapmyindia.sdk.demo.kotlin.adapter.AutoSuggestAdapter
import com.mapmyindia.sdk.demo.kotlin.kotlin.utility.TransparentProgressDialog
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

    lateinit var mapmyIndiaMap: MapboxMap
    private lateinit var autoSuggestText: EditText
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    lateinit var handler: Handler
    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
    private lateinit var transparentDialog: TransparentProgressDialog
    lateinit var authToken: String
    lateinit var tokenType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.autosuggest_activity)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map_view) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        initReference()
    }

    override fun onMapReady(mapmyIndiaMap: MapboxMap?) {
        this.mapmyIndiaMap = mapmyIndiaMap!!



        mapmyIndiaMap.setPadding(20, 20, 20, 20)
        mapmyIndiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
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

        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@AutoSuggestActivity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.visibility = View.GONE



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
        MapmyIndiaGeoCoding.builder()
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
        mapmyIndiaMap.clear()
        mapmyIndiaMap.addMarker(MarkerOptions().position(LatLng(
                latitude, longitude)))
    }

    @Synchronized
    private fun callAutoSuggestApi(searchString: String) {

        MapmyIndiaAutoSuggest.builder()
                .query(searchString)
                .tokenizeAddress(true)
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