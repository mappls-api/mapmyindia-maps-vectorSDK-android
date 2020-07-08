package com.mapmyindia.sdk.demo.kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.model.GeofenceDetail

class GeoFenceDetailAdapter : RecyclerView.Adapter<GeoFenceDetailAdapter.ViewHolder>() {
    private var geofenceDetailList: List<GeofenceDetail>? = null
    private var onGeoFenceChangeListener: OnGeoFenceChangeListener? = null


    fun setGeofenceDetailList(geofenceDetailList: List<GeofenceDetail>?) {
        this.geofenceDetailList = geofenceDetailList
        notifyDataSetChanged()
    }

    fun setOnGeoFenceChangeListener(onGeoFenceChangeListener: OnGeoFenceChangeListener?) {
        this.onGeoFenceChangeListener = onGeoFenceChangeListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_geofence_detail_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return geofenceDetailList?.size?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val geofenceDetail = geofenceDetailList!![position]
        holder.tvLabel.setText(geofenceDetail.gfLabel)
        holder.swStatus.isChecked = geofenceDetail.active
        holder.ivEdit.setOnClickListener { onGeoFenceChangeListener?.onEditGeoFence(geofenceDetail) }
        holder.ivDelete.setOnClickListener {
            onGeoFenceChangeListener?.onRemoveGeofence(
                    geofenceDetail
            )
        }
        holder.swStatus.setOnCheckedChangeListener { buttonView, isChecked ->
            geofenceDetailList!![position].active = isChecked
            holder.swStatus.post { notifyDataSetChanged() }
            onGeoFenceChangeListener?.onGeoFenceStatusChange()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val swStatus: SwitchCompat
        val tvLabel: TextView
        val ivEdit: ImageView
        val ivDelete: ImageView

        init {
            swStatus = itemView.findViewById(R.id.sw_status)
            tvLabel = itemView.findViewById(R.id.tv_geofence_label)
            ivEdit = itemView.findViewById(R.id.iv_edit_geofence)
            ivDelete = itemView.findViewById(R.id.iv_delete_geofence)
        }

    }

    interface OnGeoFenceChangeListener {
        fun onGeoFenceStatusChange()
        fun onEditGeoFence(geofenceDetail: GeofenceDetail?)
        fun onRemoveGeofence(geofenceDetail: GeofenceDetail?)
    }
}