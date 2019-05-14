package com.mmi.sdk.demo.java.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmi.sdk.demo.R;
import com.mmi.sdk.demo.java.model.Features;

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
        holder.viewName.setText(list.get(position).getFeatureTittle());
        holder.viewName.setOnClickListener(new View.OnClickListener() {
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

        TextView viewName;

        public MyViewHolder(View itemView) {
            super(itemView);
            viewName = itemView.findViewById(R.id.textView);
        }
    }
}
