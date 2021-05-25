package com.mapmyindia.sdk.demo.kotlin.settings

import android.R
import com.mapbox.geojson.Point
import com.mapmyindia.sdk.demo.java.settings.MapmyIndiaPlacePickerSetting
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions

 class MapmyIndiaPlacePickerSetting {


     companion object {
         val instance = MapmyIndiaPlacePickerSetting()
     }


    var isDefault = true
    var pickerToolbarColor = R.color.white
    var isIncludeDeviceLocation = true
    var isIncludeSearch = true
    var location: Point? = null
    var filter: String? = null
    var isEnableHistory = false
    var pod: String? = null
    var isTokenizeAddress = true
    var backgroundColor = R.color.white
    var toolbarColor = R.color.white
     var hint = "Search Here"
    var signatureVertical = PlaceOptions.GRAVITY_TOP
    var signatureHorizontal = PlaceOptions.GRAVITY_LEFT
    var logoSize = PlaceOptions.SIZE_MEDIUM
    var historyCount: Int? = null
    var zoom: Double? = null




}
