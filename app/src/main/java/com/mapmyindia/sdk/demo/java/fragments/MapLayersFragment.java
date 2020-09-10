package com.mapmyindia.sdk.demo.java.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.activity.HeatMapActivity;
import com.mapmyindia.sdk.demo.java.activity.IndoorActivity;
import com.mapmyindia.sdk.demo.java.activity.InteractiveLayerActivity;
import com.mapmyindia.sdk.demo.java.activity.SafetyStripActivity;
import com.mapmyindia.sdk.demo.java.activity.ScalebarActivity;
import com.mapmyindia.sdk.demo.java.adapter.MapFeatureListAdapter;
import com.mapmyindia.sdk.demo.java.model.FeaturesList;

import java.util.ArrayList;

/**
 * * Created by Saksham on 04-09-2020.
 **/
public class MapLayersFragment extends Fragment {

    RecyclerView featuresRecycleView;
    MapFeatureListAdapter.OnClickListener onClickListener;
    ArrayList<FeaturesList> featuresArrayList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_mapfeatures_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        featuresRecycleView = view.findViewById(R.id.addfeaturesRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        featuresRecycleView.setLayoutManager(mLayoutManager);
        setList();
        MapFeatureListAdapter mapFeatureListAdapter= new MapFeatureListAdapter(featuresArrayList);
        featuresRecycleView.setAdapter(mapFeatureListAdapter);
        mapFeatureListAdapter.setOnClickListener(new MapFeatureListAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                if(position==0){
                    Intent heatMapIntent= new Intent(getContext(), HeatMapActivity.class);
                    startActivity(heatMapIntent);
                } else if(position==1){
                    Intent indoorIntent= new Intent(getContext(), IndoorActivity.class);
                    startActivity(indoorIntent);
                }
                else if(position==2){
                    Intent interactiveLayerIntent= new Intent(getContext(), InteractiveLayerActivity.class);
                    startActivity(interactiveLayerIntent);
                }
                else if(position==3){
                    Intent scaleBarIntent= new Intent(getContext(), ScalebarActivity.class);
                    startActivity(scaleBarIntent);
                } else if(position == 4) {
                    Intent safetyStripIntent = new Intent(getContext(), SafetyStripActivity.class);
                    startActivity(safetyStripIntent);
                }
            }
        });
    }

    private void setList() {
        featuresArrayList.add(new FeaturesList("Show Heatmap data", "Add a heatmap to visualize data"));
        featuresArrayList.add(new FeaturesList("Indoor", "Show indoor widget when focus on multi storey building"));
        featuresArrayList.add(new FeaturesList("Interactive Layer", "Show Interactive CORONA Layers on the map view"));
        featuresArrayList.add(new FeaturesList("Map Scalebar", "Add a scale bar on map view to determine distance based on zoom level"));
        featuresArrayList.add(new FeaturesList("Map Safety Strip", "To display a user's safety status for COVID-19 on a map"));



    }
}
