package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityDirectionPluginBinding
import com.mapmyindia.sdk.direction.ui.DirectionCallback
import com.mapmyindia.sdk.direction.ui.DirectionFragment
import com.mapmyindia.sdk.direction.ui.model.DirectionOptions
import com.mapmyindia.sdk.direction.ui.model.DirectionPoint
import com.mmi.services.api.directions.models.DirectionsResponse


class DirectionPluginActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDirectionPluginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_direction_plugin)

        mBinding.btnOpenDirectionFragmentUi.setOnClickListener { v ->


            val options = DirectionOptions.builder().build()

            val directionFragment = DirectionFragment.newInstance(options)
            supportFragmentManager.
            beginTransaction().
            add(R.id.container, directionFragment, DirectionFragment::class.java.simpleName).
            addToBackStack(null).
            commit()

            directionFragment.setDirectionCallback(object : DirectionCallback {
                override fun onCancel() {
                    supportFragmentManager.beginTransaction().remove(directionFragment).commit()
                }

                override fun onStartNavigation(directionPoint: DirectionPoint, directionPoint1: DirectionPoint, list: List<DirectionPoint>, directionsResponse: DirectionsResponse, i: Int) {
                    Toast.makeText(this@DirectionPluginActivity, "On Navigation Start", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}