package com.mapmyindia.sdk.demo.kotlin.settingscreen

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.ActivityGeofenceWidgetSettingsBinding
import com.mapmyindia.sdk.demo.kotlin.settings.MapmyIndiaGeofenceSetting

class GeofenceWidgetSettingsActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityGeofenceWidgetSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_geofence_widget_settings)
        initSettings()
        initCallbacks()
    }

    private fun initSettings() {

        mBinding.cbDefault.setChecked(MapmyIndiaGeofenceSetting.instance.isDefault)
        mBinding.disableView.setVisibility(if (MapmyIndiaGeofenceSetting.instance.isDefault) View.VISIBLE else View.GONE)
        mBinding.etCircleOutlineWidth.setText(MapmyIndiaGeofenceSetting.instance.circleOutlineWidth.toString() + "")
        mBinding.circleFillColorTv.setText(String.format("#%06X", 0xFFFFFF and MapmyIndiaGeofenceSetting.instance.circleFillColor))
        mBinding.circleFillOutlineColorTv.setText(String.format("#%06X", 0xFFFFFF and MapmyIndiaGeofenceSetting.instance.circleFillOutlineColor))
        mBinding.draggingLineColorTv.setText(String.format("#%06X", 0xFFFFFF and MapmyIndiaGeofenceSetting.instance.draggingLineColor))
        mBinding.etMaxRadius.setText(MapmyIndiaGeofenceSetting.instance.maxRadius.toString() + "")
        mBinding.etMinRadius.setText(MapmyIndiaGeofenceSetting.instance.minRadius.toString() + "")
        mBinding.showSeekbarBtn.isChecked = MapmyIndiaGeofenceSetting.instance.showSeekBar
        mBinding.seekbarPrimaryColorTv.text = String.format("#%06X", 0xFFFFFF and MapmyIndiaGeofenceSetting.instance.seekbarPrimaryColor)
        mBinding.seekbarSecondaryColorTv.text = String.format("#%06X", 0xFFFFFF and MapmyIndiaGeofenceSetting.instance.seekbarPrimaryColor)
        mBinding.polygonDrawingLineColorTv.setText(String.format("#%06X", 0xFFFFFF and MapmyIndiaGeofenceSetting.instance.polygonDrawingLineColor))
        mBinding.polygonFillColorTv.setText(String.format("#%06X", 0xFFFFFF and MapmyIndiaGeofenceSetting.instance.polygonFillColor))
        mBinding.polygonFillOutlineColorTv.setText(String.format("#%06X", 0xFFFFFF and MapmyIndiaGeofenceSetting.instance.polygonFillOutlineColor))
        mBinding.etPolygonOutlineWidth.setText(MapmyIndiaGeofenceSetting.instance.polygonOutlineWidth.toString() + "")
        mBinding.simplifyWhenIntersectingPolygonDetectedBtn.setChecked(MapmyIndiaGeofenceSetting.instance.isSimplifyWhenIntersectingPolygonDetected)
        mBinding.isPolygonBtn.setChecked(MapmyIndiaGeofenceSetting.instance.isPolygon)
        mBinding.etSeekbarCornerRadius.setText(MapmyIndiaGeofenceSetting.instance.seekbarCornerRadius.toString() + "")
    }

    private fun initCallbacks() {
        mBinding.cbDefault.setOnCheckedChangeListener { buttonView, isChecked ->
            MapmyIndiaGeofenceSetting.instance.isDefault = isChecked
            if (isChecked) {
                mBinding.disableView.setVisibility(View.VISIBLE)
            } else {
                mBinding.disableView.setVisibility(View.GONE)
            }
        }
        mBinding.showSeekbarBtn.setOnCheckedChangeListener { buttonView, isChecked -> MapmyIndiaGeofenceSetting.instance.showSeekBar = isChecked }
        mBinding.btnSaveCircleOutlineWidth.setOnClickListener { v ->
            if (!TextUtils.isEmpty(mBinding.etCircleOutlineWidth.getText().toString().trim())) {
                MapmyIndiaGeofenceSetting.instance.circleOutlineWidth = mBinding.etCircleOutlineWidth.getText().toString().trim().toFloat()
            }
        }

        mBinding.btnSeekbarCornerRadius.setOnClickListener { v ->
            if (!TextUtils.isEmpty(mBinding.etSeekbarCornerRadius.text.toString().trim())) {
               MapmyIndiaGeofenceSetting.instance.seekbarCornerRadius = mBinding.etSeekbarCornerRadius.text.toString().trim().toFloat()
            }
        }
        mBinding.circleFillColorLayout.setOnClickListener { v ->
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.instance.circleFillColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok") { dialog, selectedColor, allColors ->
                        MapmyIndiaGeofenceSetting.instance.circleFillColor = selectedColor
                        mBinding.circleFillColorTv.setText(String.format("#%06X", 0xFFFFFF and selectedColor))
                        dialog.dismiss()
                    }
                    .setNegativeButton("cancel") { dialog, which -> }
                    .build()
                    .show()
        }
        mBinding.circleFillOutlineColorLayout.setOnClickListener { v ->
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.instance.circleFillOutlineColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok") { dialog: DialogInterface, selectedColor: Int, allColors: Array<Int?>? ->
                        MapmyIndiaGeofenceSetting.instance.circleFillOutlineColor = selectedColor
                        mBinding.circleFillOutlineColorTv.setText(String.format("#%06X", 0xFFFFFF and selectedColor))
                        dialog.dismiss()
                    }
                    .setNegativeButton("cancel") { dialog: DialogInterface?, which: Int -> }
                    .build()
                    .show()
        }
        mBinding.draggingLineColorLayout.setOnClickListener { v ->
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.instance.draggingLineColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok") { dialog: DialogInterface, selectedColor: Int, allColors: Array<Int?>? ->
                        MapmyIndiaGeofenceSetting.instance.draggingLineColor = selectedColor
                        mBinding.draggingLineColorTv.setText(String.format("#%06X", 0xFFFFFF and selectedColor))
                        dialog.dismiss()
                    }
                    .setNegativeButton("cancel") { dialog: DialogInterface?, which: Int -> }
                    .build()
                    .show()
        }
        mBinding.btnSaveMaxRadius.setOnClickListener { v ->
            if (!TextUtils.isEmpty(mBinding.etMaxRadius.getText().toString().trim())) {
                MapmyIndiaGeofenceSetting.instance.maxRadius = mBinding.etMaxRadius.getText().toString().trim().toInt()
            }
        }
        mBinding.btnSaveMinRadius.setOnClickListener { v ->
            if (!TextUtils.isEmpty(mBinding.etMinRadius.getText().toString().trim())) {
                MapmyIndiaGeofenceSetting.instance.minRadius = mBinding.etMinRadius.getText().toString().trim().toInt()
            }
        }
        mBinding.polygonDrawingLineColorLayout.setOnClickListener { v ->
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.instance.polygonDrawingLineColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                    .density(12)
                    .setPositiveButton("ok") { dialog: DialogInterface, selectedColor: Int, allColors: Array<Int?>? ->
                        MapmyIndiaGeofenceSetting.instance.polygonDrawingLineColor = selectedColor
                        mBinding.polygonDrawingLineColorTv.setText(String.format("#%06X", 0xFFFFFF and selectedColor))
                        dialog.dismiss()
                    }
                    .setNegativeButton("cancel") { dialog: DialogInterface?, which: Int -> }
                    .build()
                    .show()
        }
        mBinding.polygonFillColorLayout.setOnClickListener { v ->
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.instance.polygonFillColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                    .density(12)
                    .setPositiveButton("ok") { dialog: DialogInterface, selectedColor: Int, allColors: Array<Int?>? ->
                        MapmyIndiaGeofenceSetting.instance.polygonFillColor = selectedColor
                        mBinding.polygonFillColorTv.setText(String.format("#%06X", 0xFFFFFF and selectedColor))
                        dialog.dismiss()
                    }
                    .setNegativeButton("cancel") { dialog: DialogInterface?, which: Int -> }
                    .build()
                    .show()
        }
        mBinding.polygonFillOutlineColorLayout.setOnClickListener { v ->
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.instance.polygonFillOutlineColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                    .density(12)
                    .setPositiveButton("ok") { dialog: DialogInterface, selectedColor: Int, allColors: Array<Int?>? ->
                        MapmyIndiaGeofenceSetting.instance.polygonFillOutlineColor = selectedColor
                        mBinding.polygonFillOutlineColorTv.setText(String.format("#%06X", 0xFFFFFF and selectedColor))
                        dialog.dismiss()
                    }
                    .setNegativeButton("cancel") { dialog: DialogInterface?, which: Int -> }
                    .build()
                    .show()
        }

        mBinding.seekbarPrimaryColorLayout.setOnClickListener { v ->
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.instance.seekbarPrimaryColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok") { dialog: DialogInterface, selectedColor: Int, allColors: Array<Int?>? ->
                        MapmyIndiaGeofenceSetting.instance.seekbarPrimaryColor = selectedColor
                        mBinding.seekbarPrimaryColorTv.text = String.format("#%06X", 0xFFFFFF and selectedColor)
                        dialog.dismiss()
                    }
                    .setNegativeButton("cancel") { dialog: DialogInterface?, which: Int -> }
                    .build()
                    .show()
        }

        mBinding.seekbarSecondaryColorLayout.setOnClickListener { v ->
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.instance.seekbarSecondaryColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok") { dialog: DialogInterface, selectedColor: Int, allColors: Array<Int?>? ->
                        MapmyIndiaGeofenceSetting.instance.seekbarSecondaryColor = selectedColor
                        mBinding.seekbarSecondaryColorTv.text = String.format("#%06X", 0xFFFFFF and selectedColor)
                        dialog.dismiss()
                    }
                    .setNegativeButton("cancel") { dialog: DialogInterface?, which: Int -> }
                    .build()
                    .show()
        }
        mBinding.btnPolygonOutlineWidth.setOnClickListener { v ->
            if (!TextUtils.isEmpty(mBinding.etPolygonOutlineWidth.getText().toString().trim())) {
                MapmyIndiaGeofenceSetting.instance.polygonOutlineWidth = mBinding.etPolygonOutlineWidth.getText().toString().trim().toFloat()
            }
        }
        mBinding.simplifyWhenIntersectingPolygonDetectedBtn.setOnCheckedChangeListener { buttonView, isChecked -> MapmyIndiaGeofenceSetting.instance.isSimplifyWhenIntersectingPolygonDetected = isChecked }
        mBinding.isPolygonBtn.setOnCheckedChangeListener { buttonView, isChecked -> MapmyIndiaGeofenceSetting.instance.isPolygon = isChecked }
    }
}