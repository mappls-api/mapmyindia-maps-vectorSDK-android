package com.mapmyindia.sdk.demo.kotlin.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.java.settingscreen.PlaceAutocompleteSettingActivity
import com.mapmyindia.sdk.demo.kotlin.settings.MapmyIndiaPlacePickerSetting
import com.mapmyindia.sdk.demo.kotlin.settingscreen.PlacePickerSettingsActivity
import com.mapmyindia.sdk.maps.camera.CameraPosition
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapmyindia.sdk.plugins.places.placepicker.PlacePicker
import com.mapmyindia.sdk.plugins.places.placepicker.model.PlacePickerOptions
import com.mmi.services.api.Place

class PickerActivity : AppCompatActivity() {

    private lateinit var tvSelectedPlace: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_picker)
        tvSelectedPlace = findViewById(R.id.selected_place)
        val button: Button = findViewById(R.id.place_picker)
        button.setOnClickListener {
            val intentBuilder = PlacePicker.IntentBuilder()

            if (!MapmyIndiaPlacePickerSetting.instance.isDefault) {
                val options = PlaceOptions.builder()
                        .zoom(MapmyIndiaPlacePickerSetting.instance.zoom)
                        .hint(MapmyIndiaPlacePickerSetting.instance.hint)
                        .location(MapmyIndiaPlacePickerSetting.instance.location)
                        .filter(MapmyIndiaPlacePickerSetting.instance.filter)
                        .saveHistory(MapmyIndiaPlacePickerSetting.instance.isEnableHistory)
                        .pod(MapmyIndiaPlacePickerSetting.instance.pod)
                        .attributionHorizontalAlignment(MapmyIndiaPlacePickerSetting.instance.signatureVertical)
                        .attributionVerticalAlignment(MapmyIndiaPlacePickerSetting.instance.signatureHorizontal)
                        .logoSize(MapmyIndiaPlacePickerSetting.instance.logoSize)
                        .tokenizeAddress(MapmyIndiaPlacePickerSetting.instance.isTokenizeAddress)
                        .historyCount(MapmyIndiaPlacePickerSetting.instance.historyCount)
                        .backgroundColor(resources.getColor(MapmyIndiaPlacePickerSetting.instance.backgroundColor))
                        .toolbarColor(resources.getColor(MapmyIndiaPlacePickerSetting.instance.toolbarColor))
                        .build()
                intentBuilder.placeOptions(PlacePickerOptions.builder()
                        .toolbarColor(MapmyIndiaPlacePickerSetting.instance.pickerToolbarColor)
                        .includeDeviceLocationButton(MapmyIndiaPlacePickerSetting.instance.isIncludeDeviceLocation)
                        .includeSearch(MapmyIndiaPlacePickerSetting.instance.isIncludeSearch)
                        .searchPlaceOption(options)
                        .statingCameraPosition(CameraPosition.Builder()
                                .target(LatLng(27.00, 75.0)).zoom(16.0).build())
                        .build())
            } else {
                intentBuilder.placeOptions(PlacePickerOptions.builder()
                        .statingCameraPosition(CameraPosition.Builder()
                                .target(LatLng(27.00, 75.0)).zoom(16.0).build())
                        .build())
            }

            val intent = intentBuilder.build(this@PickerActivity)
            startActivityForResult(intent, 101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 101 && resultCode == Activity.RESULT_OK) {
            val place: Place? = PlacePicker.getPlace(data!!)
            tvSelectedPlace.setText(place?.formattedAddress)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.widget_setting_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.widget_setting) {
            startActivity(Intent(this, PlacePickerSettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}