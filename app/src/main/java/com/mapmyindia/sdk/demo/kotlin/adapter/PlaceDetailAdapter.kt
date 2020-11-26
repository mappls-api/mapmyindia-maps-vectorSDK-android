package com.mapmyindia.sdk.demo.kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.model.PlaceDetailModel

/**
 ** Created by Saksham on 26-11-2020.
 **/
class PlaceDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var placeDetailModels: List<PlaceDetailModel>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == PlaceDetailModel.TYPE_HEADER) {
            val v: View = LayoutInflater.from(parent.context).inflate(R.layout.header_place_detail_layout, parent, false)
            HeaderViewHolder(v)
        } else {
            val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_place_detail_layout, parent, false)
            ItemViewHolder(v)
        }
    }

    fun setPlaceDetailModels(placeDetailModels: List<PlaceDetailModel>?) {
        this.placeDetailModels = placeDetailModels
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return placeDetailModels!![position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.tvTitle.text = placeDetailModels!![position].title
            holder.tvValue.text = placeDetailModels!![position].value
        } else {
            (holder as HeaderViewHolder).tvHeader.text = placeDetailModels!![position].title
        }
    }

    override fun getItemCount(): Int {
        return if (placeDetailModels == null) 0 else placeDetailModels!!.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvValue: TextView = itemView.findViewById(R.id.tv_value)

    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHeader: TextView = itemView.findViewById(R.id.tv_title)

    }
}
