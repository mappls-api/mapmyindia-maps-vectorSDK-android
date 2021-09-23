package com.mapmyindia.sdk.demo.java.settingscreen;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityPlacePickerSettingsBinding;
import com.mapmyindia.sdk.demo.java.settings.MapmyIndiaPlacePickerSetting;
import com.mapmyindia.sdk.demo.java.utils.InputFilterMinMax;
import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mmi.services.api.autosuggest.AutoSuggestCriteria;

public class PlacePickerSettingsActivity extends AppCompatActivity {
    private ActivityPlacePickerSettingsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_place_picker_settings);
        mBinding.etLatitude.setFilters(new InputFilter[]{new InputFilterMinMax(-90, 90)});
        mBinding.etLongitude.setFilters(new InputFilter[]{new InputFilterMinMax(-180, 180)});
        initSettings();
        initCallback();
    }

    private void initCallback() {

        mBinding.pickerToolbarColor.setText(String.format("#%06X", (0xFFFFFF & MapmyIndiaPlacePickerSetting.getInstance().getPickerToolbarColor())));
        mBinding.includeDeviceLocation.setChecked(MapmyIndiaPlacePickerSetting.getInstance().isIncludeDeviceLocation());
        mBinding.includeSearchButton.setChecked(MapmyIndiaPlacePickerSetting.getInstance().isIncludeSearch());
        mBinding.tokenizeAddressBtn.setChecked(MapmyIndiaPlacePickerSetting.getInstance().isTokenizeAddress());

        mBinding.cbDefault.setOnCheckedChangeListener((buttonView, isChecked) -> {
            MapmyIndiaPlacePickerSetting.getInstance().setDefault(isChecked);
            if (isChecked) {
                mBinding.disableView.setVisibility(View.VISIBLE);
            } else {
                mBinding.disableView.setVisibility(View.GONE);
            }
        });

        mBinding.rgVertical.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == mBinding.rbTop.getId()) {
                MapmyIndiaPlacePickerSetting.getInstance().setSignatureVertical(PlaceOptions.GRAVITY_TOP);
            } else {

                MapmyIndiaPlacePickerSetting.getInstance().setSignatureVertical(PlaceOptions.GRAVITY_BOTTOM);
            }
        });

        mBinding.rgHorizontal.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_left:
                    MapmyIndiaPlacePickerSetting.getInstance().setSignatureHorizontal(PlaceOptions.GRAVITY_LEFT);
                    break;
                case R.id.rb_center:
                    MapmyIndiaPlacePickerSetting.getInstance().setSignatureHorizontal(PlaceOptions.GRAVITY_CENTER);
                    break;
                case R.id.rb_right:
                    MapmyIndiaPlacePickerSetting.getInstance().setSignatureHorizontal(PlaceOptions.GRAVITY_RIGHT);
                    break;
            }
        });

        mBinding.rgLogoSize.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_small:
                    MapmyIndiaPlacePickerSetting.getInstance().setLogoSize(PlaceOptions.SIZE_SMALL);
                    break;
                case R.id.rb_medium:
                    MapmyIndiaPlacePickerSetting.getInstance().setLogoSize(PlaceOptions.SIZE_MEDIUM);
                    break;
                case R.id.rb_large:
                    MapmyIndiaPlacePickerSetting.getInstance().setLogoSize(PlaceOptions.SIZE_LARGE);
                    break;
            }
        });

        mBinding.btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!TextUtils.isEmpty(mBinding.etLatitude.getText().toString().trim())) && (!TextUtils.isEmpty(mBinding.etLongitude.getText().toString().trim()))) {
                    MapmyIndiaPlacePickerSetting.getInstance().setLocation(Point.fromLngLat(Double.parseDouble(mBinding.etLongitude.getText().toString().trim()), Double.parseDouble(mBinding.etLatitude.getText().toString().trim())));
                } else {
                    MapmyIndiaPlacePickerSetting.getInstance().setLocation(null);
                }
                Toast.makeText(PlacePickerSettingsActivity.this, "Location save successfully", Toast.LENGTH_SHORT).show();
            }
        });

        mBinding.btnSaveHistoryCount.setOnClickListener(v -> {
            if((!TextUtils.isEmpty(mBinding.etHistoryCount.getText().toString()))) {
                MapmyIndiaPlacePickerSetting.getInstance().setHistoryCount(Integer.parseInt(mBinding.etHistoryCount.getText().toString()));
            } else {
                MapmyIndiaPlacePickerSetting.getInstance().setHistoryCount(null);
            }
            Toast.makeText(PlacePickerSettingsActivity.this, "History Count save successfully", Toast.LENGTH_SHORT).show();
        });

        mBinding.btnSaveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mBinding.etFilter.getText().toString().trim())) {
                    MapmyIndiaPlacePickerSetting.getInstance().setFilter(mBinding.etFilter.getText().toString().trim());
                } else {
                    MapmyIndiaPlacePickerSetting.getInstance().setFilter(null);
                }
                Toast.makeText(PlacePickerSettingsActivity.this, "Filter save successfully", Toast.LENGTH_SHORT).show();
            }
        });

        mBinding.cbEnableHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapmyIndiaPlacePickerSetting.getInstance().setEnableHistory(isChecked);
               if (isChecked){
                   mBinding.historyCountLayout.setVisibility(View.VISIBLE);
               }else
               {
                   mBinding.historyCountLayout.setVisibility(View.GONE);
               }

            }
        });
        mBinding.btnPodClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.rgPod.clearCheck();
                MapmyIndiaPlacePickerSetting.getInstance().setPod(null);
            }
        });
        mBinding.rgPod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_city:
                        MapmyIndiaPlacePickerSetting.getInstance().setPod(AutoSuggestCriteria.POD_CITY);
                        break;
                    case R.id.rb_state:
                        MapmyIndiaPlacePickerSetting.getInstance().setPod(AutoSuggestCriteria.POD_STATE);
                        break;
                    case R.id.rb_sub_district:
                        MapmyIndiaPlacePickerSetting.getInstance().setPod(AutoSuggestCriteria.POD_SUB_DISTRICT);
                        break;
                    case R.id.rb_district:
                        MapmyIndiaPlacePickerSetting.getInstance().setPod(AutoSuggestCriteria.POD_DISTRICT);
                        break;
                    case R.id.rb_sub_locality:
                        MapmyIndiaPlacePickerSetting.getInstance().setPod(AutoSuggestCriteria.POD_SUB_LOCALITY);
                        break;
                    case R.id.rb_sub_sub_locality:
                        MapmyIndiaPlacePickerSetting.getInstance().setPod(AutoSuggestCriteria.POD_SUB_SUB_LOCALITY);
                        break;
                    case R.id.rb_locality:
                        MapmyIndiaPlacePickerSetting.getInstance().setPod(AutoSuggestCriteria.POD_LOCALITY);
                        break;
                    case R.id.rb_village:
                        MapmyIndiaPlacePickerSetting.getInstance().setPod(AutoSuggestCriteria.POD_VILLAGE);
                        break;
                }
            }
        });


        mBinding.btnHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mBinding.etHint.getText().toString().trim())) {
                    MapmyIndiaPlacePickerSetting.getInstance().setHint(mBinding.etHint.getText().toString().trim());
                    Toast.makeText(PlacePickerSettingsActivity.this, "Hint save successfully", Toast.LENGTH_SHORT).show();
                } else {
                    mBinding.etHint.setText(MapmyIndiaPlacePickerSetting.getInstance().getHint());
                    Toast.makeText(PlacePickerSettingsActivity.this, "Hint is mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBinding.btnZoom.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(mBinding.etZoom.getText().toString().trim())) {
                MapmyIndiaPlacePickerSetting.getInstance().setZoom(Double.parseDouble(mBinding.etZoom.getText().toString().trim()));
                Toast.makeText(PlacePickerSettingsActivity.this, "zoom save successfully", Toast.LENGTH_SHORT).show();
            }
        });

       /* mBinding.cbEnableTextSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MapmyIndiaPlacePickerSetting.getInstance().setEnableTextSearch(isChecked);
            }
        });*/
        mBinding.backgroundRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_white:
                        MapmyIndiaPlacePickerSetting.getInstance().setBackgroundColor(android.R.color.white);
                        break;
                    case R.id.rb_black:
                        MapmyIndiaPlacePickerSetting.getInstance().setBackgroundColor(android.R.color.black);
                        break;
                    case R.id.rb_red:
                        MapmyIndiaPlacePickerSetting.getInstance().setBackgroundColor(android.R.color.holo_red_light);
                        break;
                    case R.id.rb_green:
                        MapmyIndiaPlacePickerSetting.getInstance().setBackgroundColor(android.R.color.holo_green_dark);
                        break;
                    case R.id.rb_blue:
                        MapmyIndiaPlacePickerSetting.getInstance().setBackgroundColor(android.R.color.holo_blue_bright);
                        break;
                }
            }
        });
        mBinding.toolbarRG.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_white_toolbar:
                    MapmyIndiaPlacePickerSetting.getInstance().setToolbarColor(android.R.color.white);
                    break;
                case R.id.rb_black_toolbar:
                    MapmyIndiaPlacePickerSetting.getInstance().setToolbarColor(android.R.color.black);
                    break;
                case R.id.rb_red_toolbar:
                    MapmyIndiaPlacePickerSetting.getInstance().setToolbarColor(android.R.color.holo_red_light);
                    break;
                case R.id.rb_green_toolbar:
                    MapmyIndiaPlacePickerSetting.getInstance().setToolbarColor(android.R.color.holo_green_dark);
                    break;
                case R.id.rb_blue_toolbar:
                    MapmyIndiaPlacePickerSetting.getInstance().setToolbarColor(android.R.color.holo_blue_bright);
                    break;
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void initSettings() {

       mBinding.toolbarColorLayout.setOnClickListener(v -> {
           ColorPickerDialogBuilder
                   .with(this)
                   .setTitle("Choose color")
                   .initialColor(MapmyIndiaPlacePickerSetting.getInstance().getPickerToolbarColor())
                   .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                   .density(12)
                   .setPositiveButton("ok", new ColorPickerClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                          // changeBackgroundColor(selectedColor);
                           MapmyIndiaPlacePickerSetting.getInstance().setPickerToolbarColor(selectedColor);
                           mBinding.pickerToolbarColor.setText(String.format("#%06X", (0xFFFFFF & selectedColor)));
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

        mBinding.includeDeviceLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
             MapmyIndiaPlacePickerSetting.getInstance().setIncludeDeviceLocation(isChecked);
        });
        mBinding.includeSearchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            MapmyIndiaPlacePickerSetting.getInstance().setIncludeSearch(isChecked);
        });
      mBinding.tokenizeAddressBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
          MapmyIndiaPlacePickerSetting.getInstance().setTokenizeAddress(isChecked);
      });

        mBinding.cbDefault.setChecked(MapmyIndiaPlacePickerSetting.getInstance().isDefault());
        mBinding.disableView.setVisibility(MapmyIndiaPlacePickerSetting.getInstance().isDefault() ? View.VISIBLE : View.GONE);

        if (MapmyIndiaPlacePickerSetting.getInstance().getHistoryCount()!=null){
            mBinding.etHistoryCount.setText(MapmyIndiaPlacePickerSetting.getInstance().getHistoryCount().toString());
        }

        if (MapmyIndiaPlacePickerSetting.getInstance().getSignatureVertical() == PlaceOptions.GRAVITY_TOP) {
            mBinding.rgVertical.check(mBinding.rbTop.getId());
        } else {
            mBinding.rgVertical.check(mBinding.rbBottom.getId());
        }

        if (MapmyIndiaPlacePickerSetting.getInstance().getSignatureHorizontal() == PlaceOptions.GRAVITY_LEFT) {
            mBinding.rgHorizontal.check(mBinding.rbLeft.getId());
        } else if (MapmyIndiaPlacePickerSetting.getInstance().getSignatureHorizontal() == PlaceOptions.GRAVITY_CENTER) {
            mBinding.rgHorizontal.check(mBinding.rbCenter.getId());
        } else {
            mBinding.rgHorizontal.check(mBinding.rbRight.getId());
        }

        if (MapmyIndiaPlacePickerSetting.getInstance().getLogoSize() == PlaceOptions.SIZE_SMALL) {
            mBinding.rgLogoSize.check(R.id.rb_small);
        } else if (MapmyIndiaPlacePickerSetting.getInstance().getLogoSize() == PlaceOptions.SIZE_MEDIUM) {
            mBinding.rgLogoSize.check(R.id.rb_medium);
        } else {
            mBinding.rgLogoSize.check(R.id.rb_large);
        }

        if(MapmyIndiaPlacePickerSetting.getInstance().getLocation() != null) {
            mBinding.etLatitude.setText(String.valueOf(MapmyIndiaPlacePickerSetting.getInstance().getLocation().latitude()));
            mBinding.etLongitude.setText(String.valueOf(MapmyIndiaPlacePickerSetting.getInstance().getLocation().longitude()));
        }
        if(MapmyIndiaPlacePickerSetting.getInstance().getFilter() != null) {
            mBinding.etFilter.setText(MapmyIndiaPlacePickerSetting.getInstance().getFilter());
        }

        mBinding.cbEnableHistory.setChecked(MapmyIndiaPlacePickerSetting.getInstance().isEnableHistory());
        if (!MapmyIndiaPlacePickerSetting.getInstance().isEnableHistory()){
            mBinding.historyCountLayout.setVisibility(View.GONE);
        }else {
            mBinding.historyCountLayout.setVisibility(View.VISIBLE);
        }

        if(MapmyIndiaPlacePickerSetting.getInstance().getPod() != null) {
            if(MapmyIndiaPlacePickerSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_CITY)) {
                mBinding.rgPod.check(mBinding.rbCity.getId());
            } else if(MapmyIndiaPlacePickerSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_DISTRICT)) {
                mBinding.rgPod.check(mBinding.rbDistrict.getId());
            } else if(MapmyIndiaPlacePickerSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_LOCALITY)) {
                mBinding.rgPod.check(mBinding.rbLocality.getId());
            } else if(MapmyIndiaPlacePickerSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_STATE)) {
                mBinding.rgPod.check(mBinding.rbState.getId());
            } else if(MapmyIndiaPlacePickerSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_SUB_DISTRICT)) {
                mBinding.rgPod.check(mBinding.rbSubDistrict.getId());
            } else if(MapmyIndiaPlacePickerSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_SUB_LOCALITY)) {
                mBinding.rgPod.check(mBinding.rbSubLocality.getId());
            } else if(MapmyIndiaPlacePickerSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_SUB_SUB_LOCALITY)) {
                mBinding.rgPod.check(mBinding.rbSubSubLocality.getId());
            } else if(MapmyIndiaPlacePickerSetting.getInstance().getPod().equalsIgnoreCase(AutoSuggestCriteria.POD_VILLAGE)) {
                mBinding.rgPod.check(mBinding.rbVillage.getId());
            }
        } else {
            mBinding.rgPod.clearCheck();
        }

        mBinding.etHint.setText(MapmyIndiaPlacePickerSetting.getInstance().getHint());
        if (MapmyIndiaPlacePickerSetting.getInstance().getZoom()!=null){
            mBinding.etZoom.setText(MapmyIndiaPlacePickerSetting.getInstance().getZoom().toString());

        }
       // mBinding.cbEnableTextSearch.setChecked(MapmyIndiaPlacePickerSetting.getInstance().isEnableTextSearch());


        if (MapmyIndiaPlacePickerSetting.getInstance().getBackgroundColor()== android.R.color.white){
            mBinding.backgroundRG.check(mBinding.rbWhite.getId());
        }else  if (MapmyIndiaPlacePickerSetting.getInstance().getBackgroundColor()== android.R.color.black){
            mBinding.backgroundRG.check(mBinding.rbBlack.getId());
        }else  if (MapmyIndiaPlacePickerSetting.getInstance().getBackgroundColor()== android.R.color.holo_red_light){
            mBinding.backgroundRG.check(mBinding.rbRed.getId());
        }else  if (MapmyIndiaPlacePickerSetting.getInstance().getBackgroundColor()== android.R.color.holo_green_dark){
            mBinding.backgroundRG.check(mBinding.rbGreen.getId());
        }else  if (MapmyIndiaPlacePickerSetting.getInstance().getBackgroundColor()== android.R.color.holo_blue_bright){
            mBinding.backgroundRG.check(mBinding.rbBlue.getId());
        }


        if (MapmyIndiaPlacePickerSetting.getInstance().getToolbarColor()== android.R.color.white){
            mBinding.toolbarRG.check(mBinding.rbWhiteToolbar.getId());
        }else  if (MapmyIndiaPlacePickerSetting.getInstance().getToolbarColor()== android.R.color.black){
            mBinding.toolbarRG.check(mBinding.rbBlackToolbar.getId());
        }else  if (MapmyIndiaPlacePickerSetting.getInstance().getToolbarColor()== android.R.color.holo_red_light){
            mBinding.toolbarRG.check(mBinding.rbRedToolbar.getId());
        }else  if (MapmyIndiaPlacePickerSetting.getInstance().getToolbarColor()== android.R.color.holo_green_dark){
            mBinding.toolbarRG.check(mBinding.rbGreenToolbar.getId());
        }else  if (MapmyIndiaPlacePickerSetting.getInstance().getToolbarColor()== android.R.color.holo_blue_bright){
            mBinding.toolbarRG.check(mBinding.rbBlueToolbar.getId());
        }
    }

}