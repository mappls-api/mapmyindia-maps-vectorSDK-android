package com.mapmyindia.sdk.demo.java.settingscreen;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityDrivingRangeSettingBinding;
import com.mapmyindia.sdk.demo.java.settings.MapmyIndiaDrivingRangeSetting;
import com.mapmyindia.sdk.demo.java.utils.InputFilterMinMax;
import com.mapmyindia.sdk.drivingrange.DrivingRangeCriteria;
import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment;
import com.mapmyindia.sdk.plugins.places.autocomplete.ui.PlaceSelectionListener;
import com.mmi.services.api.autosuggest.model.ELocation;

import java.util.Calendar;
import java.util.Date;

public class DrivingRangeSettingActivity extends AppCompatActivity {

    private ActivityDrivingRangeSettingBinding mBinding;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_driving_range_setting);
        mBinding.etLatitude.setFilters(new InputFilter[]{new InputFilterMinMax(-90, 90)});
        mBinding.etLongitude.setFilters(new InputFilter[]{new InputFilterMinMax(-180, 180)});
        mBinding.etContourValue.setFilters(new InputFilter[]{new InputFilterMinMax(1, 500)});
        mBinding.etDenoise.setFilters(new InputFilter[]{new InputFilterMinMax(0, 1)});
        mBinding.datePicker.setMinDate(System.currentTimeMillis());
        initialiseView();
        initCallback();
    }

    private void initCallback() {
        mBinding.cbCurrentLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapmyIndiaDrivingRangeSetting.getInstance().setUsingCurrentLocation(isChecked);
            }
        });
        mBinding.btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(
                        mBinding.etLatitude.getText().toString().trim()
                ) && !TextUtils.isEmpty(mBinding.etLongitude.getText().toString().trim())
                ) {
                    MapmyIndiaDrivingRangeSetting.getInstance().setLocation(Point.fromLngLat(
                            Double.parseDouble(mBinding.etLongitude.getText().toString().trim()),
                            Double.parseDouble(mBinding.etLatitude.getText().toString().trim())
                    ));
                }


            }
        });
        mBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceAutocompleteFragment placeAutocompleteFragment =
                        PlaceAutocompleteFragment.newInstance(
                                PlaceOptions.builder().backgroundColor(Color.WHITE)
                                        .build(PlaceOptions.MODE_CARDS)
                        );
                placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(ELocation eLocation) {
                        if (eLocation.latitude != null && eLocation.longitude != null) {
                            mBinding.etLongitude.setText(eLocation.longitude);
                            mBinding.etLatitude.setText(eLocation.latitude);
                        }
                        if (!TextUtils.isEmpty(
                                mBinding.etLatitude.getText().toString().trim()
                        ) && !TextUtils.isEmpty(mBinding.etLongitude.getText().toString().trim())
                        ) {
                            MapmyIndiaDrivingRangeSetting.getInstance().setLocation(Point.fromLngLat(
                                    Double.parseDouble(mBinding.etLongitude.getText().toString().trim()),
                                    Double.parseDouble(mBinding.etLatitude.getText().toString().trim())
                            ));
                        }
                        getSupportFragmentManager().popBackStack(
                                PlaceAutocompleteFragment.class.getSimpleName(),
                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                        );
                    }

                    @Override
                    public void onCancel() {

                        getSupportFragmentManager().popBackStack(
                                PlaceAutocompleteFragment.class.getSimpleName(),
                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                        );
                    }
                });
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.setting_layout, placeAutocompleteFragment, PlaceAutocompleteFragment.class.getSimpleName())
                        .addToBackStack(PlaceAutocompleteFragment.class.getSimpleName())
                        .commit();
            }
        });
        mBinding.rgRangeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_distance) {
                    MapmyIndiaDrivingRangeSetting.getInstance().setRangeType(DrivingRangeCriteria.RANGE_TYPE_DISTANCE);
                } else {
                    MapmyIndiaDrivingRangeSetting.getInstance().setRangeType(DrivingRangeCriteria.RANGE_TYPE_TIME);
                }
            }
        });
        mBinding.btnSaveContourValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mBinding.etContourValue.getText().toString().trim())) {
                    MapmyIndiaDrivingRangeSetting.getInstance().setContourValue(Integer.parseInt(mBinding.etContourValue.getText().toString()));
                }
            }
        });
        mBinding.contourColorRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_contour_red:
                        MapmyIndiaDrivingRangeSetting.getInstance().setContourColor("ff0000");
                        break;
                    case R.id.rb_contour_green:
                        MapmyIndiaDrivingRangeSetting.getInstance().setContourColor("00ff00");
                        break;
                    default:
                        MapmyIndiaDrivingRangeSetting.getInstance().setContourColor("0000ff");
                        break;
                }
            }
        });
        mBinding.btnDrivingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(
                        mBinding.etDrivingProfile.getText().toString().trim()
                )
                ) {
                    MapmyIndiaDrivingRangeSetting.getInstance().setDrivingProfile(mBinding.etDrivingProfile.getText().toString());
                }
            }
        });
        mBinding.showLocationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapmyIndiaDrivingRangeSetting.getInstance().setShowLocations(isChecked);
            }
        });
        mBinding.showPolygonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapmyIndiaDrivingRangeSetting.getInstance().setForPolygon(isChecked);
            }
        });
        mBinding.btnDenoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(
                        mBinding.etDenoise.getText().toString().trim()
                )
                ) {
                    MapmyIndiaDrivingRangeSetting.getInstance().setDenoise(Float.parseFloat(mBinding.etDenoise.getText().toString()));
                }
            }
        });
        mBinding.btnGeneralize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(
                        mBinding.etGeneralize.getText().toString().trim()
                )
                ) {
                    MapmyIndiaDrivingRangeSetting.getInstance().setGeneralize(Float.parseFloat(mBinding.etGeneralize.getText().toString()));
                }
            }
        });
        mBinding.rgSpeedType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_optimal) {
                    MapmyIndiaDrivingRangeSetting.getInstance().setSpeedType(MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL);
                } else {
                    MapmyIndiaDrivingRangeSetting.getInstance().setSpeedType(MapmyIndiaDrivingRangeSetting.SPEED_TYPE_PREDECTIVE);
                }

                mBinding.layoutPredective.setVisibility(MapmyIndiaDrivingRangeSetting.getInstance().getSpeedType() == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL ? View.GONE : View.VISIBLE);
                mBinding.datePicker.setVisibility(MapmyIndiaDrivingRangeSetting.getInstance().getSpeedType() == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL || MapmyIndiaDrivingRangeSetting.getInstance().getPredectiveType() == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT ? View.GONE : View.VISIBLE);
                mBinding.timePicker.setVisibility(MapmyIndiaDrivingRangeSetting.getInstance().getSpeedType() == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL || MapmyIndiaDrivingRangeSetting.getInstance().getPredectiveType() == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT ? View.GONE : View.VISIBLE);
            }
        });
        mBinding.rgPredectiveType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_current) {
                    MapmyIndiaDrivingRangeSetting.getInstance().setPredectiveType(MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT);
                } else {
                    MapmyIndiaDrivingRangeSetting.getInstance().setPredectiveType(MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CUSTOM);
                }
                mBinding.layoutPredective.setVisibility(MapmyIndiaDrivingRangeSetting.getInstance().getSpeedType() == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL ? View.GONE : View.VISIBLE);
                mBinding.datePicker.setVisibility(MapmyIndiaDrivingRangeSetting.getInstance().getSpeedType() == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL || MapmyIndiaDrivingRangeSetting.getInstance().getPredectiveType() == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT ? View.GONE : View.VISIBLE);
                mBinding.timePicker.setVisibility(MapmyIndiaDrivingRangeSetting.getInstance().getSpeedType() == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL || MapmyIndiaDrivingRangeSetting.getInstance().getPredectiveType() == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT ? View.GONE : View.VISIBLE);
            }
        });
        mBinding.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(mBinding.datePicker.getYear(), mBinding.datePicker.getMonth(), mBinding.datePicker.getDayOfMonth(), hourOfDay, minute);
                MapmyIndiaDrivingRangeSetting.getInstance().setTime(calendar.getTimeInMillis());

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initialiseView() {
        mBinding.cbCurrentLocation.setChecked(MapmyIndiaDrivingRangeSetting.getInstance().isUsingCurrentLocation());
        mBinding.etLatitude.setText("" +MapmyIndiaDrivingRangeSetting.getInstance().getLocation().latitude());
        mBinding.etLongitude.setText("" +MapmyIndiaDrivingRangeSetting.getInstance().getLocation().longitude());
        mBinding.rbDistance.setChecked(MapmyIndiaDrivingRangeSetting.getInstance().getRangeType().equals(DrivingRangeCriteria.RANGE_TYPE_DISTANCE));
        mBinding.rbTime.setChecked(MapmyIndiaDrivingRangeSetting.getInstance().getRangeType().equals(DrivingRangeCriteria.RANGE_TYPE_TIME));
        mBinding.etContourValue.setText("" + MapmyIndiaDrivingRangeSetting.getInstance().getContourValue());
        switch (MapmyIndiaDrivingRangeSetting.getInstance().getContourColor()) {
            case "00ff00":
                mBinding.rbContourGreen.setChecked(true);
            break;
            case "0000ff":
                mBinding.rbContourBlue.setChecked(true);
                break;

            default:
                mBinding.rbContourRed.setChecked(true);
            break;
        }
        mBinding.etDrivingProfile.setText(MapmyIndiaDrivingRangeSetting.getInstance().getDrivingProfile());
        mBinding.showLocationsSwitch.setChecked(MapmyIndiaDrivingRangeSetting.getInstance().isShowLocations());
        mBinding.showPolygonSwitch.setChecked(MapmyIndiaDrivingRangeSetting.getInstance().isForPolygon());
        mBinding.etDenoise.setText(""+MapmyIndiaDrivingRangeSetting.getInstance().getDenoise());
        mBinding.etGeneralize.setText(""+MapmyIndiaDrivingRangeSetting.getInstance().getGeneralize());
        mBinding.rgSpeedType.check(MapmyIndiaDrivingRangeSetting.getInstance().getSpeedType() == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL? R.id.rb_optimal : R.id.rb_predective);
        mBinding.rgPredectiveType.check(MapmyIndiaDrivingRangeSetting.getInstance().getPredectiveType() == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT? R.id.rb_current : R.id.rb_custom);
        mBinding.layoutPredective.setVisibility(MapmyIndiaDrivingRangeSetting.getInstance().getSpeedType() == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL? View.GONE :View.VISIBLE);
        mBinding.datePicker.setVisibility(MapmyIndiaDrivingRangeSetting.getInstance().getSpeedType() == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL || MapmyIndiaDrivingRangeSetting.getInstance().getPredectiveType() == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT? View.GONE :View.VISIBLE);
        mBinding.timePicker.setVisibility(MapmyIndiaDrivingRangeSetting.getInstance().getSpeedType() == MapmyIndiaDrivingRangeSetting.SPEED_TYPE_OPTIMAL || MapmyIndiaDrivingRangeSetting.getInstance().getPredectiveType() == MapmyIndiaDrivingRangeSetting.PREDECTIVE_TYPE_CURRENT? View.GONE :View.VISIBLE);
        Date date = new Date(MapmyIndiaDrivingRangeSetting.getInstance().getTime());
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        mBinding.datePicker.init(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth, mBinding.timePicker.getHour(), mBinding.timePicker.getMinute());
                MapmyIndiaDrivingRangeSetting.getInstance().setTime(calendar.getTimeInMillis());
            }
        });
        mBinding.timePicker.setHour(calender.get(Calendar.HOUR_OF_DAY));
        mBinding.timePicker.setMinute(calender.get(Calendar.MINUTE));
    }
}