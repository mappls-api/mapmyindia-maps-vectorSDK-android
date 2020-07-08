package com.mapmyindia.sdk.demo.kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mapmyindia.sdk.demo.R
import com.mmi.services.api.autosuggest.model.ELocation
import java.util.*

/**
 * Created by CEINFO on 26-02-2019.
 */
class AutoSuggestAdapter(private var list: ArrayList<ELocation>,private var placeData: PlaceData) : androidx.recyclerview.widget.RecyclerView.Adapter<AutoSuggestAdapter.MyViewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.auto_suggest_adapter_row, parent, false)
        return MyViewholder(v)

    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        holder.viewName.text = list[position].placeName
        holder.viewName.setOnClickListener {
            val eLocation=ELocation()
            eLocation.latitude=list[position].latitude
            eLocation.longitude=list[position].longitude
            eLocation.placeName=list[position].placeName

            placeData.dataOfPlace(eLocation) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface PlaceData {
        fun dataOfPlace(eLocation: ELocation)
    }

    inner class MyViewholder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var viewName: TextView

        init {
            viewName = itemView.findViewById(R.id.textView)
        }
    }
}
