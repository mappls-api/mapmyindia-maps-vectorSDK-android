package com.mapmyindia.sdk.demo.java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapmyindia.sdk.demo.R;
import com.mmi.services.api.alongroute.models.SuggestedPOI;

import java.util.List;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class PoiAlongAdapter extends RecyclerView.Adapter<PoiAlongAdapter.PoiAlongView> {

    List<SuggestedPOI> list;

    public PoiAlongAdapter(List<SuggestedPOI> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public PoiAlongView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.auto_suggest_adapter_row, parent, false);
        return new PoiAlongView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PoiAlongView holder, int position) {
        holder.viewName.setText(list.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PoiAlongView extends RecyclerView.ViewHolder {
        TextView viewName;

        public PoiAlongView(View itemView) {
            super(itemView);
            viewName = itemView.findViewById(R.id.textView);
        }
    }
}
