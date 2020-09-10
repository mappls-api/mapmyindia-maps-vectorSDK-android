package com.mapmyindia.sdk.demo.java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapmyindia.sdk.demo.R;
import com.mmi.services.api.autosuggest.model.SuggestedSearchAtlas;

import java.util.ArrayList;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class AutoSuggestSearchesAdapter extends RecyclerView.Adapter<AutoSuggestSearchesAdapter.MyViewholder> {
    private ArrayList<SuggestedSearchAtlas> list;
    private SuggestedSearches suggestedSearches;

    public   AutoSuggestSearchesAdapter(ArrayList<SuggestedSearchAtlas> list, SuggestedSearches suggestedSearches) {
        this.list = list;
        this.suggestedSearches = suggestedSearches;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.auto_suggest_adapter_row, parent, false);
        return new MyViewholder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, final int position) {
        holder.viewName.setText(list.get(position).location);

        holder.viewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestedSearches.hyperlinkData(list.get(position).hyperLink);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface SuggestedSearches {
        void hyperlinkData(String hyperlink);
    }

    class MyViewholder extends RecyclerView.ViewHolder {

        TextView viewName;

        public MyViewholder(View itemView) {
            super(itemView);
            viewName = itemView.findViewById(R.id.textView);
        }
    }
}
