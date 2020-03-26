package com.mapmyindia.sdk.demo.java.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapmyindia.sdk.demo.R;
import com.mmi.services.api.nearby.model.NearbyAtlasResult;

import java.util.ArrayList;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.NearByView> {

    ArrayList<NearbyAtlasResult> list;

    public NearByAdapter(ArrayList<NearbyAtlasResult> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NearByView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.auto_suggest_adapter_row, parent, false);
        return new NearByView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NearByView holder, int position) {
        holder.viewName.setText(list.get(position).getPlaceAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NearByView extends RecyclerView.ViewHolder {
        TextView viewName;

        public NearByView(View itemView) {
            super(itemView);
            viewName = itemView.findViewById(R.id.textView);
        }
    }
}
