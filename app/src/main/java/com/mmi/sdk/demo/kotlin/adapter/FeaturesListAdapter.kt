package com.mmi.sdk.demo.kotlin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mmi.sdk.demo.R
import com.mmi.sdk.demo.kotlin.model.Features
import java.util.*

/**
 * Created by CEINFO on 26-02-2019.
 */
abstract class FeaturesListAdapter(private val list: ArrayList<Features>) : RecyclerView.Adapter<FeaturesListAdapter.MyViewHolder>() {

    abstract fun redirectOnFeatureCallBack(features: Features)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.features_adapter_row, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.viewName.text = list[position].featureTittle
        holder.viewName.setOnClickListener { redirectOnFeatureCallBack(list[position]) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var viewName: TextView

        init {
            viewName = itemView.findViewById<View>(R.id.textView) as TextView
        }
    }
}
