package com.mapmyindia.sdk.demo.kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityGeoFenceBinding
import com.mapmyindia.sdk.demo.kotlin.settingscreen.DirectionWidgetSettingActivity
import com.mapmyindia.sdk.demo.kotlin.settingscreen.GeofenceWidgetSettingsActivity

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.widget_setting_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.widget_setting) {
            startActivity(Intent(this, GeofenceWidgetSettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}