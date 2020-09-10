package com.mapmyindia.sdk.demo.kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mapmyindia.sdk.demo.R
import com.mmi.services.api.alongroute.models.SuggestedPOI


/**
 * Created by CEINFO on 26-02-2019.
 */
class PoiAlongAdapter(var list: List<SuggestedPOI>) : RecyclerView.Adapter<PoiAlongAdapter.PoiAlongView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiAlongAdapter.PoiAlongView {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.auto_suggest_adapter_row, parent, false)
        return PoiAlongView(v)
    }

    override fun onBindViewHolder(holder: PoiAlongView, position: Int) {
        holder.viewName.text = list[position].address
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class PoiAlongView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var viewName: TextView = itemView.findViewById(R.id.textView)

    }

}