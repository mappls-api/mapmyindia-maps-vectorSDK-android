package com.mapmyindia.sdk.demo.java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.java.model.PlaceDetailModel;

import java.util.List;

/**
 * * Created by Saksham on 26-11-2020.
 **/
public class PlaceDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PlaceDetailModel> placeDetailModels;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == PlaceDetailModel.TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_place_detail_layout, parent, false);
            return new HeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_detail_layout, parent, false);
            return new ItemViewHolder(v);
        }
    }

    public void setPlaceDetailModels(List<PlaceDetailModel> placeDetailModels) {
        this.placeDetailModels = placeDetailModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return placeDetailModels.get(position).getType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).tvTitle.setText(placeDetailModels.get(position).getTitle());
            ((ItemViewHolder) holder).tvValue.setText(placeDetailModels.get(position).getValue());
        } else {
            ((HeaderViewHolder)holder).tvHeader.setText(placeDetailModels.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return placeDetailModels ==null? 0:placeDetailModels.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvValue;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvValue = itemView.findViewById(R.id.tv_value);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tv_title);
        }
    }
}
