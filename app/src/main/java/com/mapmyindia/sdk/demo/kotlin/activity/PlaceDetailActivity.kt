package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityPlaceDetailBinding
import com.mapmyindia.sdk.demo.kotlin.adapter.PlaceDetailAdapter
import com.mapmyindia.sdk.demo.kotlin.model.PlaceDetailModel
import com.mmi.services.api.placedetail.MapmyIndiaPlaceDetail
import com.mmi.services.api.placedetail.model.PlaceDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 ** Created by Saksham on 26-11-2020.
 **/

class PlaceDetailActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPlaceDetailBinding
    private var adapter: PlaceDetailAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_place_detail)
        adapter = PlaceDetailAdapter()
        mBinding.rvPlaceDetail.layoutManager = LinearLayoutManager(this)
        mBinding.rvPlaceDetail.adapter = adapter
        mBinding.btnSearch.setOnClickListener {
            val eLoc: String = mBinding.etEloc.text.toString()
            if (eLoc.isNotEmpty()) {
                callELoc(eLoc)
            } else {
                Toast.makeText(this@PlaceDetailActivity, "Please add ELoc", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun callELoc(eLoc: String) {
        mBinding.progressBar.visibility = View.VISIBLE
        MapmyIndiaPlaceDetail.builder()
                .eLoc(eLoc)
                .build().enqueueCall(object : Callback<PlaceDetailResponse?> {
                    override fun onResponse(call: Call<PlaceDetailResponse?>, response: Response<PlaceDetailResponse?>) {
                        mBinding.progressBar.visibility = View.GONE
                        if (response.code() == 200) {
                            val placeDetailResponse = response.body()
                            if (placeDetailResponse != null) {
                                setValues(placeDetailResponse)
                            } else {
                                Toast.makeText(this@PlaceDetailActivity, "No Data Found", Toast.LENGTH_SHORT).show()
                            }
                        } else if (response.code() == 204) {
                            Toast.makeText(this@PlaceDetailActivity, "No Data Found", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@PlaceDetailActivity, "" + response.code(), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<PlaceDetailResponse?>, t: Throwable) {
                        mBinding.progressBar.visibility = View.GONE
                        t.printStackTrace()
                        Toast.makeText(this@PlaceDetailActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun setValues(placeDetailResponse: PlaceDetailResponse) {
        val placeDetailModels: MutableList<PlaceDetailModel> = ArrayList()
        addItem(placeDetailModels, "ELoc", placeDetailResponse.geteLoc())
        addItem(placeDetailModels, "Latitude", placeDetailResponse.latitude.toString() + "")
        addItem(placeDetailModels, "Longitude", placeDetailResponse.longitude.toString() + "")
        addItem(placeDetailModels, "Place Name", placeDetailResponse.placeName)
        addItem(placeDetailModels, "Place Address", placeDetailResponse.address)
        addItem(placeDetailModels, "City", placeDetailResponse.city)
        addItem(placeDetailModels, "House Name", placeDetailResponse.houseName)
        addItem(placeDetailModels, "District", placeDetailResponse.district)
        addItem(placeDetailModels, "House Number", placeDetailResponse.houseNumber)
        addItem(placeDetailModels, "Locality", placeDetailResponse.locality)
        addItem(placeDetailModels, "Pin code", placeDetailResponse.pincode)
        addItem(placeDetailModels, "POI", placeDetailResponse.poi)
        addItem(placeDetailModels, "State", placeDetailResponse.state)
        addItem(placeDetailModels, "Street", placeDetailResponse.street)
        addItem(placeDetailModels, "Sub District", placeDetailResponse.subDistrict)
        adapter?.setPlaceDetailModels(placeDetailModels)
    }

    private fun addItem(placeDetailModels: MutableList<PlaceDetailModel>, title: String, value: String?) {
        if (value != null) {
            placeDetailModels.add(PlaceDetailModel(PlaceDetailModel.TYPE_ITEM, title, value))
        }
    }
}