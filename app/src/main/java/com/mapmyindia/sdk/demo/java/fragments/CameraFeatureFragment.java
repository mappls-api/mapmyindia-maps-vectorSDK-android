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
import com.mapmyindia.sdk.demo.java.activity.CameraActivity;
import com.mapmyindia.sdk.demo.java.activity.ELocCameraActivity;
import com.mapmyindia.sdk.demo.java.activity.LocationCameraActivity;
import com.mapmyindia.sdk.demo.java.adapter.MapFeatureListAdapter;
import com.mapmyindia.sdk.demo.java.model.FeaturesList;

import java.util.ArrayList;

public class CameraFeatureFragment extends Fragment {
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
                    Intent mapFragmentIntent = new Intent(getContext(), CameraActivity.class);
                    startActivity(mapFragmentIntent);
                } else if (position == 1) {
                    Intent mapClickIntent = new Intent(getContext(), LocationCameraActivity.class);
                    startActivity(mapClickIntent);
                } else if (position == 2) {
                    Intent mapClickIntent = new Intent(getContext(), ELocCameraActivity.class);
                    startActivity(mapClickIntent);
                }

            }
        });
    }

    private void setList() {
        featuresArrayList.add(new FeaturesList("Camera Features", "Animate, Move or Ease Camera Position"));
        featuresArrayList.add(new FeaturesList("Location Camera Options", "Long press on map and get Latitude Longitude"));
        featuresArrayList.add(new FeaturesList("Camera Features in ELoc", "Animate, Move or Ease Camera Position using eloc"));


    }
}
