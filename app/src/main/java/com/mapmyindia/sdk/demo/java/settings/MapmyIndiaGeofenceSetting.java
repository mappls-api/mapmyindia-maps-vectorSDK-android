package com.mapmyindia.sdk.demo.java.settings;

import android.graphics.Color;

public class MapmyIndiaGeofenceSetting {

    private static final MapmyIndiaGeofenceSetting INSTANCE= new MapmyIndiaGeofenceSetting();

    public static  MapmyIndiaGeofenceSetting getInstance(){
        return INSTANCE;
    }



    private boolean isDefault = true;
    private float circleOutlineWidth = 1;
    private int circleFillColor = Color.parseColor("#D81B60");
    private int circleFillOutlineColor =Color.parseColor("#511050");
    private int draggingLineColor =Color.parseColor("#000000");
    private int maxRadius = 1000;
    private int minRadius =25;
    private int polygonDrawingLineColor =Color.parseColor("#000000");
    private int polygonFillColor = Color.parseColor("#511050");
    private int polygonFillOutlineColor = Color.parseColor("#511050");
    private float polygonOutlineWidth = 1;
    private boolean simplifyWhenIntersectingPolygonDetected = false;
    private boolean isPolygon = false;
    private int seekbarPrimaryColor=Color.parseColor("#D81B60");
    private int seekbarSecondaryColor=Color.parseColor("#511050");
    private float seekbarCornerRadius=5;
    private boolean showSeekBar= true;

    public int getSeekbarPrimaryColor() {
        return seekbarPrimaryColor;
    }

    public void setSeekbarPrimaryColor(int seekbarPrimaryColor) {
        this.seekbarPrimaryColor = seekbarPrimaryColor;
    }

    public int getSeekbarSecondaryColor() {
        return seekbarSecondaryColor;
    }

    public void setSeekbarSecondaryColor(int seekbarSecondaryColor) {
        this.seekbarSecondaryColor = seekbarSecondaryColor;
    }

    public float getSeekbarCornerRadius() {
        return seekbarCornerRadius;
    }

    public void setSeekbarCornerRadius(float seekbarCornerRadius) {
        this.seekbarCornerRadius = seekbarCornerRadius;
    }

    public boolean isShowSeekBar() {
        return showSeekBar;
    }

    public void setShowSeekBar(boolean showSeekBar) {
        this.showSeekBar = showSeekBar;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
    public float getCircleOutlineWidth() {
        return circleOutlineWidth;
    }

    public void setCircleOutlineWidth(float circleOutlineWidth) {
        this.circleOutlineWidth = circleOutlineWidth;
    }

    public int getCircleFillColor() {
        return circleFillColor;
    }

    public void setCircleFillColor(int circleFillColor) {
        this.circleFillColor = circleFillColor;
    }

    public int getCircleFillOutlineColor() {
        return circleFillOutlineColor;
    }

    public void setCircleFillOutlineColor(int circleFillOutlineColor) {
        this.circleFillOutlineColor = circleFillOutlineColor;
    }

    public int getDraggingLineColor() {
        return draggingLineColor;
    }

    public void setDraggingLineColor(int draggingLineColor) {
        this.draggingLineColor = draggingLineColor;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }

    public int getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(int minRadius) {
        this.minRadius = minRadius;
    }

    public int getPolygonDrawingLineColor() {
        return polygonDrawingLineColor;
    }

    public void setPolygonDrawingLineColor(int polygonDrawingLineColor) {
        this.polygonDrawingLineColor = polygonDrawingLineColor;
    }

    public int getPolygonFillColor() {
        return polygonFillColor;
    }

    public void setPolygonFillColor(int polygonFillColor) {
        this.polygonFillColor = polygonFillColor;
    }

    public int getPolygonFillOutlineColor() {
        return polygonFillOutlineColor;
    }

    public void setPolygonFillOutlineColor(int polygonFillOutlineColor) {
        this.polygonFillOutlineColor = polygonFillOutlineColor;
    }

    public float getPolygonOutlineWidth() {
        return polygonOutlineWidth;
    }

    public void setPolygonOutlineWidth(float polygonOutlineWidth) {
        this.polygonOutlineWidth = polygonOutlineWidth;
    }

    public boolean isSimplifyWhenIntersectingPolygonDetected() {
        return simplifyWhenIntersectingPolygonDetected;
    }

    public void setSimplifyWhenIntersectingPolygonDetected(boolean simplifyWhenIntersectingPolygonDetected) {
        this.simplifyWhenIntersectingPolygonDetected = simplifyWhenIntersectingPolygonDetected;
    }


    public boolean isPolygon() {
        return isPolygon;
    }

    public void setPolygon(boolean polygon) {
        isPolygon = polygon;
    }
}
