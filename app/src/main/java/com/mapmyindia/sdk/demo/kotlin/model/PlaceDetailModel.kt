package com.mapmyindia.sdk.demo.kotlin.model

/**
 ** Created by Saksham on 26-11-2020.
 **/
data class PlaceDetailModel(val type: Int, val title: String, val value: String) {
    companion object {
        const val TYPE_ITEM = 0
        const val TYPE_HEADER = 1
    }
}