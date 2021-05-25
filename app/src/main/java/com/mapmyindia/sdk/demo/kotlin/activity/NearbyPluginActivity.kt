package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityNearbyPluginBinding
import com.mapmyindia.sdk.nearby.plugin.IMapmyIndiaNearbyCallback
import com.mapmyindia.sdk.nearby.plugin.MapmyIndiaNearbyFragment
import com.mmi.services.api.nearby.model.NearbyAtlasResult

class NearbyPluginActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityNearbyPluginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_nearby_plugin)
        val nearbyFragment = MapmyIndiaNearbyFragment.newInstance()
        supportFragmentManager.beginTransaction().add(mBinding.fragmentContainer.id, nearbyFragment, MapmyIndiaNearbyFragment::class.java.simpleName)
                .commit()

        nearbyFragment.setMapmyIndiaNearbyCallback(object : IMapmyIndiaNearbyCallback {
            override fun getNearbyCallback(nearbyAtlasResult: NearbyAtlasResult) {
                Toast.makeText(this@NearbyPluginActivity, nearbyAtlasResult.placeAddress, Toast.LENGTH_SHORT).show()
            }
        })
    }
}