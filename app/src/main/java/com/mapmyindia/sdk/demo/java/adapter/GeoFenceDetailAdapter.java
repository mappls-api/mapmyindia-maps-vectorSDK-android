package com.mapmyindia.sdk.demo.java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.model.GeofenceDetail;

import java.util.List;

public class GeoFenceDetailAdapter extends RecyclerView.Adapter<GeoFenceDetailAdapter.ViewHolder> {

    private List<GeofenceDetail> geofenceDetailList;
    private OnGeoFenceChangeListener onGeoFenceChangeListener;

    public void setGeofenceDetailList(List<GeofenceDetail> geofenceDetailList) {
        this.geofenceDetailList = geofenceDetailList;
        notifyDataSetChanged();
    }

    public void setOnGeoFenceChangeListener(OnGeoFenceChangeListener onGeoFenceChangeListener) {
        this.onGeoFenceChangeListener = onGeoFenceChangeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_geofence_detail_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GeofenceDetail geofenceDetail = geofenceDetailList.get(position);
        holder.tvLabel.setText(geofenceDetail.getGfLabel());
        holder.swStatus.setChecked(geofenceDetail.getActive());
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onGeoFenceChangeListener != null) {
                    onGeoFenceChangeListener.onEditGeoFence(geofenceDetail);
                }
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onGeoFenceChangeListener != null) {
                    onGeoFenceChangeListener.onRemoveGeofence(geofenceDetail);
                }
            }
        });
        holder.swStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                geofenceDetailList.get(position).setActive(isChecked);
                holder.swStatus.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
                if(onGeoFenceChangeListener != null) {
                    onGeoFenceChangeListener.onGeoFenceStatusChange();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return geofenceDetailList == null ?0: geofenceDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvLabel;
        private SwitchCompat swStatus;
        private ImageView ivEdit, ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tv_geofence_label);
            swStatus = itemView.findViewById(R.id.sw_status);
            ivEdit = itemView.findViewById(R.id.iv_edit_geofence);
            ivDelete = itemView.findViewById(R.id.iv_delete_geofence);
        }
    }

    public interface OnGeoFenceChangeListener {
        void onGeoFenceStatusChange();
        void onEditGeoFence(GeofenceDetail geofenceDetail);
        void onRemoveGeofence(GeofenceDetail geofenceDetail);
    }
}
