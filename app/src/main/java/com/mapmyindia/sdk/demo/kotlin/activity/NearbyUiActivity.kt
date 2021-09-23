package com.mapmyindia.sdk.demo.kotlin.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityNearbyUiBinding
import com.mapmyindia.sdk.nearby.plugin.IMapmyIndiaNearbyCallback
import com.mapmyindia.sdk.nearby.plugin.MapmyIndiaNearbyFragment
import com.mapmyindia.sdk.nearby.plugin.MapmyIndiaNearbyWidget
import com.mmi.services.api.nearby.model.NearbyAtlasResult

class NearbyUiActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityNearbyUiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_nearby_ui)

        mBinding.btnOpenNearbyActivity.setOnClickListener { v ->
            val intent = MapmyIndiaNearbyWidget.IntentBuilder().build(this)
            startActivityForResult(intent, 101)
        }

        mBinding.btnOpenNearbyFragmentUi.setOnClickListener {
            startActivity(Intent(this, NearbyPluginActivity::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            val place: NearbyAtlasResult? = MapmyIndiaNearbyWidget.getNearbyResponse(data!!)
            Toast.makeText(this, place?.placeAddress, Toast.LENGTH_SHORT).show()
        }
    }
}