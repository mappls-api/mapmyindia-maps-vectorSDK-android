package com.mapmyindia.sdk.demo.kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.model.GeoAnalyticsModel


/**
 * Created by Saksham on 27-09-2021
 */

class GeoAnalyticsAdapter: RecyclerView.Adapter<GeoAnalyticsAdapter.ViewHolder>() {

    private var onLayerSelected: OnLayerSelected? = null
    private var geoAnalyticsModels: List<GeoAnalyticsModel>? = null;


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_geoanalytics_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = geoAnalyticsModels!![position]
        holder.checkBox.text = model.type.getName()
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onLayerSelected?.onLayerSelected(
                model,
                isChecked
            )
        }
    }

    override fun getItemCount(): Int {
        return geoAnalyticsModels?.size?:0
    }

    fun setGeoAnalyticsModels(geoAnalyticsModels: List<GeoAnalyticsModel>) {
        this.geoAnalyticsModels = geoAnalyticsModels
        notifyDataSetChanged()
    }

    fun setOnLayerSelected(onLayerSelected: OnLayerSelected?) {
        this.onLayerSelected = onLayerSelected
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: SwitchMaterial = itemView.findViewById(R.id.cb_geoanalytics)

    }

    interface OnLayerSelected {
        fun onLayerSelected(geoAnalyticsModel: GeoAnalyticsModel, isSelected: Boolean)
    }

}