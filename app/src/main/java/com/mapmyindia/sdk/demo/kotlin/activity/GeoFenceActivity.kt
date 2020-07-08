package com.mapmyindia.sdk.demo.kotlin.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityGeoFenceBinding

class GeoFenceActivity : AppCompatActivity() {

   private lateinit var mBinding: ActivityGeoFenceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_geo_fence)

        mBinding.btnBasicGeofenceActivity.setOnClickListener {
            val intent = Intent(this@GeoFenceActivity, BasicGeofenceActivity::class.java)
            startActivity(intent)
        }
        mBinding.btnGeofenceActivityUi.setOnClickListener {
            val intent = Intent(this@GeoFenceActivity, GeoFenceDetailActivity::class.java)
            startActivity(intent)
        }

    }
}