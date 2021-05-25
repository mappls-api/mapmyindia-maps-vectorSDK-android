package com.mapmyindia.sdk.demo.kotlin.settings

import android.graphics.Color


class MapmyIndiaGeofenceSetting {
    var isDefault = true
    var circleOutlineWidth = 1f
    var circleFillColor = Color.parseColor("#D81B60")
    var circleFillOutlineColor = Color.parseColor("#511050")
    var draggingLineColor = Color.parseColor("#000000")
    var maxRadius = 1000
    var minRadius = 25
    var polygonDrawingLineColor = Color.parseColor("#000000")
    var polygonFillColor = Color.parseColor("#511050")
    var polygonFillOutlineColor = Color.parseColor("#511050")
    var polygonOutlineWidth = 1f
    var isSimplifyWhenIntersectingPolygonDetected = false
    var isPolygon = false
     var seekbarPrimaryColor = Color.parseColor("#D81B60")
     var seekbarSecondaryColor = Color.parseColor("#511050")
     var seekbarCornerRadius = 5f
     var showSeekBar = true

    companion object {
        val instance = MapmyIndiaGeofenceSetting()
    }
}
