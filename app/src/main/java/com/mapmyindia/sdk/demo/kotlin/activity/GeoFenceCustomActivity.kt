package com.mapmyindia.sdk.demo.kotlin.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.LayoutGeofenceUiActivityBinding
import com.mapmyindia.sdk.demo.kotlin.model.GeofenceDetail
import com.mapmyindia.sdk.geofence.ui.GeoFence
import com.mapmyindia.sdk.geofence.ui.listeners.GeoFenceViewCallback
import com.mapmyindia.sdk.geofence.ui.util.Orientation

class GeoFenceCustomActivity: AppCompatActivity(), GeoFenceViewCallback {


    private lateinit var mBinding: LayoutGeofenceUiActivityBinding
    var geoFence: GeoFence? = null
    var geofenceDetail: GeofenceDetail? = null
    private val mapmyIndiaMap: MapboxMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(intent != null && intent.hasExtra("Geofence")) {
            geofenceDetail = Gson().fromJson(intent.getStringExtra("Geofence"), GeofenceDetail::class.java)
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.layout_geofence_ui_activity);
        if(geofenceDetail == null) {
            geoFence = GeoFence();
            geoFence?.isPolygon = false
            geoFence?.circleCenter = LatLng(24.6496185, 77.3062072)
            geoFence?.circleRadius = 200
        } else {
            geoFence = GeoFence()
            if(geofenceDetail?.gfType?.equals(GeofenceDetail.TYPE_POLYGON, ignoreCase = true)!!) {
                geoFence?.isPolygon = true
                val pointList: MutableList<List<Point>> = ArrayList()
                pointList.add(geofenceDetail?.polygonPoints!!)
                geoFence?.polygon = pointList
            } else {
                geoFence?.isPolygon = false
                geoFence?.circleCenter = geofenceDetail?.circleCentre
                geoFence?.circleRadius = geofenceDetail?.circleRadius
            }
        }
            mBinding.idSeekBar.max = mBinding.geoFenceView.maxProgress
            mBinding.geoFenceView.geoFence = geoFence
            mBinding.geoFenceView.setCameraPadding(20, 20, 20, 20)

            mBinding.btnApply.setOnClickListener {
                val geoFence = mBinding.geoFenceView.geoFence

                if(geoFence != null) {
                    if (geofenceDetail == null) {
                        geofenceDetail = GeofenceDetail()
                        geofenceDetail?.active = false
                    }

                    if(geoFence.isPolygon) {
                        geofenceDetail?.gfType = GeofenceDetail.TYPE_POLYGON
                        if(geoFence.polygon == null) {
                            Toast.makeText(this, "Please draw Polygon first", Toast.LENGTH_SHORT).show();
                            return@setOnClickListener
                        }
                        geofenceDetail?.polygonPoints = geoFence.polygon?.get(0)

                    } else {
                        geofenceDetail?.gfType = GeofenceDetail.TYPE_CIRCLE
                        geofenceDetail?.circleCentre = geoFence.circleCenter
                        geofenceDetail?.circleRadius = geoFence.circleRadius
                    }

                    val geoFenceIntent = Gson().toJson(geofenceDetail)
                    val intent = Intent()
                    intent.putExtra("GEOFENCE", geoFenceIntent)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }

        mBinding.geoFenceView.onCreate(savedInstanceState)
        mBinding.geoFenceView.setGeoFenceViewCallback(this@GeoFenceCustomActivity)


        mBinding.geoFenceView.convertPointsToClockWise(Orientation.CLOCKWISE)

        mBinding.geoFenceView.simplifyWhenIntersectingPolygonDetected(true)

        mBinding.idSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mBinding.geoFenceView.onRadiusChange(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mBinding.geoFenceView.radiusChangeFinish(seekBar.progress)
            }
        })

        mBinding.cbAction.setOnCheckedChangeListener { buttonView, isChecked -> mBinding.geoFenceView.enablePolygonDrawing(isChecked) }
        mBinding.idCircleButton.setOnClickListener { mBinding.geoFenceView.drawCircleGeoFence() }
        mBinding.idPolygonButton.setOnClickListener { mBinding.geoFenceView.drawPolygonGeofence() }
    }

    override fun onCircleRadiusChanging(radius: Int) {
        mBinding.idSeekBarValue.text = "$radius m"
    }

    override fun onUpdateGeoFence(geoFence: GeoFence?) {
        if (geoFence?.isPolygon?:false) {
            mBinding.cbAction.visibility = View.GONE
            mBinding.cbAction.isChecked = false

        }


    }

    override fun onGeoFenceReady(p0: MapboxMap?) {
        mBinding.toolsView.visibility = View.VISIBLE
    }

    override fun hasIntersectionPoints() {
    }

    override fun geoFenceType(polygon: Boolean) {
        if (polygon) {
            mBinding.cbAction.visibility = View.VISIBLE
        } else {
            mBinding.cbAction.visibility = View.GONE
        }
        mBinding.seekBarView.visibility = if (polygon) View.GONE else View.VISIBLE

        mBinding.cbAction.isChecked = polygon
        mBinding.ivButtonCircle.isSelected = !polygon
        mBinding.ivButtonPolygon.isSelected = polygon
        if (polygon) {
            if (mBinding.geoFenceView.geoFence.polygon != null) {
                mBinding.cbAction.visibility = View.GONE
            }
        } else {
            if (mBinding.geoFenceView.geoFence.circleRadius != null) {
                mBinding.idSeekBar.progress = mBinding.geoFenceView.progress
                mBinding.geoFenceView.radiusChangeFinish(mBinding.idSeekBar.progress)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        mBinding.geoFenceView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mBinding.geoFenceView.onResume()
    }


    override fun onPause() {
        super.onPause()
        mBinding.geoFenceView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.geoFenceView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.geoFenceView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.geoFenceView.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        mBinding.geoFenceView.onStop()
    }

}