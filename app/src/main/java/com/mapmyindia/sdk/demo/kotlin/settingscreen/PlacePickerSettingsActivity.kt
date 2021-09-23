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
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.mapmyindia.sdk.geojson.Point
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityPlacePickerSettingsBinding
import com.mapmyindia.sdk.demo.java.utils.InputFilterMinMax
import com.mapmyindia.sdk.demo.kotlin.settings.MapmyIndiaPlacePickerSetting
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mmi.services.api.autosuggest.AutoSuggestCriteria

class PlacePickerSettingsActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPlacePickerSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_place_picker_settings)
        mBinding.etLatitude.filters = arrayOf<InputFilter>(InputFilterMinMax(-90, 90))
        mBinding.etLongitude.filters = arrayOf<InputFilter>(InputFilterMinMax(-180, 180))
        initSettings()
        initCallback()
    }

    private fun initCallback() {
        mBinding.pickerToolbarColor.text = String.format("#%06X", 0xFFFFFF and MapmyIndiaPlacePickerSetting.instance.pickerToolbarColor)
        mBinding.includeDeviceLocation.isChecked = MapmyIndiaPlacePickerSetting.instance.isIncludeDeviceLocation
        mBinding.includeSearchButton.isChecked = MapmyIndiaPlacePickerSetting.instance.isIncludeSearch
        mBinding.tokenizeAddressBtn.isChecked = MapmyIndiaPlacePickerSetting.instance.isTokenizeAddress
        mBinding.cbDefault.setOnCheckedChangeListener { buttonView, isChecked ->
            MapmyIndiaPlacePickerSetting.instance.isDefault = isChecked
            if (isChecked) {
                mBinding.disableView.visibility = View.VISIBLE
            } else {
                mBinding.disableView.visibility = View.GONE
            }
        }
        mBinding.rgVertical.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == mBinding.rbTop.getId()) {
                MapmyIndiaPlacePickerSetting.instance.signatureVertical = PlaceOptions.GRAVITY_TOP
            } else {
                MapmyIndiaPlacePickerSetting.instance.signatureVertical = PlaceOptions.GRAVITY_BOTTOM
            }
        }
        mBinding.rgHorizontal.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_left -> MapmyIndiaPlacePickerSetting.instance.signatureHorizontal = PlaceOptions.GRAVITY_LEFT
                R.id.rb_center -> MapmyIndiaPlacePickerSetting.instance.signatureHorizontal = PlaceOptions.GRAVITY_CENTER
                R.id.rb_right -> MapmyIndiaPlacePickerSetting.instance.signatureHorizontal = PlaceOptions.GRAVITY_RIGHT
            }
        }
        mBinding.rgLogoSize.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_small -> MapmyIndiaPlacePickerSetting.instance.logoSize = PlaceOptions.SIZE_SMALL
                R.id.rb_medium -> MapmyIndiaPlacePickerSetting.instance.logoSize = PlaceOptions.SIZE_MEDIUM
                R.id.rb_large -> MapmyIndiaPlacePickerSetting.instance.logoSize = PlaceOptions.SIZE_LARGE
            }
        }
        mBinding.btnSaveLocation.setOnClickListener {
            if (!TextUtils.isEmpty(mBinding.etLatitude.text.toString().trim()) && !TextUtils.isEmpty(mBinding.etLongitude.getText().toString().trim())) {
                MapmyIndiaPlacePickerSetting.instance.location = Point.fromLngLat(mBinding.etLongitude.getText().toString().trim().toDouble(), mBinding.etLatitude.getText().toString().trim().toDouble())
            } else {
                MapmyIndiaPlacePickerSetting.instance.location = null
            }
            Toast.makeText(this@PlacePickerSettingsActivity, "Location save successfully", Toast.LENGTH_SHORT).show()
        }
        mBinding.btnSaveHistoryCount.setOnClickListener { v ->
            if (!TextUtils.isEmpty(mBinding.etHistoryCount.text.toString())) {
                MapmyIndiaPlacePickerSetting.instance.historyCount = mBinding.etHistoryCount.getText().toString().toInt()
            } else {
                MapmyIndiaPlacePickerSetting.instance.historyCount = null
            }
            Toast.makeText(this@PlacePickerSettingsActivity, "History Count save successfully", Toast.LENGTH_SHORT).show()
        }
        mBinding.btnSaveFilter.setOnClickListener(View.OnClickListener {
            if (!TextUtils.isEmpty(mBinding.etFilter.text.toString().trim())) {
                MapmyIndiaPlacePickerSetting.instance.filter = mBinding.etFilter.getText().toString().trim()
            } else {
                MapmyIndiaPlacePickerSetting.instance.filter = null
            }
            Toast.makeText(this@PlacePickerSettingsActivity, "Filter save successfully", Toast.LENGTH_SHORT).show()
        })
        mBinding.cbEnableHistory.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            MapmyIndiaPlacePickerSetting.instance.isEnableHistory = isChecked
            if (isChecked) {
                mBinding.historyCountLayout.visibility = View.VISIBLE
            } else {
                mBinding.historyCountLayout.visibility = View.GONE
            }
        })
        mBinding.btnPodClear.setOnClickListener(View.OnClickListener {
            mBinding.rgPod.clearCheck()
            MapmyIndiaPlacePickerSetting.instance.pod = null
        })
        mBinding.rgPod.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_city -> MapmyIndiaPlacePickerSetting.instance.pod = AutoSuggestCriteria.POD_CITY
                R.id.rb_state -> MapmyIndiaPlacePickerSetting.instance.pod = AutoSuggestCriteria.POD_STATE
                R.id.rb_sub_district -> MapmyIndiaPlacePickerSetting.instance.pod = AutoSuggestCriteria.POD_SUB_DISTRICT
                R.id.rb_district -> MapmyIndiaPlacePickerSetting.instance.pod = AutoSuggestCriteria.POD_DISTRICT
                R.id.rb_sub_locality -> MapmyIndiaPlacePickerSetting.instance.pod = AutoSuggestCriteria.POD_SUB_LOCALITY
                R.id.rb_sub_sub_locality -> MapmyIndiaPlacePickerSetting.instance.pod = AutoSuggestCriteria.POD_SUB_SUB_LOCALITY
                R.id.rb_locality -> MapmyIndiaPlacePickerSetting.instance.pod = AutoSuggestCriteria.POD_LOCALITY
                R.id.rb_village -> MapmyIndiaPlacePickerSetting.instance.pod = AutoSuggestCriteria.POD_VILLAGE
            }
        }
        mBinding.btnHint.setOnClickListener(View.OnClickListener {
            if (!TextUtils.isEmpty(mBinding.etHint.getText().toString().trim())) {
                MapmyIndiaPlacePickerSetting.instance.hint = mBinding.etHint.getText().toString().trim()
                Toast.makeText(this@PlacePickerSettingsActivity, "Hint save successfully", Toast.LENGTH_SHORT).show()
            } else {
                mBinding.etHint.setText(MapmyIndiaPlacePickerSetting.instance.hint)
                Toast.makeText(this@PlacePickerSettingsActivity, "Hint is mandatory", Toast.LENGTH_SHORT).show()
            }
        })
        mBinding.btnZoom.setOnClickListener { v ->
            if (!TextUtils.isEmpty(mBinding.etZoom.getText().toString().trim())) {
                MapmyIndiaPlacePickerSetting.instance.zoom = mBinding.etZoom.getText().toString().trim().toDouble()
                Toast.makeText(this@PlacePickerSettingsActivity, "zoom save successfully", Toast.LENGTH_SHORT).show()
            }
        }

        /* mBinding.cbEnableTextSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 MapmyIndiaPlacePickerSetting.getInstance().setEnableTextSearch(isChecked);
             }
         });*/mBinding.backgroundRG.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_white -> MapmyIndiaPlacePickerSetting.instance.backgroundColor = R.color.white
                R.id.rb_black -> MapmyIndiaPlacePickerSetting.instance.backgroundColor = android.R.color.black
                R.id.rb_red -> MapmyIndiaPlacePickerSetting.instance.backgroundColor = android.R.color.holo_red_light
                R.id.rb_green -> MapmyIndiaPlacePickerSetting.instance.backgroundColor = android.R.color.holo_green_dark
                R.id.rb_blue -> MapmyIndiaPlacePickerSetting.instance.backgroundColor = android.R.color.holo_blue_bright
            }
        })
        mBinding.toolbarRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_white_toolbar -> MapmyIndiaPlacePickerSetting.instance.toolbarColor = R.color.white
                R.id.rb_black_toolbar -> MapmyIndiaPlacePickerSetting.instance.toolbarColor = android.R.color.black
                R.id.rb_red_toolbar -> MapmyIndiaPlacePickerSetting.instance.toolbarColor =android.R.color.holo_red_light
                R.id.rb_green_toolbar -> MapmyIndiaPlacePickerSetting.instance.toolbarColor = android.R.color.holo_green_dark
                R.id.rb_blue_toolbar -> MapmyIndiaPlacePickerSetting.instance.toolbarColor = android.R.color.holo_blue_bright
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initSettings() {
        mBinding.toolbarColorLayout.setOnClickListener { v ->
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaPlacePickerSetting.instance.pickerToolbarColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok") { dialog, selectedColor, allColors -> // changeBackgroundColor(selectedColor);
                        MapmyIndiaPlacePickerSetting.instance.pickerToolbarColor = selectedColor
                        mBinding.pickerToolbarColor.text = String.format("#%06X", 0xFFFFFF and selectedColor)
                        dialog.dismiss()
                    }
                    .setNegativeButton("cancel") { dialog, which -> }
                    .build()
                    .show()
        }
        mBinding.includeDeviceLocation.setOnCheckedChangeListener { buttonView, isChecked -> MapmyIndiaPlacePickerSetting.instance.isIncludeDeviceLocation = isChecked }
        mBinding.includeSearchButton.setOnCheckedChangeListener { buttonView, isChecked -> MapmyIndiaPlacePickerSetting.instance.isIncludeSearch = isChecked }
        mBinding.tokenizeAddressBtn.setOnCheckedChangeListener { buttonView, isChecked -> MapmyIndiaPlacePickerSetting.instance.isTokenizeAddress = isChecked }
        mBinding.cbDefault.isChecked = MapmyIndiaPlacePickerSetting.instance.isDefault
        mBinding.disableView.visibility = if (MapmyIndiaPlacePickerSetting.instance.isDefault) View.VISIBLE else View.GONE
        if (MapmyIndiaPlacePickerSetting.instance.historyCount != null) {
            mBinding.etHistoryCount.setText(MapmyIndiaPlacePickerSetting.instance.historyCount.toString())
        }
        if (MapmyIndiaPlacePickerSetting.instance.signatureVertical == PlaceOptions.GRAVITY_TOP) {
            mBinding.rgVertical.check(mBinding.rbTop.id)
        } else {
            mBinding.rgVertical.check(mBinding.rbBottom.id)
        }
        if (MapmyIndiaPlacePickerSetting.instance.signatureHorizontal == PlaceOptions.GRAVITY_LEFT) {
            mBinding.rgHorizontal.check(mBinding.rbLeft.getId())
        } else if (MapmyIndiaPlacePickerSetting.instance.signatureHorizontal == PlaceOptions.GRAVITY_CENTER) {
            mBinding.rgHorizontal.check(mBinding.rbCenter.getId())
        } else {
            mBinding.rgHorizontal.check(mBinding.rbRight.getId())
        }
        if (MapmyIndiaPlacePickerSetting.instance.logoSize == PlaceOptions.SIZE_SMALL) {
            mBinding.rgLogoSize.check(R.id.rb_small)
        } else if (MapmyIndiaPlacePickerSetting.instance.logoSize == PlaceOptions.SIZE_MEDIUM) {
            mBinding.rgLogoSize.check(R.id.rb_medium)
        } else {
            mBinding.rgLogoSize.check(R.id.rb_large)
        }
        if (MapmyIndiaPlacePickerSetting.instance.location != null) {
            mBinding.etLatitude.setText(MapmyIndiaPlacePickerSetting.instance.location.latitude().toString())
            mBinding.etLongitude.setText(MapmyIndiaPlacePickerSetting.instance.location.longitude().toString())
        }
        if (MapmyIndiaPlacePickerSetting.instance.filter != null) {
            mBinding.etFilter.setText(MapmyIndiaPlacePickerSetting.instance.filter)
        }
        mBinding.cbEnableHistory.isChecked = MapmyIndiaPlacePickerSetting.instance.isEnableHistory
        if (!MapmyIndiaPlacePickerSetting.instance.isEnableHistory) {
            mBinding.historyCountLayout.visibility = View.GONE
        } else {
            mBinding.historyCountLayout.visibility = View.VISIBLE
        }
        if (MapmyIndiaPlacePickerSetting.instance.pod != null) {
            if (MapmyIndiaPlacePickerSetting.instance.pod.equals(AutoSuggestCriteria.POD_CITY, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbCity.getId())
            } else if (MapmyIndiaPlacePickerSetting.instance.pod.equals(AutoSuggestCriteria.POD_DISTRICT, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbDistrict.getId())
            } else if (MapmyIndiaPlacePickerSetting.instance.pod.equals(AutoSuggestCriteria.POD_LOCALITY, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbLocality.getId())
            } else if (MapmyIndiaPlacePickerSetting.instance.pod.equals(AutoSuggestCriteria.POD_STATE, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbState.getId())
            } else if (MapmyIndiaPlacePickerSetting.instance.pod.equals(AutoSuggestCriteria.POD_SUB_DISTRICT, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbSubDistrict.getId())
            } else if (MapmyIndiaPlacePickerSetting.instance.pod.equals(AutoSuggestCriteria.POD_SUB_LOCALITY, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbSubLocality.getId())
            } else if (MapmyIndiaPlacePickerSetting.instance.pod.equals(AutoSuggestCriteria.POD_SUB_SUB_LOCALITY, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbSubSubLocality.getId())
            } else if (MapmyIndiaPlacePickerSetting.instance.pod.equals(AutoSuggestCriteria.POD_VILLAGE, ignoreCase = true)) {
                mBinding.rgPod.check(mBinding.rbVillage.getId())
            }
        } else {
            mBinding.rgPod.clearCheck()
        }
        mBinding.etHint.setText(MapmyIndiaPlacePickerSetting.instance.hint)
        if (MapmyIndiaPlacePickerSetting.instance.zoom != null) {
            mBinding.etZoom.setText(MapmyIndiaPlacePickerSetting.instance.zoom.toString())
        }

        if (MapmyIndiaPlacePickerSetting.instance.backgroundColor == R.color.white) {
            mBinding.backgroundRG.check(mBinding.rbWhite.id)
        } else if (MapmyIndiaPlacePickerSetting.instance.backgroundColor == android.R.color.black) {
            mBinding.backgroundRG.check(mBinding.rbBlack.id)
        } else if (MapmyIndiaPlacePickerSetting.instance.backgroundColor == android.R.color.holo_red_light) {
            mBinding.backgroundRG.check(mBinding.rbRed.id)
        } else if (MapmyIndiaPlacePickerSetting.instance.backgroundColor == android.R.color.holo_green_dark) {
            mBinding.backgroundRG.check(mBinding.rbGreen.id)
        } else if (MapmyIndiaPlacePickerSetting.instance.backgroundColor == android.R.color.holo_blue_bright) {
            mBinding.backgroundRG.check(mBinding.rbBlue.id)
        }
        if (MapmyIndiaPlacePickerSetting.instance.toolbarColor == R.color.white) {
            mBinding.toolbarRG.check(mBinding.rbWhiteToolbar.id)
        } else if (MapmyIndiaPlacePickerSetting.instance.toolbarColor == android.R.color.black) {
            mBinding.toolbarRG.check(mBinding.rbBlackToolbar.id)
        } else if (MapmyIndiaPlacePickerSetting.instance.toolbarColor == android.R.color.holo_red_light) {
            mBinding.toolbarRG.check(mBinding.rbRedToolbar.id)
        } else if (MapmyIndiaPlacePickerSetting.instance.toolbarColor == android.R.color.holo_green_dark) {
            mBinding.toolbarRG.check(mBinding.rbGreenToolbar.id)
        } else if (MapmyIndiaPlacePickerSetting.instance.toolbarColor == android.R.color.holo_blue_bright) {
            mBinding.toolbarRG.check(mBinding.rbBlueToolbar.id)
        }
    }
}