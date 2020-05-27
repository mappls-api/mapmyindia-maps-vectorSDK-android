package com.mapmyindia.sdk.demo.kotlin.kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mapmyindia.sdk.demo.R
import com.mmi.services.api.nearby.model.NearbyAtlasResult
import java.util.*

/**
 * Created by CEINFO on 26-02-2019.
 */
class NearByAdapter(internal var list: ArrayList<NearbyAtlasResult>) : androidx.recyclerview.widget.RecyclerView.Adapter<NearByAdapter.NearByView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearByView {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.auto_suggest_adapter_row, parent, false)
        return NearByView(v)
    }

    override fun onBindViewHolder(holder: NearByView, position: Int) {
        holder.viewName.text = list[position].placeAddress
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class NearByView(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var viewName: TextView

        init {
            viewName = itemView.findViewById(R.id.textView)
        }
    }
}
