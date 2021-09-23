package com.mapmyindia.sdk.demo.java.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.adapter.StepsAdapter;
import com.mapmyindia.sdk.geojson.Point;
import com.mmi.services.api.OnResponseCallback;
import com.mmi.services.api.directions.DirectionsCriteria;
import com.mmi.services.api.directions.MapmyIndiaDirectionManager;
import com.mmi.services.api.directions.MapmyIndiaDirections;
import com.mmi.services.api.directions.models.DirectionsResponse;
import com.mmi.services.api.directions.models.DirectionsRoute;
import com.mmi.services.api.directions.models.LegStep;
import com.mmi.services.api.directions.models.RouteLeg;

import java.util.ArrayList;
import java.util.List;

public class DirectionStepActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_step);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MapmyIndiaDirections mapmyIndiaDirections = MapmyIndiaDirections.builder()
                .origin(Point.fromLngLat(73.041932, 19.018686))
                .destination(Point.fromLngLat(73.040028, 19.019499))
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .resource(DirectionsCriteria.RESOURCE_ROUTE)
                .steps(true)
                .alternatives(false)
                .overview(DirectionsCriteria.OVERVIEW_FULL).build();
        MapmyIndiaDirectionManager.newInstance(mapmyIndiaDirections).call(new OnResponseCallback<DirectionsResponse>() {
            @Override
            public void onSuccess(DirectionsResponse directionsResponse) {
                if (directionsResponse != null) {
                    List<DirectionsRoute> results = directionsResponse.routes();

                    if (results.size() > 0) {
                        List<RouteLeg> routeLegList = results.get(0).legs();
                        List<LegStep> legSteps = new ArrayList<>();
                        for (RouteLeg routeLeg : routeLegList) {
                            legSteps.addAll(routeLeg.steps());
                        }
                        if (legSteps != null && legSteps.size() > 0) {
                            recyclerView.setAdapter(new StepsAdapter(legSteps));
                        }
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }
}
