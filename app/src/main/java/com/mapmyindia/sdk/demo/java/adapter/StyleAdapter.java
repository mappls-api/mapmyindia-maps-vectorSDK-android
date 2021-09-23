package com.mapmyindia.sdk.demo.java.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mapmyindia.sdk.demo.R;
import com.mapmyindia.sdk.demo.databinding.LayoutStyleAdapterBinding;
import com.mapmyindia.sdk.maps.style.model.MapmyIndiaStyle;

import java.util.List;

/**
 * Created by Saksham on 05-03-2021
 */

public class StyleAdapter extends RecyclerView.Adapter<StyleAdapter.ViewHolder> {

    private List<MapmyIndiaStyle> styleList;

    private OnStyleSelectListener onStyleSelectListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutStyleAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_style_adapter, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvName.setText(styleList.get(position).getDisplayName());
        holder.binding.tvDescription.setText(styleList.get(position).getDescription());
        Glide.with(holder.binding.getRoot().getContext()).load(styleList.get(position).getImageUrl()).into(holder.binding.ivImage);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onStyleSelectListener != null) {
                    onStyleSelectListener.onStyleSelected(styleList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return styleList == null? 0: styleList.size();
    }

    public void setStyleList(List<MapmyIndiaStyle> styleList) {
        this.styleList = styleList;
        notifyDataSetChanged();
    }

    public void setOnStyleSelectListener(OnStyleSelectListener onStyleSelectListener) {
        this.onStyleSelectListener = onStyleSelectListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutStyleAdapterBinding binding;

        public ViewHolder(@NonNull LayoutStyleAdapterBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface OnStyleSelectListener {
        void onStyleSelected(MapmyIndiaStyle style);
    }
}
