package com.mapmyindia.sdk.demo.kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mapmyindia.sdk.demo.R
import com.mmi.services.api.autosuggest.model.SuggestedSearchAtlas
import java.util.*

class AutoSuggestSearchesAdapter(private val list: ArrayList<SuggestedSearchAtlas>, private val suggestedSearches: SuggestedSearches) : RecyclerView.Adapter<AutoSuggestSearchesAdapter.MyViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.auto_suggest_adapter_row, parent, false)
        return MyViewholder(v)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        holder.viewName.text = list[position].location
        holder.viewName.setOnClickListener { suggestedSearches.hyperlinkData(list[position].hyperLink) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface SuggestedSearches {
        fun hyperlinkData(hyperlink: String?)
    }

    inner class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var viewName: TextView = itemView.findViewById(R.id.textView)

    }

}