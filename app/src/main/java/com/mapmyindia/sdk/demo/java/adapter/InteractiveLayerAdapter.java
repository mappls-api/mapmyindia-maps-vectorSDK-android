package com.mapmyindia.sdk.demo.java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.maps.InteractiveLayer;

import java.util.List;

public class InteractiveLayerAdapter extends RecyclerView.Adapter<InteractiveLayerAdapter.ViewHolder> {

    private List<InteractiveLayer> covidLayers;
    private OnLayerSelected onLayerSelected;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_interactive_layer_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.checkBox.setText(covidLayers.get(position).getName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(onLayerSelected != null) {
                    onLayerSelected.onLayerSelected(covidLayers.get(position), isChecked);
                }
            }
        });
    }
    public void setCovidLayers(List<InteractiveLayer> covidLayers) {
        this.covidLayers = covidLayers;
        notifyDataSetChanged();
    }

    public void setOnLayerSelected(OnLayerSelected onLayerSelected) {
        this.onLayerSelected = onLayerSelected;
    }

    @Override
    public int getItemCount() {
        if(covidLayers == null) {
            return 0;
        }
        return covidLayers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cb_interactive_layer);
        }
    }
    public interface OnLayerSelected {
        void onLayerSelected(InteractiveLayer covidLayer, boolean isSelected);
    }
}
