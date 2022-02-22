package com.mapmyindia.sdk.demo.kotlin.settingscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.geojson.Point
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityDirectionWidgetSettingBinding
import com.mapmyindia.sdk.demo.java.utils.InputFilterMinMax
import com.mapmyindia.sdk.demo.kotlin.settings.MapmyIndiaDirectionWidgetSetting
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mmi.services.api.autosuggest.AutoSuggestCriteria
import com.mmi.services.api.directions.DirectionsCriteria
import java.util.*

class DirectionWidgetSettingActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDirectionWidgetSettingBinding
    private var excludes: MutableList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_direction_widget_setting)
        mBinding.etLatitude.filters = arrayOf<InputFilter>(InputFilterMinMax(-90, 90))
        mBinding.etLongitude.filters = arrayOf<InputFilter>(InputFilterMinMax(-180, 180))
        initSettings()
        initCallback()
    }

    private fun initCallback() {
        if (MapmyIndiaDirectionWidgetSetting.instance.excludes == null) {
            excludes = ArrayList()
        } else {
            excludes = MapmyIndiaDirectionWidgetSetting.instance.excludes
            for (item in excludes!!) {
                when {
                    item.equals(DirectionsCriteria.EXCLUDE_FERRY, ignoreCase = true) -> {
                        mBinding.cbFerry.isChecked = true
                    }
                    item.equals(DirectionsCriteria.EXCLUDE_MOTORWAY, ignoreCase = true) -> {
                        mBinding.cbMotorway.isChecked = true
                    }
                    item.equals(DirectionsCriteria.EXCLUDE_TOLL, ignoreCase = true) -> {
                        mBinding.cbToll.isChecked = true
                    }
                }
            }
        }
        mBinding.showAlternativeBtn.isChecked = MapmyIndiaDirectionWidgetSetting.instance.isShowAlternative
        mBinding.showStepsBtn.isChecked = MapmyIndiaDirectionWidgetSetting.instance.isSteps
        mBinding.showStartNavigationBtn.isChecked = MapmyIndiaDirectionWidgetSetting.instance.isShowStartNavigation
        mBinding.tokenizeAddressBtn.isChecked = MapmyIndiaDirectionWidgetSetting.instance.isTokenizeAddress
        mBinding.cbDefault.setOnCheckedChangeListener { buttonView, isChecked ->
            MapmyIndiaDirectionWidgetSetting.instance.isDefault = isChecked
            if (isChecked) {
                mBinding.disableView.visibility = View.VISIBLE
            } else {
                mBinding.disableView.visibility = View.GONE
            }
        }
        mBinding.cbFerry.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                excludes!!.add(DirectionsCriteria.EXCLUDE_FERRY)
            } else {
                excludes!!.remove(DirectionsCriteria.EXCLUDE_FERRY)
            }
            MapmyIndiaDirectionWidgetSetting.instance.excludes = excludes
        }
        mBinding.cbToll.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                excludes!!.add(DirectionsCriteria.EXCLUDE_TOLL)
            } else {
                excludes!!.remove(DirectionsCriteria.EXCLUDE_TOLL)
            }
            MapmyIndiaDirectionWidgetSetting.instance.excludes = excludes
        }
        mBinding.cbMotorway.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                excludes!!.add(DirectionsCriteria.EXCLUDE_MOTORWAY)
            } else {
                excludes!!.remove(DirectionsCriteria.EXCLUDE_MOTORWAY)
            }
            MapmyIndiaDirectionWidgetSetting.instance.excludes = excludes
        }
        mBinding.rgVertical.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == mBinding.rbTop.id) {
                MapmyIndiaDirectionWidgetSetting.instance.signatureVertical = PlaceOptions.GRAVITY_TOP
            } else {
                MapmyIndiaDirectionWidgetSetting.instance.signatureVertical = PlaceOptions.GRAVITY_BOTTOM
            }
        }
        mBinding.rgHorizontal.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_left -> MapmyIndiaDirectionWidgetSetting.instance.signatureHorizontal = PlaceOptions.GRAVITY_LEFT
                R.id.rb_center -> MapmyIndiaDirectionWidgetSetting.instance.signatureHorizontal = PlaceOptions.GRAVITY_CENTER
                R.id.rb_right -> MapmyIndiaDirectionWidgetSetting.instance.signatureHorizontal = PlaceOptions.GRAVITY_RIGHT
            }
        }
        mBinding.rgLogoSize.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_small ->MapmyIndiaDirectionWidgetSetting.instance.logoSize = PlaceOptions.SIZE_SMALL
                R.id.rb_medium ->MapmyIndiaDirectionWidgetSetting.instance.logoSize = PlaceOptions.SIZE_MEDIUM
                R.id.rb_large -> MapmyIndiaDirectionWidgetSetting.instance.logoSize = PlaceOptions.SIZE_LARGE
            }
        }
        mBinding.btnSaveLocation.setOnClickListener { v ->
            if (!TextUtils.isEmpty(mBinding.etLatitude.text.toString().trim()) && !TextUtils.isEmpty(mBinding.etLongitude.getText().toString().trim())) {
                MapmyIndiaDirectionWidgetSetting.instance.location = Point.fromLngLat(mBinding.etLongitude.getText().toString().trim().toDouble(), mBinding.etLatitude.getText().toString().trim().toDouble())
            } else {
                MapmyIndiaDirectionWidgetSetting.instance.location = null
            }
            Toast.makeText(this@DirectionWidgetSettingActivity, "Location save successfully", Toast.LENGTH_SHORT).show()
        }
        mBinding.btnSaveHistoryCount.setOnClickListener { v ->
            if (!TextUtils.isEmpty(mBinding.etHistoryCount.getText().toString())) {
                MapmyIndiaDirectionWidgetSetting.instance.historyCount = mBinding.etHistoryCount.getText().toString().toInt()
            } else {
                MapmyIndiaDirectionWidgetSetting.instance.historyCount = null
            }
            Toast.makeText(this@DirectionWidgetSettingActivity, "History Count save successfully", Toast.LENGTH_SHORT).show()
        }
        mBinding.btnSaveFilter.setOnClickListener { v ->
            if (!TextUtils.isEmpty(mBinding.etFilter.text.toString().trim())) {
                MapmyIndiaDirectionWidgetSetting.instance.filter = mBinding.etFilter.getText().toString().trim()
            } else {
                MapmyIndiaDirectionWidgetSetting.instance.filter = null
            }
            Toast.makeText(this@DirectionWidgetSettingActivity, "Filter save successfully", Toast.LENGTH_SHORT).show()
        }
        mBinding.cbEnableHistory.setOnCheckedChangeListener { buttonView, isChecked ->
            MapmyIndiaDirectionWidgetSetting.instance.isEnableHistory = isChecked
            if (isChecked) {
                mBinding.historyCountLayout.visibility = View.VISIBLE
            } else {
                mBinding.historyCountLayout.visibility = View.GONE
            }
        }
        mBinding.btnPodClear.setOnClickListener { v ->
            mBinding.rgPod.clearCheck()
            MapmyIndiaDirectionWidgetSetting.instance.pod = null
        }
        mBinding.rgResources.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_route ->MapmyIndiaDirectionWidgetSetting.instance.resource = DirectionsCriteria.RESOURCE_ROUTE
                R.id.rb_route_eta -> MapmyIndiaDirectionWidgetSetting.instance.resource = DirectionsCriteria.RESOURCE_ROUTE_ETA
                R.id.rb_route_traffic -> MapmyIndiaDirectionWidgetSetting.instance.resource = DirectionsCriteria.RESOURCE_ROUTE_TRAFFIC
            }
        }
        mBinding.rgProfiles.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_driving -> MapmyIndiaDirectionWidgetSetting.instance.profile = DirectionsCriteria.PROFILE_DRIVING
                R.id.rb_walking -> MapmyIndiaDirectionWidgetSetting.instance.profile = DirectionsCriteria.PROFILE_WALKING
                R.id.rb_biking ->MapmyIndiaDirectionWidgetSetting.instance.profile = DirectionsCriteria.PROFILE_BIKING
                R.id.rb_trucking ->MapmyIndiaDirectionWidgetSetting.instance.profile = DirectionsCriteria.PROFILE_TRUCKING
            }
        }
        mBinding.rgOverviews.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_full -> MapmyIndiaDirectionWidgetSetting.instance.overview = DirectionsCriteria.OVERVIEW_FULL
                R.id.rb_none -> MapmyIndiaDirectionWidgetSetting.instance.overview = DirectionsCriteria.OVERVIEW_FALSE
                R.id.rb_simplified -> MapmyIndiaDirectionWidgetSetting.instance.overview = DirectionsCriteria.OVERVIEW_SIMPLIFIED
            }
        }
        mBinding.rgPod.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_city -> MapmyIndiaDirectionWidgetSetting.instance.pod = AutoSuggestCriteria.POD_CITY
                R.id.rb_state -> MapmyIndiaDirectionWidgetSetting.instance.pod = AutoSuggestCriteria.POD_STATE
                R.id.rb_sub_district ->MapmyIndiaDirectionWidgetSetting.instance.pod = AutoSuggestCriteria.POD_SUB_DISTRICT
                R.id.rb_district -> MapmyIndiaDirectionWidgetSetting.instance.pod = AutoSuggestCriteria.POD_DISTRICT
                R.id.rb_sub_locality -> MapmyIndiaDirectionWidgetSetting.instance.pod = AutoSuggestCriteria.POD_SUB_LOCALITY
                R.id.rb_sub_sub_locality -> MapmyIndiaDirectionWidgetSetting.instance.pod = AutoSuggestCriteria.POD_SUB_SUB_LOCALITY
                R.id.rb_locality -> MapmyIndiaDirectionWidgetSetting.instance.pod = AutoSuggestCriteria.POD_LOCALITY
                R.id.rb_village ->MapmyIndiaDirectionWidgetSetting.instance.pod = AutoSuggestCriteria.POD_VILLAGE
            }
        }
        mBinding.btnHint.setOnClickListener { v ->
            if (!TextUtils.isEmpty(mBinding.etHint.getText().toString().trim())) {
                MapmyIndiaDirectionWidgetSetting.instance.hint = mBinding.etHint.getText().toString().trim()
                Toast.makeText(this@DirectionWidgetSettingActivity, "Hint save successfully", Toast.LENGTH_SHORT).show()
            } else {
                mBinding.etHint.setText(MapmyIndiaDirectionWidgetSetting.instance.hint)
                Toast.makeText(this@DirectionWidgetSettingActivity, "Hint is mandatory", Toast.LENGTH_SHORT).show()
            }
        }
        mBinding.btnZoom.setOnClickListener { v ->
            if (!TextUtils.isEmpty(mBinding.etZoom.getText().toString().trim())) {
                MapmyIndiaDirectionWidgetSetting.instance.zoom = mBinding.etZoom.getText().toString().trim().toDouble()
                Toast.makeText(this@DirectionWidgetSettingActivity, "zoom save successfully", Toast.LENGTH_SHORT).show()
            }
        }
        mBinding.backgroundRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_white -> MapmyIndiaDirectionWidgetSetting.instance.backgroundColor = R.color.white
                R.id.rb_black -> MapmyIndiaDirectionWidgetSetting.instance.backgroundColor = android.R.color.black
                R.id.rb_red -> MapmyIndiaDirectionWidgetSetting.instance.backgroundColor = android.R.color.holo_red_light
                R.id.rb_green -> MapmyIndiaDirectionWidgetSetting.instance.backgroundColor = android.R.color.holo_green_dark
                R.id.rb_blue -> MapmyIndiaDirectionWidgetSetting.instance.backgroundColor = android.R.color.holo_blue_bright
            }
        }
        mBinding.toolbarRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_white_toolbar -> MapmyIndiaDirectionWidgetSetting.instance.toolbarColor = R.color.white
                R.id.rb_black_toolbar -> MapmyIndiaDirectionWidgetSetting.instance.toolbarColor = android.R.color.black
                R.id.rb_red_toolbar -> MapmyIndiaDirectionWidgetSetting.instance.toolbarColor = android.R.color.holo_red_light
                R.id.rb_green_toolbar -> MapmyIndiaDirectionWidgetSetting.instance.toolbarColor = android.R.color.holo_green_dark
                R.id.rb_blue_toolbar -> MapmyIndiaDirectionWidgetSetting.instance.toolbarColor = android.R.color.holo_blue_bright
            }
        }
        mBinding.cbPoiSearch.setOnCheckedChangeListener { buttonView, isChecked ->
            MapmyIndiaDirectionWidgetSetting.instance.isShowPOISearch = isChecked
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initSettings() {
        mBinding.tokenizeAddressBtn.setOnCheckedChangeListener { buttonView, isChecked -> MapmyIndiaDirectionWidgetSetting.instance.isTokenizeAddress = isChecked }
        mBinding.showStartNavigationBtn.setOnCheckedChangeListener { buttonView, isChecked -> MapmyIndiaDirectionWidgetSetting.instance.isShowStartNavigation = isChecked }
        mBinding.showStepsBtn.setOnCheckedChangeListener { buttonView, isChecked -> MapmyIndiaDirectionWidgetSetting.instance.isSteps = isChecked }
        mBinding.showAlternativeBtn.setOnCheckedChangeListener { buttonView, isChecked -> MapmyIndiaDirectionWidgetSetting.instance.isShowAlternative = isChecked }
        mBinding.cbDefault.isChecked = MapmyIndiaDirectionWidgetSetting.instance.isDefault
        mBinding.disableView.visibility = if (MapmyIndiaDirectionWidgetSetting.instance.isDefault) View.VISIBLE else View.GONE
        if (MapmyIndiaDirectionWidgetSetting.instance.historyCount != null) {
            mBinding.etHistoryCount.setText(MapmyIndiaDirectionWidgetSetting.instance.historyCount.toString())
        }
        if (MapmyIndiaDirectionWidgetSetting.instance.signatureVertical == PlaceOptions.GRAVITY_TOP) {
            mBinding.rgVertical.check(mBinding.rbTop.id)
        } else {
            mBinding.rgVertical.check(mBinding.rbBottom.id)
        }
        if (MapmyIndiaDirectionWidgetSetting.instance.signatureHorizontal == PlaceOptions.GRAVITY_LEFT) {
            mBinding.rgHorizontal.check(mBinding.rbLeft.id)
        } else if (MapmyIndiaDirectionWidgetSetting.instance.signatureHorizontal == PlaceOptions.GRAVITY_CENTER) {
            mBinding.rgHorizontal.check(mBinding.rbCenter.id)
        } else {
            mBinding.rgHorizontal.check(mBinding.rbRight.id)
        }
        if (MapmyIndiaDirectionWidgetSetting.instance.logoSize == PlaceOptions.SIZE_SMALL) {
            mBinding.rgLogoSize.check(R.id.rb_small)
        } else if (MapmyIndiaDirectionWidgetSetting.instance.logoSize == PlaceOptions.SIZE_MEDIUM) {
            mBinding.rgLogoSize.check(R.id.rb_medium)
        } else {
            mBinding.rgLogoSize.check(R.id.rb_large)
        }
        if (MapmyIndiaDirectionWidgetSetting.instance.location != null) {
            mBinding.etLatitude.setText(MapmyIndiaDirectionWidgetSetting.instance.location?.latitude().toString())
            mBinding.etLongitude.setText(MapmyIndiaDirectionWidgetSetting.instance.location?.longitude().toString())
        }
        if (MapmyIndiaDirectionWidgetSetting.instance.filter != null) {
            mBinding.etFilter.setText(MapmyIndiaDirectionWidgetSetting.instance.filter)
        }
        mBinding.cbEnableHistory.isChecked = MapmyIndiaDirectionWidgetSetting.instance.isEnableHistory
        if (!MapmyIndiaDirectionWidgetSetting.instance.isEnableHistory) {
            mBinding.historyCountLayout.visibility = View.GONE
        } else {
            mBinding.historyCountLayout.visibility = View.VISIBLE
        }
        if (MapmyIndiaDirectionWidgetSetting.instance.resource != null) {
            if (MapmyIndiaDirectionWidgetSetting.instance.resource.equals(DirectionsCriteria.RESOURCE_ROUTE, ignoreCase = true)) {
                mBinding.rgResources.check(mBinding.rbRoute.id)
            } else if (MapmyIndiaDirectionWidgetSetting.instance.resource.equals(DirectionsCriteria.RESOURCE_ROUTE_ETA, ignoreCase = true)) {
                mBinding.rgResources.check(mBinding.rbRouteEta.id)
            } else if (MapmyIndiaDirectionWidgetSetting.instance.resource.equals(DirectionsCriteria.RESOURCE_ROUTE_TRAFFIC, ignoreCase = true)) {
                mBinding.rgResources.check(mBinding.rbRouteTraffic.id)
            }
        }
        if (MapmyIndiaDirectionWidgetSetting.instance.profile != null) {
            if (MapmyIndiaDirectionWidgetSetting.instance.profile.equals(DirectionsCriteria.PROFILE_DRIVING, ignoreCase = true)) {
                mBinding.rgProfiles.check(mBinding.rbDriving.id)
            } else if (MapmyIndiaDirectionWidgetSetting.instance.profile.equals(DirectionsCriteria.PROFILE_WALKING, ignoreCase = true)) {
                mBinding.rgProfiles.check(mBinding.rbWalking.id)
            } else if (MapmyIndiaDirectionWidgetSetting.instance.profile.equals(DirectionsCriteria.PROFILE_BIKING, ignoreCase = true)) {
                mBinding.rgProfiles.check(mBinding.rbBiking.id)
            } else if (MapmyIndiaDirectionWidgetSetting.instance.profile.equals(DirectionsCriteria.PROFILE_TRUCKING, ignoreCase = true)) {
                mBinding.rgProfiles.check(mBinding.rbTrucking.id)
            }
        }
        if (MapmyIndiaDirectionWidgetSetting.instance.overview != null) {
            if (MapmyIndiaDirectionWidgetSetting.instance.overview.equals(DirectionsCriteria.OVERVIEW_FULL, ignoreCase = true)) {
                mBinding.rgOverviews.check(mBinding.rbFull.id)
            } else if (MapmyIndiaDirectionWidgetSetting.instance.overview.equals(DirectionsCriteria.OVERVIEW_FALSE, ignoreCase = true)) {
                mBinding.rgOverviews.check(mBinding.rbNone.id)
            } else if (MapmyIndiaDirectionWidgetSetting.instance.overview.equals(DirectionsCriteria.OVERVIEW_SIMPLIFIED, ignoreCase = true)) {
                mBinding.rgOverviews.check(mBinding.rbSimplified.id)
            }
        }
        if (MapmyIndiaDirectionWidgetSetting.instance.pod != null) {
            if (MapmyIndiaDirectionWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_CITY, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbCity.getId())
            } else if (MapmyIndiaDirectionWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_DISTRICT, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbDistrict.getId())
            } else if (MapmyIndiaDirectionWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_LOCALITY, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbLocality.getId())
            } else if (MapmyIndiaDirectionWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_STATE, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbState.getId())
            } else if (MapmyIndiaDirectionWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_SUB_DISTRICT, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbSubDistrict.getId())
            } else if (MapmyIndiaDirectionWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_SUB_LOCALITY, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbSubLocality.getId())
            } else if (MapmyIndiaDirectionWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_SUB_SUB_LOCALITY, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbSubSubLocality.getId())
            } else if (MapmyIndiaDirectionWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_VILLAGE, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbVillage.getId())
            }
        } else {
            mBinding.rgPod.clearCheck()
        }
        mBinding.etHint.setText(MapmyIndiaDirectionWidgetSetting.instance.hint)
        if (MapmyIndiaDirectionWidgetSetting.instance.zoom != null) {
            mBinding.etZoom.setText(MapmyIndiaDirectionWidgetSetting.instance.zoom.toString())
        }
        // mBinding.cbEnableTextSearch.setChecked(MapmyIndiaPlacePickerSetting.getInstance().isEnableTextSearch());
        if (MapmyIndiaDirectionWidgetSetting.instance.backgroundColor == R.color.white) {
            mBinding.backgroundRG.check(mBinding.rbWhite.getId())
        } else if (MapmyIndiaDirectionWidgetSetting.instance.backgroundColor == android.R.color.black) {
            mBinding.backgroundRG.check(mBinding.rbBlack.getId())
        } else if (MapmyIndiaDirectionWidgetSetting.instance.backgroundColor == android.R.color.holo_red_light) {
            mBinding.backgroundRG.check(mBinding.rbRed.getId())
        } else if (MapmyIndiaDirectionWidgetSetting.instance.backgroundColor == android.R.color.holo_green_dark) {
            mBinding.backgroundRG.check(mBinding.rbGreen.getId())
        } else if (MapmyIndiaDirectionWidgetSetting.instance.backgroundColor == android.R.color.holo_blue_bright) {
            mBinding.backgroundRG.check(mBinding.rbBlue.getId())
        }
        if (MapmyIndiaDirectionWidgetSetting.instance.toolbarColor == R.color.white) {
            mBinding.toolbarRG.check(mBinding.rbWhiteToolbar.getId())
        } else if (MapmyIndiaDirectionWidgetSetting.instance.toolbarColor == android.R.color.black) {
            mBinding.toolbarRG.check(mBinding.rbBlackToolbar.getId())
        } else if (MapmyIndiaDirectionWidgetSetting.instance.toolbarColor == android.R.color.holo_red_light) {
            mBinding.toolbarRG.check(mBinding.rbRedToolbar.getId())
        } else if (MapmyIndiaDirectionWidgetSetting.instance.toolbarColor == android.R.color.holo_green_dark) {
            mBinding.toolbarRG.check(mBinding.rbGreenToolbar.getId())
        } else if (MapmyIndiaDirectionWidgetSetting.instance.toolbarColor == android.R.color.holo_blue_bright) {
            mBinding.toolbarRG.check(mBinding.rbBlueToolbar.getId())
        }
        mBinding.cbPoiSearch.isChecked = MapmyIndiaDirectionWidgetSetting.instance.isShowPOISearch
    }
}