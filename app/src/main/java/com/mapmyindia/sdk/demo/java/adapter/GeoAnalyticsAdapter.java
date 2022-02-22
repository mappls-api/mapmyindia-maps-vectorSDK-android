package com.mapmyindia.sdk.demo.java.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.model.GeoAnalyticsModel;

import java.util.List;

public class GeoAnalyticsAdapter extends RecyclerView.Adapter<GeoAnalyticsAdapter.ViewHolder> {

    private List<GeoAnalyticsModel> geoAnalyticsModels;
    private OnLayerSelected onLayerSelected;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_geoanalytics_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GeoAnalyticsModel model = geoAnalyticsModels.get(position);
        holder.checkBox.setText(model.getType().getName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(onLayerSelected != null) {
                    onLayerSelected.onLayerSelected(geoAnalyticsModels.get(position), isChecked);
                }
            }
        });
    }

    public void setGeoAnalyticsModels(List<GeoAnalyticsModel> geoAnalyticsModels) {
        this.geoAnalyticsModels = geoAnalyticsModels;
        notifyDataSetChanged();
    }

    public void setOnLayerSelected(OnLayerSelected onLayerSelected) {
        this.onLayerSelected = onLayerSelected;
    }

    @Override
    public int getItemCount() {
        if(geoAnalyticsModels == null) {
            return 0;
        }
        return geoAnalyticsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SwitchMaterial checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cb_geoanalytics);
        }
    }
    public interface OnLayerSelected {
        void onLayerSelected(GeoAnalyticsModel geoAnalyticsModel, boolean isChecked);
    }
}
