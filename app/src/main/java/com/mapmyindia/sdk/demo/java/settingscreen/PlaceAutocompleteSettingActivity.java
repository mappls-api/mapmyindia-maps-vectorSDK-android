package com.mapmyindia.sdk.demo.java.settingscreen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapbox.geojson.Point;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityPlaceAutocompleteSettingBinding;
import com.mapmyindia.sdk.demo.java.settings.MapmyIndiaPlaceWidgetSetting;
import com.mapmyindia.sdk.demo.java.utils.InputFilterMinMax;
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mmi.services.api.autosuggest.AutoSuggestCriteria;

public class PlaceAutocompleteSettingActivity extends AppCompatActivity {

    private ActivityPlaceAutocompleteSettingBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_place_autocomplete_setting);
        mBinding.etLatitude.setFilters(new InputFilter[]{new InputFilterMinMax(-90, 90)});
        mBinding.etLongitude.setFilters(new InputFilter[]{new InputFilterMinMax(-180, 180)});
        initSettings();
        initCallback();
    }

    private void initCallback() {
        mBinding.cbDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapmyIndiaPlaceWidgetSetting.getInstance().setDefault(isChecked);
                if (isChecked) {
                    mBinding.disableView.setVisibility(View.VISIBLE);
                } else {
                    mBinding.disableView.setVisibility(View.GONE);
                }
            }
        });

        mBinding.rgVertical.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mBinding.rbTop.getId()) {
                    MapmyIndiaPlaceWidgetSetting.getInstance().setSignatureVertical(PlaceOptions.GRAVITY_TOP);
                } else {

                    MapmyIndiaPlaceWidgetSetting.getInstance().setSignatureVertical(PlaceOptions.GRAVITY_BOTTOM);
                }
            }
        });

        mBinding.rgHorizontal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_left:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setSignatureHorizontal(PlaceOptions.GRAVITY_LEFT);
                        break;
                    case R.id.rb_center:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setSignatureHorizontal(PlaceOptions.GRAVITY_CENTER);
                        break;
                    case R.id.rb_right:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setSignatureHorizontal(PlaceOptions.GRAVITY_RIGHT);
                        break;
                }
            }
        });

        mBinding.rgLogoSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_small:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setLogoSize(PlaceOptions.SIZE_SMALL);
                        break;
                    case R.id.rb_medium:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setLogoSize(PlaceOptions.SIZE_MEDIUM);
                        break;
                    case R.id.rb_large:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setLogoSize(PlaceOptions.SIZE_LARGE);
                        break;
                }
            }
        });

        mBinding.btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!TextUtils.isEmpty(mBinding.etLatitude.getText().toString().trim())) && (!TextUtils.isEmpty(mBinding.etLongitude.getText().toString().trim()))) {
                    MapmyIndiaPlaceWidgetSetting.getInstance().setLocation(Point.fromLngLat(Double.parseDouble(mBinding.etLongitude.getText().toString().trim()), Double.parseDouble(mBinding.etLatitude.getText().toString().trim())));
                } else {
                    MapmyIndiaPlaceWidgetSetting.getInstance().setLocation(null);
                }
                Toast.makeText(PlaceAutocompleteSettingActivity.this, "Location save successfully", Toast.LENGTH_SHORT).show();
            }
        });

        mBinding.btnSaveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mBinding.etFilter.getText().toString().trim())) {
                    MapmyIndiaPlaceWidgetSetting.getInstance().setFilter(mBinding.etFilter.getText().toString().trim());
                } else {
                    MapmyIndiaPlaceWidgetSetting.getInstance().setFilter(null);
                }
                Toast.makeText(PlaceAutocompleteSettingActivity.this, "Filter save successfully", Toast.LENGTH_SHORT).show();
            }
        });

        mBinding.cbEnableHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapmyIndiaPlaceWidgetSetting.getInstance().setEnableHistory(isChecked);
            }
        });
        mBinding.btnPodClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.rgPod.clearCheck();
                MapmyIndiaPlaceWidgetSetting.getInstance().setPod(null);
            }
        });
        mBinding.rgPod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_city:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setPod(AutoSuggestCriteria.POD_CITY);
                        break;
                    case R.id.rb_state:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setPod(AutoSuggestCriteria.POD_STATE);
                        break;
                    case R.id.rb_sub_district:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setPod(AutoSuggestCriteria.POD_SUB_DISTRICT);
                        break;
                    case R.id.rb_district:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setPod(AutoSuggestCriteria.POD_DISTRICT);
                        break;
                    case R.id.rb_sub_locality:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setPod(AutoSuggestCriteria.POD_SUB_LOCALITY);
                        break;
                    case R.id.rb_sub_sub_locality:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setPod(AutoSuggestCriteria.POD_SUB_SUB_LOCALITY);
                        break;
                    case R.id.rb_locality:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setPod(AutoSuggestCriteria.POD_LOCALITY);
                        break;
                    case R.id.rb_village:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setPod(AutoSuggestCriteria.POD_VILLAGE);
                        break;
                }
            }
        });


        mBinding.btnHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mBinding.etHint.getText().toString().trim())) {
                    MapmyIndiaPlaceWidgetSetting.getInstance().setHint(mBinding.etHint.getText().toString().trim());
                    Toast.makeText(PlaceAutocompleteSettingActivity.this, "Hint save successfully", Toast.LENGTH_SHORT).show();
                } else {
                    mBinding.etHint.setText(MapmyIndiaPlaceWidgetSetting.getInstance().getHint());
                    Toast.makeText(PlaceAutocompleteSettingActivity.this, "Hint is mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBinding.cbEnableTextSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapmyIndiaPlaceWidgetSetting.getInstance().setEnableTextSearch(isChecked);
            }
        });
        mBinding.backgroundRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_white:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setBackgroundColor(android.R.color.white);
                        break;
                    case R.id.rb_black:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setBackgroundColor(android.R.color.black);
                        break;
                    case R.id.rb_red:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setBackgroundColor(android.R.color.holo_red_light);
                        break;
                    case R.id.rb_green:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setBackgroundColor(android.R.color.holo_green_dark);
                        break;
                    case R.id.rb_blue:
                        MapmyIndiaPlaceWidgetSetting.getInstance().setBackgroundColor(android.R.color.holo_blue_bright);
                        break;
                }
            }
        });
        mBinding.toolbarRG.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_white_toolbar:
                    MapmyIndiaPlaceWidgetSetting.getInstance().setToolbarColor(android.R.color.white);
                    break;
                case R.id.rb_black_toolbar:
                    MapmyIndiaPlaceWidgetSetting.getInstance().setToolbarColor(android.R.color.black);
                    break;
                case R.id.rb_red_toolbar:
                    MapmyIndiaPlaceWidgetSetting.getInstance().setToolbarColor(android.R.color.holo_red_light);
                    break;
                case R.id.rb_green_toolbar:
                    MapmyIndiaPlaceWidgetSetting.getInstance().setToolbarColor(android.R.color.holo_green_dark);
                    break;
                case R.id.rb_blue_toolbar:
                    MapmyIndiaPlaceWidgetSetting.getInstance().setToolbarColor(android.R.color.holo_blue_bright);
                    break;
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void initSettings() {
        mBinding.cbDefault.setChecked(MapmyIndiaPlaceWidgetSetting.getInstance().isDefault());
        mBinding.disableView.setVisibility(MapmyIndiaPlaceWidgetSetting.getInstance().isDefault() ? View.VISIBLE : View.GONE);

        if (MapmyIndiaPlaceWidgetSetting.getInstance().getSignatureVertical() == PlaceOptions.GRAVITY_TOP) {
            mBinding.rgVertical.check(mBinding.rbTop.getId());
        } else {
            mBinding.rgVertical.check(mBinding.rbBottom.getId());
        }

        if (MapmyIndiaPlaceWidgetSetting.getInstance().getSignatureHorizontal() == PlaceOptions.GRAVITY_LEFT) {
            mBinding.rgHorizontal.check(mBinding.rbLeft.getId());
        } else if (MapmyIndiaPlaceWidgetSetting.getInstance().getSignatureHorizontal() == PlaceOptions.GRAVITY_CENTER) {
            mBinding.rgHorizontal.check(mBinding.rbCenter.getId());
        } else {
            mBinding.rgHorizontal.check(mBinding.rbRight.getId());
        }

        if (MapmyIndiaPlaceWidgetSetting.getInstance().getLogoSize() == PlaceOptions.SIZE_SMALL) {
            mBinding.rgLogoSize.check(R.id.rb_small);
        } else if (MapmyIndiaPlaceWidgetSetting.getInstance().getLogoSize() == PlaceOptions.SIZE_MEDIUM) {
            mBinding.rgLogoSize.check(R.id.rb_medium);
        } else {
            mBinding.rgLogoSize.check(R.id.rb_large);
        }

        if(MapmyIndiaPlaceWidgetSetting.getInstance().getLocation() != null) {
            mBinding.etLatitude.setText(String.valueOf(MapmyIndiaPlaceWidgetSetting.getInstance().getLocation().latitude()));
            mBinding.etLongitude.setText(String.valueOf(MapmyIndiaPlaceWidgetSetting.getInstance().getLocation().longitude()));
        }
        if(MapmyIndiaPlaceWidgetSetting.getInstance().getFilter() != null) {
            mBinding.etFilter.setText(MapmyIndiaPlaceWidgetSetting.getInstance().getFilter());
        }

        mBinding.cbEnableHistory.setChecked(MapmyIndiaPlaceWidgetSetting.getInstance().isEnableHistory());

        if(MapmyIndiaPlaceWidgetSetting.getInstance().getPod() != null) {
            if(MapmyIndiaPlaceWidgetSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_CITY)) {
                mBinding.rgPod.check(mBinding.rbCity.getId());
            } else if(MapmyIndiaPlaceWidgetSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_DISTRICT)) {
                mBinding.rgPod.check(mBinding.rbDistrict.getId());
            } else if(MapmyIndiaPlaceWidgetSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_LOCALITY)) {
                mBinding.rgPod.check(mBinding.rbLocality.getId());
            } else if(MapmyIndiaPlaceWidgetSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_STATE)) {
                mBinding.rgPod.check(mBinding.rbState.getId());
            } else if(MapmyIndiaPlaceWidgetSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_SUB_DISTRICT)) {
                mBinding.rgPod.check(mBinding.rbSubDistrict.getId());
            } else if(MapmyIndiaPlaceWidgetSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_SUB_LOCALITY)) {
                mBinding.rgPod.check(mBinding.rbSubLocality.getId());
            } else if(MapmyIndiaPlaceWidgetSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_SUB_SUB_LOCALITY)) {
                mBinding.rgPod.check(mBinding.rbSubSubLocality.getId());
            } else if(MapmyIndiaPlaceWidgetSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_VILLAGE)) {
                mBinding.rgPod.check(mBinding.rbVillage.getId());
            }
        } else {
            mBinding.rgPod.clearCheck();
        }

        mBinding.etHint.setText(MapmyIndiaPlaceWidgetSetting.getInstance().getHint());
        mBinding.cbEnableTextSearch.setChecked(MapmyIndiaPlaceWidgetSetting.getInstance().isEnableTextSearch());


        if (MapmyIndiaPlaceWidgetSetting.getInstance().getBackgroundColor()== android.R.color.white){
            mBinding.backgroundRG.check(mBinding.rbWhite.getId());
        }else  if (MapmyIndiaPlaceWidgetSetting.getInstance().getBackgroundColor()== android.R.color.black){
            mBinding.backgroundRG.check(mBinding.rbBlack.getId());
        }else  if (MapmyIndiaPlaceWidgetSetting.getInstance().getBackgroundColor()== android.R.color.holo_red_light){
            mBinding.backgroundRG.check(mBinding.rbRed.getId());
        }else  if (MapmyIndiaPlaceWidgetSetting.getInstance().getBackgroundColor()== android.R.color.holo_green_dark){
            mBinding.backgroundRG.check(mBinding.rbGreen.getId());
        }else  if (MapmyIndiaPlaceWidgetSetting.getInstance().getBackgroundColor()== android.R.color.holo_blue_bright){
            mBinding.backgroundRG.check(mBinding.rbBlue.getId());
        }


        if (MapmyIndiaPlaceWidgetSetting.getInstance().getToolbarColor()== android.R.color.white){
            mBinding.toolbarRG.check(mBinding.rbWhiteToolbar.getId());
        }else  if (MapmyIndiaPlaceWidgetSetting.getInstance().getToolbarColor()== android.R.color.black){
            mBinding.toolbarRG.check(mBinding.rbBlackToolbar.getId());
        }else  if (MapmyIndiaPlaceWidgetSetting.getInstance().getToolbarColor()== android.R.color.holo_red_light){
            mBinding.toolbarRG.check(mBinding.rbRedToolbar.getId());
        }else  if (MapmyIndiaPlaceWidgetSetting.getInstance().getToolbarColor()== android.R.color.holo_green_dark){
            mBinding.toolbarRG.check(mBinding.rbGreenToolbar.getId());
        }else  if (MapmyIndiaPlaceWidgetSetting.getInstance().getToolbarColor()== android.R.color.holo_blue_bright){
            mBinding.toolbarRG.check(mBinding.rbBlueToolbar.getId());
        }
    }
}