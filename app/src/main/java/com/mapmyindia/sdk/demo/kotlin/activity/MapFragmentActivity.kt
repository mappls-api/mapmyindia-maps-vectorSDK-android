package com.mapmyindia.sdk.demo.kotlin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.fragments.MapFragment

class MapFragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_fragment)

        supportFragmentManager.beginTransaction().add(R.id.container_id, MapFragment(), MapFragment::class.java.simpleName).commit()
    }
}