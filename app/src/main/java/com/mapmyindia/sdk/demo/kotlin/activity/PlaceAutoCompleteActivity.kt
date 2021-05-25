package com.mapmyindia.sdk.demo.kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.settingscreen.PlaceAutocompleteSettingActivity

class PlaceAutoCompleteActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_autocomplete)

        val btnFragmentCardMode: Button = findViewById(R.id.fragment_card_mode)
        val btnFragmentFullMode: Button = findViewById(R.id.fragment_full_mode)
        val btnActivityCardMode: Button = findViewById(R.id.activity_card_mode)
        val btnActivityFullMode: Button = findViewById(R.id.activity_full_mode)

        btnFragmentCardMode.setOnClickListener(this)
        btnFragmentFullMode.setOnClickListener(this)
        btnActivityCardMode.setOnClickListener(this)
        btnActivityFullMode.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.widget_setting_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.widget_setting) {
            startActivity(Intent(this,PlaceAutocompleteSettingActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.fragment_card_mode -> {
                startActivity(Intent(this, CardModeFragmentAutocompleteActivity::class.java))
            }

            R.id.fragment_full_mode -> {
                startActivity(Intent(this, FullModeFragmentAutocompleteActivity::class.java))
            }

            R.id.activity_full_mode -> {
                startActivity(Intent(this, FullModeActivity::class.java))
            }

            R.id.activity_card_mode -> {
                startActivity(Intent(this, CardModeActivity::class.java))
            }
        }
    }
}
