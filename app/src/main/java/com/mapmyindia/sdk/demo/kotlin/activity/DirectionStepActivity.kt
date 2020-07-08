package com.mapmyindia.sdk.demo.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mapbox.geojson.Point
import com.mapmyindia.sdk.demo.R
import com.mapmyindia.sdk.demo.kotlin.adapter.StepsAdapter
import com.mmi.services.api.directions.DirectionsCriteria
import com.mmi.services.api.directions.MapmyIndiaDirections
import com.mmi.services.api.directions.models.DirectionsResponse
import com.mmi.services.api.directions.models.LegStep
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DirectionStepActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direction_step)
        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        MapmyIndiaDirections.builder()
                .origin(Point.fromLngLat(73.041932, 19.018686))
                .destination(Point.fromLngLat(73.040028, 19.019499))
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .resource(DirectionsCriteria.RESOURCE_ROUTE)
                .steps(true)
                .alternatives(false)
                .overview(DirectionsCriteria.OVERVIEW_FULL).build().enqueueCall(object : Callback<DirectionsResponse> {
                    override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                val directionsResponse = response.body()
                                val results = directionsResponse?.routes()
                                if (results?.size ?: 0 > 0) {
                                    val routeLegList = results?.get(0)?.legs()
                                    val legSteps: MutableList<LegStep> = ArrayList()
                                    for (routeLeg in routeLegList!!) {
                                        legSteps.addAll(routeLeg.steps()!!)
                                    }
                                    if (legSteps.size > 0) {
                                        recyclerView.adapter = StepsAdapter(legSteps)
                                    }
                                }
                            }
                        }
                    }
                })
    }
}