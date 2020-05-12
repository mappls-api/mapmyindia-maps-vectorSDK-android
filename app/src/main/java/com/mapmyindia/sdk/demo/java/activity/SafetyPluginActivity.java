package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.safety.MapmyIndiaSafetyPlugin;
import com.mapmyindia.sdk.safety.callbacks.ISafetyListener;
import com.mapmyindia.sdk.safety.containmentlayer.vo.ContainmentZoneInfo;
import com.mapmyindia.sdk.safety.internal.callbacks.IAuthListener;

import java.text.DecimalFormat;

import timber.log.Timber;

public class SafetyPluginActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView txtInsideZone;
    private TextView txtZoneName;
    private TextView txtDistance;
    private TextView txtMapLink;
    private TextView txtDistrictName;
    private TextView txtZoneType;
    private TextView startStopSafetyPlugin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_plugin);


        findViewById(R.id.initialize_btn).setOnClickListener(this);
        findViewById(R.id.get_containment_zone_btn).setOnClickListener(this);
        startStopSafetyPlugin = findViewById(R.id.start_safety_plugin_btn);
        startStopSafetyPlugin.setOnClickListener(this);
        txtInsideZone = findViewById(R.id.txt_inside_zone);
        txtZoneName = findViewById(R.id.txt_zone_name);
        txtDistance = findViewById(R.id.txt_distance);
        txtMapLink = findViewById(R.id.txt_map_link);
        txtDistrictName = findViewById(R.id.txt_district_name);
        txtZoneType = findViewById(R.id.txt_zone_type);
        findViewById(R.id.initialize_btn).setEnabled(!MapmyIndiaSafetyPlugin.getInstance().isInitialized());
        startStopSafetyPlugin.setText(!MapmyIndiaSafetyPlugin.getInstance().isRunning() ? R.string.txt_start_safety_plugin : R.string.txt_stop_safety_plugin);


    }

    private void reset() {
        txtInsideZone.setText("Inside Containment Zone: ");
        txtDistance.setText("Nearest zone distance: ");
        txtMapLink.setText("Containment Zone Link: ");
        txtDistrictName.setText("District Name: ");
        txtZoneType.setText("Zone Type: ");
    }

    private String getDistanceFormat(long distance) {
        if (distance >= 1000) {
            double dist = (distance / 1000.0);
            String distFinal = new DecimalFormat("#.0").format(dist);
            return distFinal + "Km(s)";
        } else {
            int dist = (int) (distance);
            return this.getResources().getQuantityString(R.plurals.distance_meter, dist, dist);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.initialize_btn:

                MapmyIndiaSafetyPlugin.getInstance().initialize(new IAuthListener() {
                    @Override
                    public void onSuccess() {

                        Toast.makeText(SafetyPluginActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                        view.setEnabled(!MapmyIndiaSafetyPlugin.getInstance().isInitialized());
                    }

                    @Override
                    public void onError(String reason, String errorIdentifier, String errorDescription) {
                        Toast.makeText(SafetyPluginActivity.this, "onFail:- " + reason, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.get_containment_zone_btn:
                reset();
                MapmyIndiaSafetyPlugin.getInstance().getCurrentLocationSafety(new ISafetyListener() {
                    @Override
                    public void onResult(ContainmentZoneInfo zoneInfo) {
                        txtInsideZone.setText(String.format("Inside Containment Zone: %s", zoneInfo.isInsideContainmentZone()));
                        txtDistance.setText(String.format("Nearest zone distance: %s", getDistanceFormat(zoneInfo.getDistanceToNearestZone())));
                        txtMapLink.setText(String.format("Containment Zone Link: %s", zoneInfo.getMapLink()));
                        txtDistrictName.setText(String.format("District Name: %s", zoneInfo.getDistrictName()));
                        txtZoneType.setText(String.format("Zone Type: %s", zoneInfo.getZoneType()));

                    }

                    @Override
                    public void onError(String reason, String errorIdentifier, String errorDescription) {
                        Timber.e("OnError:" + reason + "errorIdentifier:- " + errorIdentifier);
                        Toast.makeText(SafetyPluginActivity.this, "" + errorDescription, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.start_safety_plugin_btn:

               
                reset();
                if (!MapmyIndiaSafetyPlugin.getInstance().isRunning()) {
                    MapmyIndiaSafetyPlugin.getInstance().startSafetyPlugin(new ISafetyListener() {
                        @Override
                        public void onResult(ContainmentZoneInfo zoneInfo) {
                            startStopSafetyPlugin.setText(!MapmyIndiaSafetyPlugin.getInstance().isRunning() ? R.string.txt_start_safety_plugin : R.string.txt_stop_safety_plugin);
                            txtInsideZone.setText(String.format("Inside Containment Zone: %s", zoneInfo.isInsideContainmentZone()));
                            txtDistance.setText(String.format("Nearest zone distance: %s", getDistanceFormat(zoneInfo.getDistanceToNearestZone())));
                            txtMapLink.setText(String.format("Containment Zone Link: %s", zoneInfo.getMapLink()));
                            txtDistrictName.setText(String.format("District Name: %s", zoneInfo.getDistrictName()));
                            txtZoneType.setText(String.format("Zone Type: %s", zoneInfo.getZoneType()));
                        }

                        @Override
                        public void onError(String reason, String errorIdentifier, String errorDescription) {
                            Timber.e("OnError:" + reason + "errorIdentifier:- " + errorIdentifier);
                        }
                    });
                } else {
                    MapmyIndiaSafetyPlugin.getInstance().stopSafetyPlugin();
                    startStopSafetyPlugin.setText(!MapmyIndiaSafetyPlugin.getInstance().isRunning() ? R.string.txt_start_safety_plugin : R.string.txt_stop_safety_plugin);

                }
                break;
        }
    }


}
