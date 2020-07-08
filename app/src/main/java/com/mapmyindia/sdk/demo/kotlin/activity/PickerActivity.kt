package com.mapmyindia.sdk.demo.kotlin.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapmyindia.sdk.demo.R
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
            val intent = PlacePicker.IntentBuilder()
                    .placeOptions(PlacePickerOptions.builder()
                                .statingCameraPosition(CameraPosition.Builder()
                                        .target(LatLng(27.00, 75.0)).zoom(16.0).build())
                            .build()).build(this)
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
}