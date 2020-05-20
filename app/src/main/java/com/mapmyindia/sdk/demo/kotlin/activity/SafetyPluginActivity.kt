package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.safety.MapmyIndiaSafetyPlugin
import com.mapmyindia.sdk.safety.callbacks.ISafetyListener
import com.mapmyindia.sdk.safety.containmentlayer.vo.ContainmentZoneInfo
import com.mapmyindia.sdk.safety.internal.callbacks.IAuthListener
import timber.log.Timber
import java.text.DecimalFormat

class SafetyPluginActivity : AppCompatActivity(), View.OnClickListener {
    private var txtInsideZone: TextView? = null
    private var txtZoneName: TextView? = null
    private var txtDistance: TextView? = null
    private var txtMapLink: TextView? = null
    private var txtDistrictName: TextView? = null
    private var txtZoneType: TextView? = null
    private var startStopSafetyPlugin: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safety_plugin)
        findViewById<View>(R.id.initialize_btn).setOnClickListener(this)
        findViewById<View>(R.id.get_containment_zone_btn).setOnClickListener(this)
        startStopSafetyPlugin = findViewById<Button>(R.id.start_safety_plugin_btn)
        startStopSafetyPlugin?.setOnClickListener(this)
        txtInsideZone = findViewById(R.id.txt_inside_zone)
        txtZoneName = findViewById(R.id.txt_zone_name)
        txtDistance = findViewById(R.id.txt_distance)
        txtMapLink = findViewById(R.id.txt_map_link)
        txtDistrictName = findViewById(R.id.txt_district_name)
        txtZoneType = findViewById(R.id.txt_zone_type)
        findViewById<View>(R.id.initialize_btn).isEnabled = !MapmyIndiaSafetyPlugin.getInstance().isInitialized
        startStopSafetyPlugin?.setText(if (!MapmyIndiaSafetyPlugin.getInstance().isRunning) R.string.txt_start_safety_plugin else R.string.txt_stop_safety_plugin)
    }

    private fun reset() {
        txtInsideZone?.text = "Inside Containment Zone: "
        txtDistance?.text = "Nearest zone distance: "
        txtMapLink?.text = "Containment Zone Link: "
        txtDistrictName?.text = "District Name: "
        txtZoneType?.text = "Zone Type: "
    }

    private fun getDistanceFormat(distance: Long): String {
        return if (distance >= 1000) {
            val dist = distance / 1000.0
            val distFinal = DecimalFormat("#.0").format(dist)
            distFinal + "Km(s)"
        } else {
            val dist = distance.toInt()
            this.resources.getQuantityString(R.plurals.distance_meter, dist, dist)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.initialize_btn -> MapmyIndiaSafetyPlugin.getInstance().initialize(object : IAuthListener {
                override fun onSuccess() {
                    Toast.makeText(this@SafetyPluginActivity, "onSuccess", Toast.LENGTH_SHORT).show()
                    view.isEnabled = !MapmyIndiaSafetyPlugin.getInstance().isInitialized
                }

                override fun onError(reason: String, errorIdentifier: String, errorDescription: String) {
                    Toast.makeText(this@SafetyPluginActivity, "onFail:- $reason", Toast.LENGTH_SHORT).show()
                }
            })
            R.id.get_containment_zone_btn -> {
                reset()
                MapmyIndiaSafetyPlugin.getInstance().getCurrentLocationSafety(object : ISafetyListener {
                    override fun onResult(zoneInfo: ContainmentZoneInfo) {
                        txtInsideZone!!.text = String.format("Inside Containment Zone: %s", zoneInfo.isInsideContainmentZone)
                        txtDistance!!.text = String.format("Nearest zone distance: %s", getDistanceFormat(zoneInfo.distanceToNearestZone))
                        txtMapLink!!.text = String.format("Containment Zone Link: %s", zoneInfo.mapLink)
                        txtDistrictName!!.text = String.format("District Name: %s", zoneInfo.districtName)
                        txtZoneType!!.text = String.format("Zone Type: %s", zoneInfo.zoneType)
                    }

                    override fun onError(reason: String, errorIdentifier: String, errorDescription: String) {
                        Timber.e("OnError:" + reason + "errorIdentifier:- " + errorIdentifier)
                        Toast.makeText(this@SafetyPluginActivity, "" + errorDescription, Toast.LENGTH_SHORT).show()
                    }
                })
            }
            R.id.start_safety_plugin_btn -> {
                reset()
                if (!MapmyIndiaSafetyPlugin.getInstance().isRunning) {
                    MapmyIndiaSafetyPlugin.getInstance().startSafetyPlugin(object : ISafetyListener {
                        override fun onResult(zoneInfo: ContainmentZoneInfo) {
                            startStopSafetyPlugin!!.setText(if (!MapmyIndiaSafetyPlugin.getInstance().isRunning) R.string.txt_start_safety_plugin else R.string.txt_stop_safety_plugin)
                            txtInsideZone!!.text = String.format("Inside Containment Zone: %s", zoneInfo.isInsideContainmentZone)
                            txtDistance!!.text = String.format("Nearest zone distance: %s", getDistanceFormat(zoneInfo.distanceToNearestZone))
                            txtMapLink!!.text = String.format("Containment Zone Link: %s", zoneInfo.mapLink)
                            txtDistrictName!!.text = String.format("District Name: %s", zoneInfo.districtName)
                            txtZoneType!!.text = String.format("Zone Type: %s", zoneInfo.zoneType)
                        }

                        override fun onError(reason: String, errorIdentifier: String, errorDescription: String) {
                            Timber.e("OnError:" + reason + "errorIdentifier:- " + errorIdentifier)
                        }
                    })
                } else {
                    MapmyIndiaSafetyPlugin.getInstance().stopSafetyPlugin()
                    startStopSafetyPlugin!!.setText(if (!MapmyIndiaSafetyPlugin.getInstance().isRunning) R.string.txt_start_safety_plugin else R.string.txt_stop_safety_plugin)
                }
            }
        }
    }
}