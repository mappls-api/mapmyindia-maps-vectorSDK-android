package com.mapmyindia.sdk.demo.java.settingscreen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityGeofenceWidgetSettingsBinding;
import com.mapmyindia.sdk.demo.java.settings.MapmyIndiaGeofenceSetting;

public class GeofenceWidgetSettingsActivity extends AppCompatActivity {

    private ActivityGeofenceWidgetSettingsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_geofence_widget_settings);
        initSettings();
        initCallbacks();

    }

    private void  initSettings(){

        mBinding.cbDefault.setChecked(MapmyIndiaGeofenceSetting.getInstance().isDefault());
        mBinding.disableView.setVisibility(MapmyIndiaGeofenceSetting.getInstance().isDefault() ? View.VISIBLE : View.GONE);
        mBinding.etCircleOutlineWidth.setText(MapmyIndiaGeofenceSetting.getInstance().getCircleOutlineWidth()+"");
        mBinding.showSeekbarBtn.setChecked(MapmyIndiaGeofenceSetting.getInstance().isShowSeekBar());
        mBinding.seekbarPrimaryColorTv.setText(String.format("#%06X", (0xFFFFFF & MapmyIndiaGeofenceSetting.getInstance().getSeekbarPrimaryColor())));
        mBinding.seekbarSecondaryColorTv.setText(String.format("#%06X", (0xFFFFFF & MapmyIndiaGeofenceSetting.getInstance().getSeekbarPrimaryColor())));
        mBinding.circleFillColorTv.setText(String.format("#%06X", (0xFFFFFF & MapmyIndiaGeofenceSetting.getInstance().getCircleFillColor())));
        mBinding.circleFillOutlineColorTv.setText(String.format("#%06X", (0xFFFFFF & MapmyIndiaGeofenceSetting.getInstance().getCircleFillOutlineColor())));
        mBinding.draggingLineColorTv.setText(String.format("#%06X", (0xFFFFFF & MapmyIndiaGeofenceSetting.getInstance().getDraggingLineColor())));
        mBinding.etMaxRadius.setText(MapmyIndiaGeofenceSetting.getInstance().getMaxRadius()+"");
        mBinding.etMinRadius.setText(MapmyIndiaGeofenceSetting.getInstance().getMinRadius()+"");
        mBinding.polygonDrawingLineColorTv.setText(String.format("#%06X", (0xFFFFFF & MapmyIndiaGeofenceSetting.getInstance().getPolygonDrawingLineColor())));
        mBinding.polygonFillColorTv.setText(String.format("#%06X", (0xFFFFFF & MapmyIndiaGeofenceSetting.getInstance().getPolygonFillColor())));
        mBinding.polygonFillOutlineColorTv.setText(String.format("#%06X", (0xFFFFFF & MapmyIndiaGeofenceSetting.getInstance().getPolygonFillOutlineColor())));
        mBinding.etPolygonOutlineWidth.setText(MapmyIndiaGeofenceSetting.getInstance().getPolygonOutlineWidth()+"");
        mBinding.simplifyWhenIntersectingPolygonDetectedBtn.setChecked(MapmyIndiaGeofenceSetting.getInstance().isSimplifyWhenIntersectingPolygonDetected());
        mBinding.isPolygonBtn.setChecked(MapmyIndiaGeofenceSetting.getInstance().isPolygon());
        mBinding.etSeekbarCornerRadius.setText(MapmyIndiaGeofenceSetting.getInstance().getSeekbarCornerRadius()+"");
    }


    private void initCallbacks(){

        mBinding.cbDefault.setOnCheckedChangeListener((buttonView, isChecked) -> {
            MapmyIndiaGeofenceSetting.getInstance().setDefault(isChecked);
            if (isChecked) {
                mBinding.disableView.setVisibility(View.VISIBLE);
            } else {
                mBinding.disableView.setVisibility(View.GONE);
            }
        });

        mBinding.showSeekbarBtn.setOnCheckedChangeListener((buttonView, isChecked) -> MapmyIndiaGeofenceSetting.getInstance().setShowSeekBar(isChecked));
        mBinding.btnSaveCircleOutlineWidth.setOnClickListener(v -> {
            if((!TextUtils.isEmpty(mBinding.etCircleOutlineWidth.getText().toString().trim()))) {
                MapmyIndiaGeofenceSetting.getInstance().setCircleOutlineWidth(Float.parseFloat(mBinding.etCircleOutlineWidth.getText().toString().trim()));
            }
        });

        mBinding.btnSeekbarCornerRadius.setOnClickListener(v -> {
            if((!TextUtils.isEmpty(mBinding.etSeekbarCornerRadius.getText().toString().trim()))) {
                MapmyIndiaGeofenceSetting.getInstance().setSeekbarCornerRadius(Float.parseFloat(mBinding.etSeekbarCornerRadius.getText().toString().trim()));
            }
        });
        mBinding.circleFillColorLayout.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.getInstance().getCircleFillColor())
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                            MapmyIndiaGeofenceSetting.getInstance().setCircleFillColor(selectedColor);
                            mBinding.circleFillColorTv.setText(String.format("#%06X", (0xFFFFFF & selectedColor)));
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .build()
                    .show();
        });

        mBinding.circleFillOutlineColorLayout.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.getInstance().getCircleFillOutlineColor())
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                        MapmyIndiaGeofenceSetting.getInstance().setCircleFillOutlineColor(selectedColor);
                        mBinding.circleFillOutlineColorTv.setText(String.format("#%06X", (0xFFFFFF & selectedColor)));
                        dialog.dismiss();
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                    })
                    .build()
                    .show();
        });
        mBinding.draggingLineColorLayout.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.getInstance().getDraggingLineColor())
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                        MapmyIndiaGeofenceSetting.getInstance().setDraggingLineColor(selectedColor);
                        mBinding.draggingLineColorTv.setText(String.format("#%06X", (0xFFFFFF & selectedColor)));
                        dialog.dismiss();
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                    })
                    .build()
                    .show();
        });
        mBinding.seekbarPrimaryColorLayout.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.getInstance().getSeekbarPrimaryColor())
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                        MapmyIndiaGeofenceSetting.getInstance().setSeekbarPrimaryColor(selectedColor);
                        mBinding.seekbarPrimaryColorTv.setText(String.format("#%06X", (0xFFFFFF & selectedColor)));
                        dialog.dismiss();
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                    })
                    .build()
                    .show();
        });

        mBinding.seekbarSecondaryColorLayout.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.getInstance().getSeekbarSecondaryColor())
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                        MapmyIndiaGeofenceSetting.getInstance().setSeekbarSecondaryColor(selectedColor);
                        mBinding.seekbarSecondaryColorTv.setText(String.format("#%06X", (0xFFFFFF & selectedColor)));
                        dialog.dismiss();
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                    })
                    .build()
                    .show();
        });

        mBinding.btnSaveMaxRadius.setOnClickListener(v -> {
            if((!TextUtils.isEmpty(mBinding.etMaxRadius.getText().toString().trim()))) {
                MapmyIndiaGeofenceSetting.getInstance().setMaxRadius(Integer.parseInt(mBinding.etMaxRadius.getText().toString().trim()));
            }
        });

        mBinding.btnSaveMinRadius.setOnClickListener(v -> {
            if((!TextUtils.isEmpty(mBinding.etMinRadius.getText().toString().trim()))) {
                MapmyIndiaGeofenceSetting.getInstance().setMinRadius(Integer.parseInt(mBinding.etMinRadius.getText().toString().trim()));
            }
        });
        mBinding.polygonDrawingLineColorLayout.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.getInstance().getPolygonDrawingLineColor())
                    .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                    .density(12)
                    .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                        MapmyIndiaGeofenceSetting.getInstance().setPolygonDrawingLineColor(selectedColor);
                        mBinding.polygonDrawingLineColorTv.setText(String.format("#%06X", (0xFFFFFF & selectedColor)));
                        dialog.dismiss();
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                    })
                    .build()
                    .show();
        });
        mBinding.polygonFillColorLayout.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.getInstance().getPolygonFillColor())
                    .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                    .density(12)
                    .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                        MapmyIndiaGeofenceSetting.getInstance().setPolygonFillColor(selectedColor);
                        mBinding.polygonFillColorTv.setText(String.format("#%06X", (0xFFFFFF & selectedColor)));
                        dialog.dismiss();
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                    })
                    .build()
                    .show();
        });
        mBinding.polygonFillOutlineColorLayout.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(MapmyIndiaGeofenceSetting.getInstance().getPolygonFillOutlineColor())
                    .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                    .density(12)
                    .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                        MapmyIndiaGeofenceSetting.getInstance().setPolygonFillOutlineColor(selectedColor);
                        mBinding.polygonFillOutlineColorTv.setText(String.format("#%06X", (0xFFFFFF & selectedColor)));
                        dialog.dismiss();
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                    })
                    .build()
                    .show();
        });
        mBinding.btnPolygonOutlineWidth.setOnClickListener(v -> {
            if((!TextUtils.isEmpty(mBinding.etPolygonOutlineWidth.getText().toString().trim()))) {
                MapmyIndiaGeofenceSetting.getInstance().setPolygonOutlineWidth(Float.parseFloat(mBinding.etPolygonOutlineWidth.getText().toString().trim()));
            }
        });
        mBinding.simplifyWhenIntersectingPolygonDetectedBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            MapmyIndiaGeofenceSetting.getInstance().setSimplifyWhenIntersectingPolygonDetected(isChecked);
        });
        mBinding.isPolygonBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            MapmyIndiaGeofenceSetting.getInstance().setPolygon(isChecked);
        });

    }
}