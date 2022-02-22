package com.mapmyindia.sdk.demo.kotlin.settingscreen

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityDrivingRangeSettingBinding
import com.mapmyindia.sdk.demo.java.utils.InputFilterMinMax
import com.mapmyindia.sdk.demo.kotlin.settings.MapmyIndiaDrivingRangeSetting
import com.mapmyindia.sdk.drivingrange.DrivingRangeCriteria
import com.mapmyindia.sdk.geojson.Point
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import com.mmi.services.api.autosuggest.model.ELocation
import java.util.*

class DrivingRangeSettingActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDrivingRangeSettingBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_driving_range_setting)
        mBinding.etLatitude.filters = arrayOf<InputFilter>(InputFilterMinMax(-90, 90))
        mBinding.etLongitude.filters = arrayOf<InputFilter>(InputFilterMinMax(-180, 180))
        mBinding.etContourValue.filters = arrayOf(InputFilterMinMax(1, 500))
        mBinding.etDenoise.filters = arrayOf(InputFilterMinMax(0, 1))
        mBinding.datePicker.minDate = System.currentTimeMillis()
        initialiseView()
        initCallback()
    }

    private fun initCallback() {
        mBinding.cbCurrentLocation.setOnCheckedChangeListener { _, isChecked ->
            MapmyIndiaDrivingRangeSetting.instance.isUsingCurrentLocation = isChecked
        }
        mBinding.btnSaveLocation.setOnClickListener {
            if (!TextUtils.isEmpty(
                    mBinding.etLatitude.text.toString().trim()
                ) && !TextUtils.isEmpty(mBinding.etLongitude.text.toString().trim())
            ) {
                MapmyIndiaDrivingRangeSetting.instance.location = Point.fromLngLat(
                    mBinding.etLongitude.text.toString().trim().toDouble(),
                    mBinding.etLatitude.text.toString().trim().toDouble()
                )
            }
        }
        mBinding.btnSearch.setOnClickListener {
            val placeAutocompleteFragment: PlaceAutocompleteFragment =
                PlaceAutocompleteFragment.newInstance(
                    PlaceOptions.builder().backgroundColor(Color.WHITE)
                        .build(PlaceOptions.MODE_CARDS)
                )
            placeAutocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
                override fun onPlaceSelected(eLocation: ELocation) {
                    if (eLocation.latitude != null && eLocation.longitude != null) {
                        mBinding.etLongitude.setText(eLocation.longitude)
                        mBinding.etLatitude.setText(eLocation.latitude)
                    }
                    if (!TextUtils.isEmpty(
                            mBinding.etLatitude.text.toString().trim()
                        ) && !TextUtils.isEmpty(mBinding.etLongitude.text.toString().trim())
                    ) {
                        MapmyIndiaDrivingRangeSetting.instance.location = Point.fromLngLat(
                            mBinding.etLongitude.text.toString().trim().toDouble(),
                            mBinding.etLatitude.text.toString().trim().toDouble()
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


            supportFragmentManager.beginTransaction()
                .add(
                    R.id.setting_layout, placeAutocompleteFragment,
                    PlaceAutocompleteFragment::class.java.simpleName
                )
                .addToBackStack(PlaceAutocompleteFragment::class.java.simpleName)
                .commit()
        }
        mBinding.rgRangeType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rb_distance) {
                MapmyIndiaDrivingRangeSetting.instance.rangeType =
                    DrivingRangeCriteria.RANGE_TYPE_DISTANCE
            } else {
                MapmyIndiaDrivingRangeSetting.instance.rangeType =
                    DrivingRangeCriteria.RANGE_TYPE_TIME
            }
        }
        mBinding.btnSaveContourValue.setOnClickListener {
            if (!TextUtils.isEmpty(
                    mBinding.etContourValue.text.toString().trim()
                )
            ) {
                MapmyIndiaDrivingRangeSetting.instance.contourValue =
                    mBinding.etContourValue.text.toString().toInt()
            }
        }
        mBinding.contourColorRG.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_contour_red -> {
                    MapmyIndiaDrivingRangeSetting.instance.contourColor = "ff0000"
                }
                R.id.rb_contour_green -> {
                    MapmyIndiaDrivingRangeSetting.instance.contourColor = "00ff00"
                }
                else -> {
                    MapmyIndiaDrivingRangeSetting.instance.contourColor = "0000ff"
                }
            }
        }
        mBinding.btnDrivingProfile.setOnClickListener {
            if (!TextUtils.isEmpty(
                    mBinding.etDrivingProfile.text.toString().trim()
                )
            ) {
                MapmyIndiaDrivingRangeSetting.instance.drivingProfile =
                    mBinding.etDrivingProfile.text.toString()
            }
        }
        mBinding.showLocationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            MapmyIndiaDrivingRangeSetting.instance.isShowLocations = isChecked
        }
        mBinding.showPolygonSwitch.setOnCheckedChangeListener { _, isChecked ->
            MapmyIndiaDrivingRangeSetting.instance.isForPolygon = isChecked
        }
        mBinding.btnDenoise.setOnClickListener {
            if (!TextUtils.isEmpty(
                    mBinding.etDenoise.text.toString().trim()
                )
            ) {
                MapmyIndiaDrivingRangeSetting.instance.denoise =
                    mBinding.etDenoise.text.toString().toFloat()
            }
        }
        mBinding.btnGeneralize.setOnClickListener {
            if (!TextUtils.isEmpty(
                    mBinding.etGeneralize.text.toString().trim()
                )
            ) {
                MapmyIndiaDrivingRangeSetting.instance.generalize =
                    mBinding.etGeneralize.text.toString().toFloat()
            }
        }
        mBinding.rgSpeedType.setOnCheckedChangeListener { _, checkedId ->
            if(checkedId == R.id.rb_optimal) {
                MapmyIndiaDrivingRangeSetting.instance.speedType = MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL
            } else {
                MapmyIndiaDrivingRangeSetting.instance.speedType = MapmyIndiaDrivingRangeSetting.SPEED_TYPE_PREDECTIVE
            }

            mBinding.layoutPredective.visibility = (if(MapmyIndiaDrivingRangeSetting.instance.speedType == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL) View.GONE else View.VISIBLE)
            mBinding.datePicker.visibility = (if(MapmyIndiaDrivingRangeSetting.instance.speedType == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL || MapmyIndiaDrivingRangeSetting.instance.predectiveType == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT) View.GONE else View.VISIBLE)
            mBinding.timePicker.visibility = (if(MapmyIndiaDrivingRangeSetting.instance.speedType == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL || MapmyIndiaDrivingRangeSetting.instance.predectiveType == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT) View.GONE else View.VISIBLE)
        }
        mBinding.rgPredectiveType.setOnCheckedChangeListener { _, checkedId ->
            if(checkedId == R.id.rb_current) {
                MapmyIndiaDrivingRangeSetting.instance.predectiveType = MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT
            } else {
                MapmyIndiaDrivingRangeSetting.instance.predectiveType = MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CUSTOM
            }

            mBinding.layoutPredective.visibility = (if(MapmyIndiaDrivingRangeSetting.instance.speedType == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL) View.GONE else View.VISIBLE)
            mBinding.datePicker.visibility = (if(MapmyIndiaDrivingRangeSetting.instance.speedType == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL || MapmyIndiaDrivingRangeSetting.instance.predectiveType == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT) View.GONE else View.VISIBLE)
            mBinding.timePicker.visibility = (if(MapmyIndiaDrivingRangeSetting.instance.speedType == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL || MapmyIndiaDrivingRangeSetting.instance.predectiveType == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT) View.GONE else View.VISIBLE)
        }

        mBinding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val calendar = Calendar.getInstance()
            calendar.set(mBinding.datePicker.year, mBinding.datePicker.month, mBinding.datePicker.dayOfMonth, hourOfDay, minute)
            MapmyIndiaDrivingRangeSetting.instance.time = calendar.timeInMillis
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun initialiseView() {
        mBinding.cbCurrentLocation.isChecked =
            MapmyIndiaDrivingRangeSetting.instance.isUsingCurrentLocation
        mBinding.etLatitude.setText("${MapmyIndiaDrivingRangeSetting.instance.location.latitude()}")
        mBinding.etLongitude.setText("${MapmyIndiaDrivingRangeSetting.instance.location.longitude()}")
        mBinding.rbDistance.isChecked =
            MapmyIndiaDrivingRangeSetting.instance.rangeType.equals(DrivingRangeCriteria.RANGE_TYPE_DISTANCE)
        mBinding.rbTime.isChecked =
            MapmyIndiaDrivingRangeSetting.instance.rangeType.equals(DrivingRangeCriteria.RANGE_TYPE_TIME)
        mBinding.etContourValue.setText("${MapmyIndiaDrivingRangeSetting.instance.contourValue}")
        when (MapmyIndiaDrivingRangeSetting.instance.contourColor) {
            "00ff00" -> {
                mBinding.rbContourGreen.isChecked = true
            }
            "0000ff" -> {
                mBinding.rbContourBlue.isChecked = true
            }
            else -> {
                mBinding.rbContourRed.isChecked = true
            }
        }
        mBinding.etDrivingProfile.setText(MapmyIndiaDrivingRangeSetting.instance.drivingProfile)
        mBinding.showLocationsSwitch.isChecked = MapmyIndiaDrivingRangeSetting.instance.isShowLocations
        mBinding.showPolygonSwitch.isChecked = MapmyIndiaDrivingRangeSetting.instance.isForPolygon
        mBinding.etDenoise.setText("${MapmyIndiaDrivingRangeSetting.instance.denoise}")
        mBinding.etGeneralize.setText("${MapmyIndiaDrivingRangeSetting.instance.generalize}")
        mBinding.rgSpeedType.check(if(MapmyIndiaDrivingRangeSetting.instance.speedType == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL) R.id.rb_optimal else R.id.rb_predective)
        mBinding.rgPredectiveType.check(if(MapmyIndiaDrivingRangeSetting.instance.predectiveType == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT) R.id.rb_current else R.id.rb_custom)
        mBinding.layoutPredective.visibility = (if(MapmyIndiaDrivingRangeSetting.instance.speedType == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL) View.GONE else View.VISIBLE)
        mBinding.datePicker.visibility = (if(MapmyIndiaDrivingRangeSetting.instance.speedType == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL || MapmyIndiaDrivingRangeSetting.instance.predectiveType == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT) View.GONE else View.VISIBLE)
        mBinding.timePicker.visibility = (if(MapmyIndiaDrivingRangeSetting.instance.speedType == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL || MapmyIndiaDrivingRangeSetting.instance.predectiveType == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT) View.GONE else View.VISIBLE)
        val date = Date(MapmyIndiaDrivingRangeSetting.instance.time)
        val calender = Calendar.getInstance()
        calender.time = date
        mBinding.datePicker.init(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH)
        ) { view, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth, mBinding.timePicker.hour, mBinding.timePicker.minute)
            MapmyIndiaDrivingRangeSetting.instance.time = calendar.timeInMillis
        }
        mBinding.timePicker.hour = calender.get(Calendar.HOUR_OF_DAY)
        mBinding.timePicker.minute = calender.get(Calendar.MINUTE)
    }
}