package com.mapmyindia.sdk.demo.kotlin.settings

import android.R
import com.mapmyindia.sdk.geojson.Point
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mmi.services.api.directions.DirectionsCriteria

class MapmyIndiaDirectionWidgetSetting {
    var isDefault = true
    var isShowAlternative = true
    var isShowStartNavigation = true
    var isSteps = true
    var resource = DirectionsCriteria.RESOURCE_ROUTE
    var profile = DirectionsCriteria.PROFILE_DRIVING
    var overview = DirectionsCriteria.OVERVIEW_FULL
    var excludes: MutableList<String>? = null
    var destination: Point? = null
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

    companion object {
        val instance = MapmyIndiaDirectionWidgetSetting()
    }
}
