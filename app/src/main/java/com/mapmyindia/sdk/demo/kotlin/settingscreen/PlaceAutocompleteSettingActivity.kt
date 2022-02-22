package com.mapmyindia.sdk.demo.kotlin.settingscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mapmyindia.sdk.geojson.Point
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityPlaceAutocompleteSettingBinding
import com.mapmyindia.sdk.demo.java.utils.InputFilterMinMax
import com.mapmyindia.sdk.demo.kotlin.settings.MapmyIndiaPlaceWidgetSetting
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mmi.services.api.autosuggest.AutoSuggestCriteria

class PlaceAutocompleteSettingActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPlaceAutocompleteSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_place_autocomplete_setting)
        mBinding.etLatitude.filters = arrayOf<InputFilter>(InputFilterMinMax(-90, 90))
        mBinding.etLongitude.filters = arrayOf<InputFilter>(InputFilterMinMax(-180, 180))
        initSettings()
        initCallback()
    }

    private fun initCallback() {
        mBinding.cbDefault.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            MapmyIndiaPlaceWidgetSetting.instance.isDefault = isChecked
            if (isChecked) {
                mBinding.disableView.visibility = View.VISIBLE
            } else {
                mBinding.disableView.visibility = View.GONE
            }
        })
        mBinding.rgVertical.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId == mBinding.rbTop.getId()) {
                MapmyIndiaPlaceWidgetSetting.instance.signatureVertical = PlaceOptions.GRAVITY_TOP
            } else {
                MapmyIndiaPlaceWidgetSetting.instance.signatureVertical = PlaceOptions.GRAVITY_BOTTOM
            }
        })
        mBinding.rgHorizontal.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_left -> MapmyIndiaPlaceWidgetSetting.instance.signatureHorizontal = PlaceOptions.GRAVITY_LEFT
                R.id.rb_center -> MapmyIndiaPlaceWidgetSetting.instance.signatureHorizontal = PlaceOptions.GRAVITY_CENTER
                R.id.rb_right -> MapmyIndiaPlaceWidgetSetting.instance.signatureHorizontal = PlaceOptions.GRAVITY_RIGHT
            }
        })
        mBinding.rgLogoSize.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_small -> MapmyIndiaPlaceWidgetSetting.instance.logoSize = PlaceOptions.SIZE_SMALL
                R.id.rb_medium -> MapmyIndiaPlaceWidgetSetting.instance.logoSize = PlaceOptions.SIZE_MEDIUM
                R.id.rb_large -> MapmyIndiaPlaceWidgetSetting.instance.logoSize = PlaceOptions.SIZE_LARGE
            }
        })
        mBinding.btnSaveLocation.setOnClickListener(View.OnClickListener {
            if (!TextUtils.isEmpty(mBinding.etLatitude.getText().toString().trim()) && !TextUtils.isEmpty(mBinding.etLongitude.getText().toString().trim())) {
                MapmyIndiaPlaceWidgetSetting.instance.location = Point.fromLngLat(mBinding.etLongitude.getText().toString().trim().toDouble(), mBinding.etLatitude.getText().toString().trim().toDouble())
            } else {
                MapmyIndiaPlaceWidgetSetting.instance.location = null
            }
            Toast.makeText(this@PlaceAutocompleteSettingActivity, "Location save successfully", Toast.LENGTH_SHORT).show()
        })
        mBinding.btnSaveFilter.setOnClickListener(View.OnClickListener {
            if (!TextUtils.isEmpty(mBinding.etFilter.getText().toString().trim())) {
                MapmyIndiaPlaceWidgetSetting.instance.filter = mBinding.etFilter.getText().toString().trim()
            } else {
                MapmyIndiaPlaceWidgetSetting.instance.filter = null
            }
            Toast.makeText(this@PlaceAutocompleteSettingActivity, "Filter save successfully", Toast.LENGTH_SHORT).show()
        })
        mBinding.cbEnableHistory.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> MapmyIndiaPlaceWidgetSetting.instance.isEnableHistory = isChecked })
        mBinding.btnPodClear.setOnClickListener(View.OnClickListener {
            mBinding.rgPod.clearCheck()
            MapmyIndiaPlaceWidgetSetting.instance.pod = null
        })
        mBinding.rgPod.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_city -> MapmyIndiaPlaceWidgetSetting.instance.pod = AutoSuggestCriteria.POD_CITY
                R.id.rb_state -> MapmyIndiaPlaceWidgetSetting.instance.pod = AutoSuggestCriteria.POD_STATE
                R.id.rb_sub_district -> MapmyIndiaPlaceWidgetSetting.instance.pod = AutoSuggestCriteria.POD_SUB_DISTRICT
                R.id.rb_district -> MapmyIndiaPlaceWidgetSetting.instance.pod = AutoSuggestCriteria.POD_DISTRICT
                R.id.rb_sub_locality -> MapmyIndiaPlaceWidgetSetting.instance.pod = AutoSuggestCriteria.POD_SUB_LOCALITY
                R.id.rb_sub_sub_locality -> MapmyIndiaPlaceWidgetSetting.instance.pod = AutoSuggestCriteria.POD_SUB_SUB_LOCALITY
                R.id.rb_locality -> MapmyIndiaPlaceWidgetSetting.instance.pod = AutoSuggestCriteria.POD_LOCALITY
                R.id.rb_village -> MapmyIndiaPlaceWidgetSetting.instance.pod = AutoSuggestCriteria.POD_VILLAGE
                R.id.rb_poi -> MapmyIndiaPlaceWidgetSetting.instance.pod = AutoSuggestCriteria.POD_POI
            }
        })
        mBinding.btnHint.setOnClickListener(View.OnClickListener {
            if (!TextUtils.isEmpty(mBinding.etHint.getText().toString().trim())) {
                MapmyIndiaPlaceWidgetSetting.instance.hint = mBinding.etHint.getText().toString().trim()
                Toast.makeText(this@PlaceAutocompleteSettingActivity, "Hint save successfully", Toast.LENGTH_SHORT).show()
            } else {
                mBinding.etHint.setText(MapmyIndiaPlaceWidgetSetting.instance.hint)
                Toast.makeText(this@PlaceAutocompleteSettingActivity, "Hint is mandatory", Toast.LENGTH_SHORT).show()
            }
        })
        mBinding.cbEnableTextSearch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> MapmyIndiaPlaceWidgetSetting.instance.isEnableTextSearch = isChecked })

        mBinding.backgroundRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_white -> MapmyIndiaPlaceWidgetSetting.instance.backgroundColor = android.R.color.white
                R.id.rb_black -> MapmyIndiaPlaceWidgetSetting.instance.backgroundColor = android.R.color.black
                R.id.rb_red -> MapmyIndiaPlaceWidgetSetting.instance.backgroundColor = android.R.color.holo_red_light
                R.id.rb_green -> MapmyIndiaPlaceWidgetSetting.instance.backgroundColor = android.R.color.holo_green_dark
                R.id.rb_blue -> MapmyIndiaPlaceWidgetSetting.instance.backgroundColor = android.R.color.holo_blue_bright
            }
        }
        mBinding.toolbarRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_white_toolbar -> MapmyIndiaPlaceWidgetSetting.instance.toolbarColor = android.R.color.white
                R.id.rb_black_toolbar -> MapmyIndiaPlaceWidgetSetting.instance.toolbarColor = android.R.color.black
                R.id.rb_red_toolbar -> MapmyIndiaPlaceWidgetSetting.instance.toolbarColor = android.R.color.holo_red_light
                R.id.rb_green_toolbar -> MapmyIndiaPlaceWidgetSetting.instance.toolbarColor = android.R.color.holo_green_dark
                R.id.rb_blue_toolbar -> MapmyIndiaPlaceWidgetSetting.instance.toolbarColor = android.R.color.holo_blue_bright
            }
        }
        mBinding.cbEnableBridge.setOnCheckedChangeListener { _, isChecked ->
            MapmyIndiaPlaceWidgetSetting.instance.isBridgeEnable = isChecked
        }
        mBinding.cbEnableHyperlocal.setOnCheckedChangeListener { _, isChecked ->
            MapmyIndiaPlaceWidgetSetting.instance.isHyperLocalEnable = isChecked
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initSettings() {
        mBinding.cbDefault.isChecked =  MapmyIndiaPlaceWidgetSetting.instance.isDefault
        mBinding.disableView.visibility = if ( MapmyIndiaPlaceWidgetSetting.instance.isDefault) View.VISIBLE else View.GONE
        if ( MapmyIndiaPlaceWidgetSetting.instance.signatureVertical == PlaceOptions.GRAVITY_TOP) {
            mBinding.rgVertical.check(mBinding.rbTop.id)
        } else {
            mBinding.rgVertical.check(mBinding.rbBottom.id)
        }
        if ( MapmyIndiaPlaceWidgetSetting.instance.signatureHorizontal == PlaceOptions.GRAVITY_LEFT) {
            mBinding.rgHorizontal.check(mBinding.rbLeft.id)
        } else if ( MapmyIndiaPlaceWidgetSetting.instance.signatureHorizontal == PlaceOptions.GRAVITY_CENTER) {
            mBinding.rgHorizontal.check(mBinding.rbCenter.id)
        } else {
            mBinding.rgHorizontal.check(mBinding.rbRight.id)
        }
        if (MapmyIndiaPlaceWidgetSetting.instance.logoSize == PlaceOptions.SIZE_SMALL) {
            mBinding.rgLogoSize.check(R.id.rb_small)
        } else if (MapmyIndiaPlaceWidgetSetting.instance.logoSize == PlaceOptions.SIZE_MEDIUM) {
            mBinding.rgLogoSize.check(R.id.rb_medium)
        } else {
            mBinding.rgLogoSize.check(R.id.rb_large)
        }
        if ( MapmyIndiaPlaceWidgetSetting.instance.location != null) {
            mBinding.etLatitude.setText(MapmyIndiaPlaceWidgetSetting.instance.location?.latitude().toString())
            mBinding.etLongitude.setText(MapmyIndiaPlaceWidgetSetting.instance.location?.longitude().toString())
        }
        if ( MapmyIndiaPlaceWidgetSetting.instance.filter != null) {
            mBinding.etFilter.setText(MapmyIndiaPlaceWidgetSetting.instance.filter)
        }
        mBinding.cbEnableHistory.isChecked =  MapmyIndiaPlaceWidgetSetting.instance.isEnableHistory
        if ( MapmyIndiaPlaceWidgetSetting.instance.pod != null) {
            if ( MapmyIndiaPlaceWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_CITY, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbCity.getId())
            } else if ( MapmyIndiaPlaceWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_DISTRICT, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbDistrict.getId())
            } else if ( MapmyIndiaPlaceWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_LOCALITY, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbLocality.getId())
            } else if ( MapmyIndiaPlaceWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_STATE, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbState.getId())
            } else if ( MapmyIndiaPlaceWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_SUB_DISTRICT, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbSubDistrict.getId())
            } else if ( MapmyIndiaPlaceWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_SUB_LOCALITY, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbSubLocality.getId())
            } else if ( MapmyIndiaPlaceWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_SUB_SUB_LOCALITY, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbSubSubLocality.getId())
            } else if ( MapmyIndiaPlaceWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_VILLAGE, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbVillage.getId())
            } else if(MapmyIndiaPlaceWidgetSetting.instance.pod.equals(AutoSuggestCriteria.POD_POI)) {
                mBinding.rgPod.check(mBinding.rbPoi.id)
            }
        } else {
            mBinding.rgPod.clearCheck()
        }
        mBinding.etHint.setText(MapmyIndiaPlaceWidgetSetting.instance.hint)
        mBinding.cbEnableTextSearch.isChecked = MapmyIndiaPlaceWidgetSetting.instance.isEnableTextSearch

        when (MapmyIndiaPlaceWidgetSetting.instance.backgroundColor) {
            android.R.color.white -> {
                mBinding.backgroundRG.check(mBinding.rbWhite.id)
            }
            android.R.color.black -> {
                mBinding.backgroundRG.check(mBinding.rbBlack.id)
            }
            android.R.color.holo_red_light -> {
                mBinding.backgroundRG.check(mBinding.rbRed.id)
            }
            android.R.color.holo_green_dark -> {
                mBinding.backgroundRG.check(mBinding.rbGreen.id)
            }
            android.R.color.holo_blue_bright -> {
                mBinding.backgroundRG.check(mBinding.rbBlue.id)
            }
        }


        when (MapmyIndiaPlaceWidgetSetting.instance.toolbarColor) {
            android.R.color.white -> {
                mBinding.toolbarRG.check(mBinding.rbWhiteToolbar.id)
            }
            android.R.color.black -> {
                mBinding.toolbarRG.check(mBinding.rbBlackToolbar.id)
            }
            android.R.color.holo_red_light -> {
                mBinding.toolbarRG.check(mBinding.rbRedToolbar.id)
            }

            android.R.color.holo_green_dark -> {
                mBinding.toolbarRG.check(mBinding.rbGreenToolbar.id)
            }
            android.R.color.holo_blue_bright -> {
                mBinding.toolbarRG.check(mBinding.rbBlueToolbar.id)
            }
        }
        mBinding.cbEnableBridge.isChecked = MapmyIndiaPlaceWidgetSetting.instance.isBridgeEnable
        mBinding.cbEnableHyperlocal.isChecked = MapmyIndiaPlaceWidgetSetting.instance.isHyperLocalEnable

    }
}