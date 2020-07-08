package com.mapmyindia.sdk.demo.kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.plugin.directions.DirectionsUtils
import com.mapmyindia.sdk.plugin.directions.ManeuverUtils
import com.mapmyindia.sdk.plugin.directions.view.ManeuverView
import com.mmi.services.api.directions.models.LegStep
import java.text.DecimalFormat

class StepsAdapter(private val legSteps: List<LegStep>?) : RecyclerView.Adapter<StepsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adpter_steps, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return legSteps?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.stepsText.setText(DirectionsUtils.getTextInstructions(legSteps?.get(position)))
        holder.maneuverView.setManeuverTypeAndModifier(legSteps?.get(position)?.maneuver()?.type()!!, legSteps?.get(position)?.maneuver()?.modifier())
//
        val type = legSteps[position].maneuver().type()
        if (type != null) {
            if (type.equals("roundabout", ignoreCase = true) || type.equals("rotary", ignoreCase = true)) {
                if (legSteps.size > position + 1) {
                    holder.maneuverView.setRoundaboutAngle(ManeuverUtils.roundaboutAngle(legSteps[position], legSteps[position + 1]))
                } else {
                    holder.maneuverView.setRoundaboutAngle(ManeuverUtils.roundaboutAngle(legSteps[position], legSteps[position]))
                }
            }
        }

        holder.distanceText.text = String.format("GO  %s", convertMetersToText(legSteps[position].distance()))
    }


    private fun convertMetersToText(dist: Double): String {
        if (dist.toInt() <= 1000) {
            var distt: String = dist.toString()
            return if (distt.indexOf(".") > -1) {
                distt.substring(0, distt.indexOf(".")) + " mt"
            } else {
                "$distt mt"
            }
        } else {
            var distance: Double = (dist / 1000)
            val  df: DecimalFormat = DecimalFormat("#.#");
            distance = df.format(distance).toDouble()
            return "$distance km"
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var stepsText: TextView
        var distanceText: TextView
        var maneuverView: ManeuverView

        init {
            stepsText = itemView.findViewById(R.id.steps_text)
            distanceText = itemView.findViewById(R.id.distance_text)
            maneuverView = itemView.findViewById(R.id.navigate_icon)
        }
    }

}