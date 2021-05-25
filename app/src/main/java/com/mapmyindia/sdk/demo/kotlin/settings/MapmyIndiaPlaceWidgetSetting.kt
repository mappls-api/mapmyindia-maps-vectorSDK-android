package com.mapmyindia.sdk.demo.kotlin.settings

import android.R
import com.mapbox.geojson.Point
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions

class MapmyIndiaPlaceWidgetSetting {
    var isDefault = true
    var signatureVertical = PlaceOptions.GRAVITY_TOP
    var signatureHorizontal = PlaceOptions.GRAVITY_LEFT
    var logoSize = PlaceOptions.SIZE_MEDIUM
    var location: Point? = null
    var filter: String? = null
    var isEnableHistory = false
    var pod: String? = null
    var hint = "Search Here"
    var isEnableTextSearch = false
     var backgroundColor = R.color.white
     var toolbarColor = R.color.white

    companion object {
        val instance = MapmyIndiaPlaceWidgetSetting()
    }
}
