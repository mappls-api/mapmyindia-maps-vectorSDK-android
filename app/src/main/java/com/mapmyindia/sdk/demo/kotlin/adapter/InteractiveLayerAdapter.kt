package com.mapmyindia.sdk.demo.kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.mapbox.mapboxsdk.maps.InteractiveLayer
import com.mapmyindia.sdk.demo.R


class InteractiveLayerAdapter : RecyclerView.Adapter<InteractiveLayerAdapter.ViewHolder>() {
    private var interactiveLayers: List<InteractiveLayer>? = null
    private var onLayerSelected: OnLayerSelected? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_interactive_layer_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkBox.text = interactiveLayers!![position].name
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            onLayerSelected?.onLayerSelected(interactiveLayers!![position], isChecked)

        }
    }

    fun setCovidLayers(interactiveLayers: List<InteractiveLayer>?) {
        this.interactiveLayers = interactiveLayers
        notifyDataSetChanged()
    }

    fun setOnLayerSelected(onLayerSelected: OnLayerSelected?) {
        this.onLayerSelected = onLayerSelected
    }

    override fun getItemCount(): Int {
        return interactiveLayers?.size?:0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.cb_interactive_layer)

    }

    interface OnLayerSelected {
        fun onLayerSelected(interactiveLayer: InteractiveLayer?, isSelected: Boolean)
    }
}
