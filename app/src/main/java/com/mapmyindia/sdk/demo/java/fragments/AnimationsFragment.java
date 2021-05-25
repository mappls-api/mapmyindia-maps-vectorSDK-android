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
import com.mapmyindia.sdk.demo.java.activity.CarAnimationActivity;
import com.mapmyindia.sdk.demo.java.activity.MarkerRotationTransitionActivity;
import com.mapmyindia.sdk.demo.java.activity.TrackingActivity;
import com.mapmyindia.sdk.demo.java.adapter.MapFeatureListAdapter;
import com.mapmyindia.sdk.demo.java.model.FeaturesList;

import java.util.ArrayList;

public class AnimationsFragment extends Fragment {
    RecyclerView featuresRecycleView;
   // MapFeatureListAdapter.OnClickListener onClickListener;
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
        MapFeatureListAdapter mapFeatureListAdapter = new MapFeatureListAdapter(featuresArrayList);
        featuresRecycleView.setAdapter(mapFeatureListAdapter);
        mapFeatureListAdapter.setOnClickListener(new MapFeatureListAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                if (position == 0) {
                    Intent animateCarIntent = new Intent(getContext(), CarAnimationActivity.class);
                    startActivity(animateCarIntent);
                } else if (position == 1) {
                    Intent markerRototationIntent = new Intent(getContext(), MarkerRotationTransitionActivity.class);
                    startActivity(markerRototationIntent);
                } else if (position == 2) {
                    Intent trackingActivityIntent = new Intent(getContext(), TrackingActivity.class);
                    startActivity(trackingActivityIntent);
                }
            }
        });

    }
    private void setList() {
        featuresArrayList.add(new FeaturesList("Animate Car", "Animate a car marker on predefined route"));
        featuresArrayList.add(new FeaturesList("Marker Rotation and Transition", "Rotate a marker by given degree and animate the marker to a new position"));
        featuresArrayList.add(new FeaturesList("Tracking sample", "To track a vehicle on map"));
    }
}