package com.mapmyindia.sdk.demo.kotlin.activity

import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityNearbyReportBinding
import com.mapmyindia.sdk.demo.java.utils.TransparentProgressDialog
import com.mapmyindia.sdk.geojson.Point
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.annotations.MarkerOptions
import com.mapmyindia.sdk.maps.camera.CameraELocUpdateFactory
import com.mapmyindia.sdk.maps.camera.CameraPosition
import com.mapmyindia.sdk.maps.camera.CameraUpdateFactory
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import com.mmi.services.api.OnResponseCallback
import com.mmi.services.api.autosuggest.model.ELocation
import com.mmi.services.api.event.nearby.MapmyIndiaNearbyReport
import com.mmi.services.api.event.nearby.MapmyIndiaNearbyReportManager
import com.mmi.services.api.event.nearby.model.NearbyReport
import com.mmi.services.api.event.nearby.model.NearbyReportResponse

class NearbyReportActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mBinding: ActivityNearbyReportBinding
    private var mapmyIndiaMap: MapmyIndiaMap? = null
    private var transparentProgressDialog: TransparentProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_nearby_report)
        transparentProgressDialog = TransparentProgressDialog(this, R.drawable.circle_loader, "")
        mBinding.mapView.onCreate(savedInstanceState)
        mBinding.mapView.getMapAsync(this)
        mBinding.search.setOnClickListener {
            if (mapmyIndiaMap != null) {
                val placeOptions = PlaceOptions.builder()
                    .backgroundColor(Color.WHITE)
                    .build()
                val placeAutocompleteFragment: PlaceAutocompleteFragment =
                    PlaceAutocompleteFragment.newInstance(placeOptions)
                placeAutocompleteFragment.setOnPlaceSelectedListener(object :
                    PlaceSelectionListener {
                    override fun onPlaceSelected(eLocation: ELocation?) {
                        mapmyIndiaMap?.clear()
                        if (eLocation?.latitude != null && eLocation.longitude != null) {
                            mapmyIndiaMap!!.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        eLocation.latitude.toDouble(),
                                        eLocation.longitude.toDouble()
                                    ), 14.0
                                )
                            )
                        } else {
                            mapmyIndiaMap!!.animateCamera(
                                CameraELocUpdateFactory.newELocZoom(
                                    eLocation?.placeId!!,
                                    14.0
                                )
                            )
                        }
                        supportFragmentManager.popBackStack(
                            PlaceAutocompleteFragment::class.java.simpleName,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }

                    override fun onCancel() {
                        supportFragmentManager.popBackStack(
                            PlaceAutocompleteFragment::class.java.simpleName,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }
                })
                supportFragmentManager.beginTransaction().add(
                    R.id.fragment_container,
                    placeAutocompleteFragment,
                    PlaceAutocompleteFragment::class.java.simpleName
                )
                    .addToBackStack(PlaceAutocompleteFragment::class.java.simpleName)
                    .commit()
            }

        }

        mBinding.tvNearbyReport.setOnClickListener {
            val top = mBinding.selectionBox.top - mBinding.mapView.top
            val left = mBinding.selectionBox.left - mBinding.mapView.left
            val bottom = top + mBinding.selectionBox.height
            val right = left + mBinding.selectionBox.width
            if (mapmyIndiaMap != null) {
                val topLeft = mapmyIndiaMap!!.projection.fromScreenLocation(
                    PointF(
                        left.toFloat(), top.toFloat()
                    )
                )
                val rightBottom = mapmyIndiaMap!!.projection.fromScreenLocation(
                    PointF(
                        right.toFloat(), bottom.toFloat()
                    )
                )
                mapmyIndiaMap!!.clear()
                val mapmyIndiaNearbyReport = MapmyIndiaNearbyReport.builder()
                    .topLeft(
                        Point.fromLngLat(
                            topLeft.longitude,
                            topLeft.latitude
                        )
                    )
                    .bottomRight(
                        Point.fromLngLat(
                            rightBottom.longitude,
                            rightBottom.latitude
                        )
                    )
                    .build()
                progressDialogShow()
                MapmyIndiaNearbyReportManager.newInstance(mapmyIndiaNearbyReport)
                    .call(object : OnResponseCallback<NearbyReportResponse?> {
                        override fun onSuccess(nearbyReportResponse: NearbyReportResponse?) {
                            if (nearbyReportResponse != null && nearbyReportResponse.reports != null && nearbyReportResponse.reports.size > 0) {
                                for (nearbyReport in nearbyReportResponse.reports) {

                                        mapmyIndiaMap?.addMarker(
                                            MarkerOptions().position(
                                                LatLng(
                                                    nearbyReport.latitude,
                                                    nearbyReport.longitude
                                                )
                                            ).title(nearbyReport.category)
                                        )
                                    }

                            } else {
                                Toast.makeText(
                                    this@NearbyReportActivity,
                                    "No result found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            progressDialogHide()
                        }

                        override fun onError(i: Int, s: String) {
                            progressDialogHide()
                            Toast.makeText(this@NearbyReportActivity, s, Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }

    protected fun progressDialogShow() {
        transparentProgressDialog!!.show()
    }

    protected fun progressDialogHide() {
        transparentProgressDialog!!.dismiss()
    }

    override fun onStart() {
        super.onStart()
        mBinding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        mBinding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mBinding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding.mapView.onLowMemory()
    }

    override fun onMapReady(mapmyIndiaMap: MapmyIndiaMap) {
        this.mapmyIndiaMap = mapmyIndiaMap
        mapmyIndiaMap.uiSettings?.setLogoMargins(0, 0, 0, 100)
        mapmyIndiaMap.cameraPosition =
            CameraPosition.Builder().target(LatLng(28.550716, 77.268928)).zoom(12.0).build()
        mBinding.tvNearbyReport.visibility = View.VISIBLE
        mBinding.selectionBox.visibility = View.VISIBLE
        mBinding.search.visibility = View.VISIBLE
    }

    override fun onMapError(p0: Int, p1: String?) {

    }
}