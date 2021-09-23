package com.mapmyindia.sdk.demo.java.model;

import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.maps.geometry.LatLng;

import java.util.List;

public class GeofenceDetail {


    public static String TYPE_POLYGON = "POLYGON";
    public static String TYPE_CIRCLE = "CIRCLE";
    private String gfLabel;
    private String gfType;
    private Integer circleRadius;
    private boolean active;
    private LatLng circleCentre;
    private List<Point> polygonPoints = null;

    public String getGfLabel() {
        return gfLabel;
    }

    public void setGfLabel(String gfLabel) {
        this.gfLabel = gfLabel;
    }

    public String getGfType() {
        return gfType;
    }

    public void setGfType(String gfType) {
        this.gfType = gfType;
    }

    public Integer getCRadius() {
        return circleRadius;
    }

    public void setCRadius(Integer cRadius) {
        this.circleRadius = cRadius;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public LatLng getCircleCentre() {
        return circleCentre;
    }

    public void setCircleCentre(LatLng circleCentre) {
        this.circleCentre = circleCentre;
    }




    public List<Point> getGPS() {
        return polygonPoints;
    }

    public void setGPS(List<Point> gPS) {
        this.polygonPoints = gPS;
    }

}