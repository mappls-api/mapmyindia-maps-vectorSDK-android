package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.LayoutBasicGeofenceBinding
import com.mapmyindia.sdk.demo.kotlin.settings.MapmyIndiaGeofenceSetting
import com.mapmyindia.sdk.geofence.ui.GeoFence
import com.mapmyindia.sdk.geofence.ui.listeners.GeoFenceViewCallback
import com.mapmyindia.sdk.geofence.ui.views.GeoFenceOptions
import com.mapmyindia.sdk.geofence.ui.views.GeoFenceView

class BasicGeofenceActivity: AppCompatActivity(), GeoFenceViewCallback {

    private lateinit  var mBinding: LayoutBasicGeofenceBinding
    private lateinit var geoFence: GeoFence
    private lateinit var geofenceView: GeoFenceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         mBinding = DataBindingUtil.setContentView(this, R.layout.layout_basic_geofence)

        mBinding = DataBindingUtil.setContentView(this, R.layout.layout_basic_geofence)
        geoFence = GeoFence()
         if (MapmyIndiaGeofenceSetting.instance.isDefault){
            geofenceView = GeoFenceView(this);
             geoFence.isPolygon= false
             geoFence.circleCenter=LatLng(24.6496185, 77.3062072)
            geoFence.circleRadius=200

        }else {
           val geoFenceOptions = GeoFenceOptions.builder();
            geoFenceOptions.circleOutlineWidth(MapmyIndiaGeofenceSetting.instance.circleOutlineWidth);
            geoFenceOptions.circleFillColor(MapmyIndiaGeofenceSetting.instance.circleFillColor);
            geoFenceOptions.circleFillOutlineColor(MapmyIndiaGeofenceSetting.instance.circleFillOutlineColor);
            geoFenceOptions.draggingLineColor(MapmyIndiaGeofenceSetting.instance.draggingLineColor);
            geoFenceOptions.maxRadius(MapmyIndiaGeofenceSetting.instance.maxRadius);
            geoFenceOptions.minRadius(MapmyIndiaGeofenceSetting.instance.minRadius);
            geoFenceOptions.polygonDrawingLineColor(MapmyIndiaGeofenceSetting.instance.polygonDrawingLineColor);
            geoFenceOptions.polygonFillColor(MapmyIndiaGeofenceSetting.instance.polygonFillColor);
            geoFenceOptions.polygonFillOutlineColor(MapmyIndiaGeofenceSetting.instance.polygonFillColor);
            geoFenceOptions.polygonOutlineWidth(MapmyIndiaGeofenceSetting.instance.polygonOutlineWidth);
             geoFenceOptions.showSeekBar(MapmyIndiaGeofenceSetting.instance.showSeekBar)
             geoFenceOptions.seekbarPrimaryColor(MapmyIndiaGeofenceSetting.instance.seekbarPrimaryColor)
             geoFenceOptions.seekbarSecondaryColor(MapmyIndiaGeofenceSetting.instance.seekbarSecondaryColor)
             geoFenceOptions.seekbarCornerRadius(MapmyIndiaGeofenceSetting.instance.seekbarCornerRadius)

            geofenceView =  GeoFenceView(this, geoFenceOptions.build())
             geoFence.isPolygon= MapmyIndiaGeofenceSetting.instance.isPolygon
             geoFence.circleCenter=LatLng(24.6496185, 77.3062072)
             geoFence.circleRadius=200

            geofenceView.simplifyWhenIntersectingPolygonDetected(MapmyIndiaGeofenceSetting.instance.isSimplifyWhenIntersectingPolygonDetected);

        }



        geofenceView.geoFence = geoFence
        geofenceView.onCreate(savedInstanceState)
        mBinding.rootLayout.addView(geofenceView)
        geofenceView.setGeoFenceViewCallback(this)

    }


    override fun onStart() {
        super.onStart()
        geofenceView.onStart()
    }

    override fun onResume() {
        super.onResume()
        geofenceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        geofenceView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        geofenceView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        geofenceView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        geofenceView.onDestroy()
    }

    override fun onCircleRadiusChanging(p0: Int) {

    }

    override fun onUpdateGeoFence(p0: GeoFence?) {

    }

    override fun onGeoFenceReady(p0: MapboxMap?) {

    }

    override fun hasIntersectionPoints() {

    }

    override fun geoFenceType(p0: Boolean) {

    }


}