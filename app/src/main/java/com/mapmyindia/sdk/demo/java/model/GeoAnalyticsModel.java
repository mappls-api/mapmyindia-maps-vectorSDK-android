package com.mapmyindia.sdk.demo.java.model;

import com.mapmyindia.sdk.geoanalytics.MapmyIndiaGeoAnalyticsRequest;
import com.mapmyindia.sdk.geoanalytics.MapmyIndiaGeoAnalyticsType;

/**
 * * Created by Saksham on 02-06-2021.
 **/
public class GeoAnalyticsModel {

    private MapmyIndiaGeoAnalyticsType type;
    private MapmyIndiaGeoAnalyticsRequest params;
    private String geoboundType;
    private String[] geoBound;

    public GeoAnalyticsModel(MapmyIndiaGeoAnalyticsType type, MapmyIndiaGeoAnalyticsRequest params, String geoboundType, String[] geoBound) {
        this.type = type;
        this.params = params;
        this.geoboundType = geoboundType;
        this.geoBound = geoBound;
    }

    public MapmyIndiaGeoAnalyticsType getType() {
        return type;
    }

    public MapmyIndiaGeoAnalyticsRequest getParams() {
        return params;
    }

    public String getGeoboundType() {
        return geoboundType;
    }

    public String[] getGeoBound() {
        return geoBound;
    }
}
