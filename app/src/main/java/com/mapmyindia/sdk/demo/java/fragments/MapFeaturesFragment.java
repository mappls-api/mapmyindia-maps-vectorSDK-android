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
import com.mapmyindia.sdk.demo.java.activity.GesturesActivity;
import com.mapmyindia.sdk.demo.java.activity.MapClickActivity;
import com.mapmyindia.sdk.demo.java.activity.MapFragmentActivity;
import com.mapmyindia.sdk.demo.java.activity.MapLongClickActivity;
import com.mapmyindia.sdk.demo.java.activity.StyleActivity;
import com.mapmyindia.sdk.demo.java.adapter.MapFeatureListAdapter;
import com.mapmyindia.sdk.demo.java.model.FeaturesList;

import java.util.ArrayList;

public class MapFeaturesFragment extends Fragment {

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
                    Intent mapFragmentIntent= new Intent(getContext(), MapFragmentActivity.class);
                    startActivity(mapFragmentIntent);
                }
                else if(position==1){
                    Intent mapClickIntent= new Intent(getContext(), MapLongClickActivity.class);
                    startActivity(mapClickIntent);
                }else if(position==2){
                    Intent mapLongClickIntent= new Intent(getContext(), MapClickActivity.class);
                    startActivity(mapLongClickIntent);
                }
                else if(position==3){
                    Intent gestureIntent= new Intent(getContext(), GesturesActivity.class);
                    startActivity(gestureIntent);
                }
                else if(position==4){
                    Intent styleIntent= new Intent(getContext(), StyleActivity.class);
                    startActivity(styleIntent);
                }
            }
        });
    }

    private void setList() {
        featuresArrayList.add(new FeaturesList("Map Fragment", "Way to add Map in Fragment"));
        featuresArrayList.add(new FeaturesList("Map Long Click", "Location camera options for render and tracking modes"));
        featuresArrayList.add(new FeaturesList("Map Tap", "Long press on map and get Latitude Longitude"));
        featuresArrayList.add(new FeaturesList("Map Gestures", "Gestures detection for map view"));
        featuresArrayList.add(new FeaturesList("Map Styles", "To change and update MapmyIndia Styles"));
    }
}
