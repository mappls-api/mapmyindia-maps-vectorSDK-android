package com.mapmyindia.sdk.demo.java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.model.Features;

import java.util.ArrayList;

/**
 * Created by CEINFO on 26-02-2019.
 */

public abstract class FeaturesListAdapter extends RecyclerView.Adapter<FeaturesListAdapter.MyViewHolder> {
    private ArrayList<Features> list;

    public FeaturesListAdapter(ArrayList<Features> list) {
        this.list = list;
    }

    public abstract void redirectOnFeatureCallBack(Features features);

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.features_adapter_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.titleTextView.setText(list.get(position).getFeatureTittle());
        holder.subTitleTextView.setText(list.get(position).getFeatureDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectOnFeatureCallBack(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView subTitleTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            subTitleTextView = itemView.findViewById(R.id.sub_title_text_view);
        }
    }
}
