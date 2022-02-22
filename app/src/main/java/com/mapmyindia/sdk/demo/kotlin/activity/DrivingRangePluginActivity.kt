package com.mapmyindia.sdk.demo.kotlin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityDrivingRangePluginBinding
import com.mapmyindia.sdk.demo.kotlin.settingscreen.DrivingRangeSettingActivity

class DrivingRangePluginActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDrivingRangePluginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_driving_range_plugin)
        mBinding.btnOpenDrivingRange.setOnClickListener {
            startActivity(Intent(this, DrivingRangeActivity::class.java))
        }
        mBinding.btnSetting.setOnClickListener {
            startActivity(Intent(this, DrivingRangeSettingActivity::class.java))
        }
    }
}