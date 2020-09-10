package com.mapmyindia.sdk.demo.kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.java.model.FeaturesList

class MapFeatureListAdapter(private val featureList: List<FeaturesList>?, var mClickListener: AdapterOnClick? ) : RecyclerView.Adapter<MapFeatureListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapFeatureListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mapfeatures_fragment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return featureList!!.size
    }
//    fun setOnClickListener(onClickListener: OnClickListener?) {
//        this.onClickListener = onClickListener
//    }
    override fun onBindViewHolder(holder: MapFeatureListAdapter.ViewHolder, position: Int) {

        holder.featureTitle?.text = featureList?.get(position)?.featureTittle
        holder.featureDesc?.text = featureList?.get(position)?.featureDescription
        holder.linearLayout?.setOnClickListener {
                    mClickListener?.onClick(position)
                }
            }




    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var featureTitle: TextView?=null
        var featureDesc: TextView?=null
        var linearLayout: RelativeLayout?=null

        init {
            featureTitle = itemView.findViewById(R.id.tv_add_title)
            featureDesc = itemView.findViewById(R.id.tv_add_desc)
            linearLayout = itemView.findViewById(R.id.container_layout)
        }
    }

    interface AdapterOnClick {
        fun onClick(position: Int)
    }
}