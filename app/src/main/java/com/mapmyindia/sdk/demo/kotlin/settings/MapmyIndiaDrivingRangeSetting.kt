package com.mapmyindia.sdk.demo.kotlin.settings

import com.mapmyindia.sdk.drivingrange.DrivingRangeCriteria
import com.mapmyindia.sdk.geojson.Point

/**
 ** Created by Saksham on 17-01-2022.
 **/
class MapmyIndiaDrivingRangeSetting {
    var location = Point.fromLngLat(77.218527, 28.632282)
    var isUsingCurrentLocation = true
    var rangeType = DrivingRangeCriteria.RANGE_TYPE_TIME
    var contourValue = 50
    var contourColor = "ff0000"
    var drivingProfile = "auto"
    var isShowLocations = false
    var isForPolygon = true
    var denoise = 0.5f
    var generalize = 1.2f
    var speedType = SPEED_TYPE_OPTIMAL
    var predectiveType = PREDECTIVE_TYPE_CURRENT
    var time = System.currentTimeMillis()

    companion object {
        val instance = MapmyIndiaDrivingRangeSetting()
        const val SPEED_TYPE_OPTIMAL = 0
        const val SPEED_TYPE_PREDECTIVE = 1
        const val PREDECTIVE_TYPE_CURRENT = 0
        const val PREDECTIVE_TYPE_CUSTOM = 1
    }

}