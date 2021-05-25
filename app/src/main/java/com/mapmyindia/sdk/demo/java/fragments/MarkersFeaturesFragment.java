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
import com.mapmyindia.sdk.demo.java.activity.AddCustomInfoWindowActivity;
import com.mapmyindia.sdk.demo.java.activity.AddCustomMarkerActivity;
import com.mapmyindia.sdk.demo.java.activity.AddELocCustomMarkerActivity;
import com.mapmyindia.sdk.demo.java.activity.AddELocMarkerActivity;
import com.mapmyindia.sdk.demo.java.activity.AddMarkerActivity;
import com.mapmyindia.sdk.demo.java.activity.MarkerDraggingActivity;
import com.mapmyindia.sdk.demo.java.adapter.MapFeatureListAdapter;
import com.mapmyindia.sdk.demo.java.model.FeaturesList;

import java.util.ArrayList;

public class MarkersFeaturesFragment extends Fragment {

    RecyclerView featuresRecycleView;
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
                    Intent addMarkerIntent= new Intent(getContext(), AddMarkerActivity.class);
                    startActivity(addMarkerIntent);
                }
                else if(position==1){
                    Intent addCustomInfowindowIntent= new Intent(getContext(), AddCustomInfoWindowActivity.class);
                    startActivity(addCustomInfowindowIntent);
                }else if(position==2){
                    Intent addCustomMarkerIntent= new Intent(getContext(), AddCustomMarkerActivity.class);
                    startActivity(addCustomMarkerIntent);
                }
                else if(position==3){
                    Intent markerDraggingIntent= new Intent(getContext(), MarkerDraggingActivity.class);
                    startActivity(markerDraggingIntent);
                }
                else if(position==4){
                    Intent markerDraggingIntent= new Intent(getContext(), AddELocMarkerActivity.class);
                    startActivity(markerDraggingIntent);
                }
                else if(position==5){
                    Intent markerDraggingIntent= new Intent(getContext(), AddELocCustomMarkerActivity.class);
                    startActivity(markerDraggingIntent);
                }

            }
        });
    }
    private void setList() {
        featuresArrayList.add(new FeaturesList("Add Marker", "Way to add Map in Fragment"));
        featuresArrayList.add(new FeaturesList("Add Custom Infowindow", "Location camera options for render and tracking modes"));
        featuresArrayList.add(new FeaturesList("Add Custom Marker", "Long press on map and get Latitude Longitude"));
        featuresArrayList.add(new FeaturesList("Marker Dragging", "Drag a marker"));
        featuresArrayList.add(new FeaturesList("Add Marker Using ELoc", "Way to add marker using eLoc"));
        featuresArrayList.add(new FeaturesList("Add Custom Marker Using ELoc", "Way to add custom marker using eLoc"));

    }
}
