package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityDirectionWidgetBinding
import com.mapmyindia.sdk.demo.kotlin.settings.MapmyIndiaDirectionWidgetSetting
import com.mapmyindia.sdk.direction.ui.DirectionCallback
import com.mapmyindia.sdk.direction.ui.DirectionFragment
import com.mapmyindia.sdk.direction.ui.model.DirectionOptions
import com.mapmyindia.sdk.direction.ui.model.DirectionPoint
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mmi.services.api.directions.models.DirectionsResponse

class DirectionWidgetActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDirectionWidgetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_direction_widget)
        val optionsBuilder = DirectionOptions.builder()
        if (!MapmyIndiaDirectionWidgetSetting.instance.isDefault) {
            val options = PlaceOptions.builder()
                .zoom(MapmyIndiaDirectionWidgetSetting.instance.zoom)
                .hint(MapmyIndiaDirectionWidgetSetting.instance.hint)
                .location(MapmyIndiaDirectionWidgetSetting.instance.location)
                .filter(MapmyIndiaDirectionWidgetSetting.instance.filter)
                .saveHistory(MapmyIndiaDirectionWidgetSetting.instance.isEnableHistory)
                .pod(MapmyIndiaDirectionWidgetSetting.instance.pod)
                .attributionHorizontalAlignment(MapmyIndiaDirectionWidgetSetting.instance.signatureVertical)
                .attributionVerticalAlignment(MapmyIndiaDirectionWidgetSetting.instance.signatureHorizontal)
                .logoSize(MapmyIndiaDirectionWidgetSetting.instance.logoSize)
                .tokenizeAddress(MapmyIndiaDirectionWidgetSetting.instance.isTokenizeAddress)
                .historyCount(MapmyIndiaDirectionWidgetSetting.instance.historyCount)
                .backgroundColor(resources.getColor(MapmyIndiaDirectionWidgetSetting.instance.backgroundColor))
                .toolbarColor(resources.getColor(MapmyIndiaDirectionWidgetSetting.instance.toolbarColor))
                .build()
            optionsBuilder.searchPlaceOption(options)
                .showStartNavigation(MapmyIndiaDirectionWidgetSetting.instance.isShowStartNavigation)
                .steps(MapmyIndiaDirectionWidgetSetting.instance.isSteps)
                .resource(MapmyIndiaDirectionWidgetSetting.instance.resource)
                .profile(MapmyIndiaDirectionWidgetSetting.instance.profile)
                .excludes(MapmyIndiaDirectionWidgetSetting.instance.excludes)
                .overview(MapmyIndiaDirectionWidgetSetting.instance.overview)
                .showAlternative(MapmyIndiaDirectionWidgetSetting.instance.isShowAlternative)
                .searchAlongRoute(MapmyIndiaDirectionWidgetSetting.instance.isShowPOISearch)
        }

        val directionFragment = DirectionFragment.newInstance(optionsBuilder.build())
        supportFragmentManager.beginTransaction().add(
            mBinding.fragmentConatiner.id,
            directionFragment,
            DirectionFragment::class.java.simpleName
        ).commit()

        directionFragment.setDirectionCallback(object : DirectionCallback {
            override fun onCancel() {
                finish()
            }

            override fun onStartNavigation(
                directionPoint: DirectionPoint,
                directionPoint1: DirectionPoint,
                list: List<DirectionPoint>,
                directionsResponse: DirectionsResponse,
                i: Int
            ) {
                Toast.makeText(
                    this@DirectionWidgetActivity,
                    "On Navigation Start",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}