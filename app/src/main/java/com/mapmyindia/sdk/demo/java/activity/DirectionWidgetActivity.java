package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.ActivityDirectionWidgetBinding;
import com.mapmyindia.sdk.demo.java.settings.MapmyIndiaDirectionWidgetSetting;
import com.mapmyindia.sdk.direction.ui.DirectionCallback;
import com.mapmyindia.sdk.direction.ui.DirectionFragment;
import com.mapmyindia.sdk.direction.ui.model.DirectionOptions;
import com.mapmyindia.sdk.direction.ui.model.DirectionPoint;
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mmi.services.api.directions.models.DirectionsResponse;

import java.util.List;

public class DirectionWidgetActivity extends AppCompatActivity {

    private ActivityDirectionWidgetBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_direction_widget);

        DirectionOptions.Builder optionsBuilder = DirectionOptions.builder();
        if (!MapmyIndiaDirectionWidgetSetting.getInstance().isDefault()){

            PlaceOptions options = PlaceOptions.builder()
                    .zoom(MapmyIndiaDirectionWidgetSetting.getInstance().getZoom())
                    .hint(MapmyIndiaDirectionWidgetSetting.getInstance().getHint())
                    .location(MapmyIndiaDirectionWidgetSetting.getInstance().getLocation())
                    .filter(MapmyIndiaDirectionWidgetSetting.getInstance().getFilter())
                    .saveHistory(MapmyIndiaDirectionWidgetSetting.getInstance().isEnableHistory())
                    .pod(MapmyIndiaDirectionWidgetSetting.getInstance().getPod())
                    .attributionHorizontalAlignment(MapmyIndiaDirectionWidgetSetting.getInstance().getSignatureVertical())
                    .attributionVerticalAlignment(MapmyIndiaDirectionWidgetSetting.getInstance().getSignatureHorizontal())
                    .logoSize(MapmyIndiaDirectionWidgetSetting.getInstance().getLogoSize())
                    .tokenizeAddress(MapmyIndiaDirectionWidgetSetting.getInstance().isTokenizeAddress())
                    .historyCount(MapmyIndiaDirectionWidgetSetting.getInstance().getHistoryCount())
                    .backgroundColor(getResources().getColor(MapmyIndiaDirectionWidgetSetting.getInstance().getBackgroundColor()))
                    .toolbarColor(getResources().getColor(MapmyIndiaDirectionWidgetSetting.getInstance().getToolbarColor()))
                    .build();


            optionsBuilder.searchPlaceOption(options)
                    .showDefaultMap(true)
                    .showStartNavigation(MapmyIndiaDirectionWidgetSetting.getInstance().isShowStartNavigation())
                    .steps(MapmyIndiaDirectionWidgetSetting.getInstance().isSteps())
                    .resource(MapmyIndiaDirectionWidgetSetting.getInstance().getResource())
                    .profile(MapmyIndiaDirectionWidgetSetting.getInstance().getProfile())
                    .excludes(MapmyIndiaDirectionWidgetSetting.getInstance().getExcludes())
                    .overview(MapmyIndiaDirectionWidgetSetting.getInstance().getOverview())
            .showAlternative(MapmyIndiaDirectionWidgetSetting.getInstance().isShowAlternative())
            .searchAlongRoute(MapmyIndiaDirectionWidgetSetting.getInstance().isShowPOISearch());
        }

        DirectionFragment directionFragment = DirectionFragment.newInstance(optionsBuilder.build());



        getSupportFragmentManager().
                beginTransaction().
                add(mBinding.fragmentConatiner.getId(), directionFragment, DirectionFragment.class.getSimpleName()).
                commit();

        directionFragment.setDirectionCallback(new DirectionCallback() {
            @Override
            public void onCancel() {
                finish();
            }

            @Override
            public void onStartNavigation(DirectionPoint directionPoint, DirectionPoint directionPoint1, List<DirectionPoint> list, DirectionsResponse directionsResponse, int i) {
                Toast.makeText(DirectionWidgetActivity.this, "On Navigation Start", Toast.LENGTH_SHORT).show();
            }
        });
    }
}