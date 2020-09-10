package com.mapmyindia.sdk.demo.kotlin.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
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
import com.mmi.services.api.autosuggest.model.ELocation
import com.mmi.services.api.textsearch.MapmyIndiaTextSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by CEINFO on 26-02-2019.
 */
class AutoSuggestActivity : AppCompatActivity(), OnMapReadyCallback, TextWatcher,TextView.OnEditorActionListener {

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
        Toast.makeText(this,"kotlin",Toast.LENGTH_SHORT).show()
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
            if (s?.length?:0 < 3)
                recyclerView.adapter = null

            if (s != null && s.toString().trim { it <= ' ' }.length < 2) {
                recyclerView.adapter = null
                return@Runnable
            }

            if (s?.length?:0 > 2) {
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
        autoSuggestText.setOnEditorActionListener(this)
    }

    fun selectedPlace(eLocation: ELocation) {
        val add:String="Latitude: " + eLocation.latitude + " longitude: " + eLocation.longitude
        addMarker(eLocation.latitude.toDouble(), eLocation.longitude.toDouble())
        showToast(add)
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
                                    val autoSuggestAdapter = AutoSuggestAdapter(suggestedList, object : AutoSuggestAdapter.PlaceData {
                                        override fun dataOfPlace(eLocation: ELocation) {
                                            selectedPlace(eLocation)
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
                        showToast(t.toString())
                    }
                })
    }

    private fun callTextSearchApi(searchString: String) {
        MapmyIndiaTextSearch.builder()
                .query(searchString)
                .build().enqueueCall(object : Callback<AutoSuggestAtlasResponse> {
                    override fun onResponse(call: Call<AutoSuggestAtlasResponse>, response: Response<AutoSuggestAtlasResponse>) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                val suggestedList = response.body()!!.suggestedLocations
                                if (suggestedList.size > 0) {
                                    recyclerView.visibility = View.VISIBLE
                                    val autoSuggestAdapter = AutoSuggestAdapter(suggestedList, object : AutoSuggestAdapter.PlaceData {
                                        override fun dataOfPlace(eLocation: ELocation) {
                                            selectedPlace(eLocation)
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
                        showToast(t.toString())
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

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            callTextSearchApi(v!!.text.toString())
            autoSuggestText.clearFocus()
            val inputMethodManger =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManger.hideSoftInputFromWindow(autoSuggestText.windowToken, 0)

        }
        return false
    }

}