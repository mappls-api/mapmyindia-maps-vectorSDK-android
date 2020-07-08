package com.mapmyindia.sdk.demo.java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapmyindia.sdk.demo.R;
import com.mmi.services.api.autosuggest.model.ELocation;

import java.util.ArrayList;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class AutoSuggestAdapter extends RecyclerView.Adapter<AutoSuggestAdapter.MyViewholder> {
    private ArrayList<ELocation> list;
    private PlaceData placeData;

    public AutoSuggestAdapter(ArrayList<ELocation> list, PlaceData placeData) {
        this.list = list;
        this.placeData = placeData;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.auto_suggest_adapter_row, parent, false);
        return new MyViewholder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, final int position) {
        holder.viewName.setText(list.get(position).placeName);

        holder.viewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ELocation eLocation=new ELocation();
                eLocation.latitude=list.get(position).latitude;
                eLocation.longitude=list.get(position).longitude;
                eLocation.placeName=list.get(position).placeName;

                placeData.dataOfPlace(eLocation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface PlaceData {
        void dataOfPlace(ELocation eLocation);
    }

    class MyViewholder extends RecyclerView.ViewHolder {

        TextView viewName;

        public MyViewholder(View itemView) {
            super(itemView);
            viewName = itemView.findViewById(R.id.textView);
        }
    }
}
