package com.mapmyindia.sdk.demo.kotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.databinding.LayoutStyleAdapterBinding
import com.mapmyindia.sdk.maps.style.model.MapmyIndiaStyle

/**
 ** Created by Saksham on 24-05-2021.
 **/
class StyleAdapter: RecyclerView.Adapter<StyleAdapter.ViewHolder>() {

    private var styleList: List<MapmyIndiaStyle>? = null
    private var onStyleSelectListener: OnStyleSelectListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LayoutStyleAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.layout_style_adapter, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return styleList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = styleList?.get(position)?.displayName
        holder.binding.tvDescription.text = styleList?.get(position)?.description
        Glide.with(holder.binding.root.context).load(styleList?.get(position)?.imageUrl).into(holder.binding.ivImage)
        holder.binding.root.setOnClickListener {
            onStyleSelectListener?.onStyleSelected(styleList?.get(position)!!)
        }

    }

    fun setStyleList(styles: List<MapmyIndiaStyle>) {
        styleList = styles
        notifyDataSetChanged()
    }

    fun setOnStyleSelectListener(onStyleSelectListener: OnStyleSelectListener) {
        this.onStyleSelectListener = onStyleSelectListener
    }

    inner class ViewHolder(val binding: LayoutStyleAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnStyleSelectListener {
        fun onStyleSelected(style: MapmyIndiaStyle)
    }
}