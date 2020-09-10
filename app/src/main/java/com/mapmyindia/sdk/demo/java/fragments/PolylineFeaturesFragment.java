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
import com.mapmyindia.sdk.demo.java.activity.GradientPolylineActivity;
import com.mapmyindia.sdk.demo.java.activity.PolygonActivity;
import com.mapmyindia.sdk.demo.java.activity.PolylineActivity;
import com.mapmyindia.sdk.demo.java.activity.SemiCirclePolylineActivity;
import com.mapmyindia.sdk.demo.java.activity.SnakeMotionPolylineActivity;
import com.mapmyindia.sdk.demo.java.adapter.MapFeatureListAdapter;
import com.mapmyindia.sdk.demo.java.model.FeaturesList;

import java.util.ArrayList;

public class PolylineFeaturesFragment extends Fragment {
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
        MapFeatureListAdapter mapFeatureListAdapter = new MapFeatureListAdapter(featuresArrayList);
        featuresRecycleView.setAdapter(mapFeatureListAdapter);
        mapFeatureListAdapter.setOnClickListener(new MapFeatureListAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                if (position == 0) {
                    Intent addPolylineIntent = new Intent(getContext(), PolylineActivity.class);
                    startActivity(addPolylineIntent);
                } else if (position == 1) {
                    Intent addGradientPolylineIntent = new Intent(getContext(), GradientPolylineActivity.class);
                    startActivity(addGradientPolylineIntent);
                }else if (position == 2) {
                    Intent addSemiCirclePolyLineIntent = new Intent(getContext(), SemiCirclePolylineActivity.class);
                    startActivity(addSemiCirclePolyLineIntent);
                }
                else if (position == 3) {
                    Intent addSnakeMotionPolylineIntent = new Intent(getContext(), SnakeMotionPolylineActivity.class);
                    startActivity(addSnakeMotionPolylineIntent);
                }
                else if (position == 4) {
                    Intent addPolyGonIntent = new Intent(getContext(), PolygonActivity.class);
                    startActivity(addPolyGonIntent);
                }
            }
        });
    }
    private void setList() {
        featuresArrayList.add(new FeaturesList("Draw Polyline", "Draw a polyline with given list of latitude and longitude"));
        featuresArrayList.add(new FeaturesList("Polyline with Gradient color", "Draw a polyline with given list of latitude and longitude"));
        featuresArrayList.add(new FeaturesList("Semicircle polyline", "Draw a semicircle polyline on the map"));
       featuresArrayList.add(new FeaturesList("SnakeMotion Polyline","Draw a Snake Motion Polyline"));
        featuresArrayList.add(new FeaturesList("Draw Polygon ", "Draw a polygon on the map"));


    }
}
