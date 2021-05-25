package com.mapmyindia.sdk.demo.kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityDirectionUiBinding
import com.mapmyindia.sdk.demo.kotlin.settingscreen.DirectionWidgetSettingActivity

class DirectionUiActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDirectionUiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_direction_ui)
        mBinding.btnOpenDirectionFragmentUi.setOnClickListener {
            startActivity(Intent(this, DirectionWidgetActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.widget_setting_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.widget_setting) {
            startActivity(Intent(this, DirectionWidgetSettingActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}