package com.mapmyindia.sdk.demo.java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.model.FeaturesList;

import java.util.List;

public class MapFeatureListAdapter extends RecyclerView.Adapter<MapFeatureListAdapter.ViewHolder> {
    List<FeaturesList> featuresListList;


    OnClickListener onClickListener;


    public MapFeatureListAdapter(List<FeaturesList> featureLists) {
        this.featuresListList = featureLists;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mapfeatures_fragment, parent, false);
        return new ViewHolder(v);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.featureTitle.setText(featuresListList.get(position).getFeatureTittle());
        holder.featureDesc.setText(featuresListList.get(position).getFeatureDescription());
        holder.containerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return featuresListList != null ? featuresListList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView featureTitle;
        TextView featureDesc;
        RelativeLayout containerLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            featureTitle = itemView.findViewById(R.id.tv_add_title);
            featureDesc = itemView.findViewById(R.id.tv_add_desc);
            containerLayout= itemView.findViewById(R.id.container_layout);
        }
    }

    public interface OnClickListener {
        public void onClick(int position);
    }
}
