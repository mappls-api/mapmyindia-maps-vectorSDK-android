package com.mapmyindia.sdk.demo.java.settings;

import com.mapmyindia.sdk.drivingrange.DrivingRangeCriteria;
import com.mapmyindia.sdk.geojson.Point;

public final class MapmyIndiaDrivingRangeSetting {

    private static final MapmyIndiaDrivingRangeSetting OUR_INSTANCE = new MapmyIndiaDrivingRangeSetting();
    public static final int SPEED_TYPE_OPTIMAL = 0;
    public static final int SPEED_TYPE_PREDECTIVE = 1;
    public static final int PREDECTIVE_TYPE_CURRENT = 0;
    public static final int PREDECTIVE_TYPE_CUSTOM = 1;
    private MapmyIndiaDrivingRangeSetting(){}
    private Point location = Point.fromLngLat(77.218527, 28.632282);
    private boolean isUsingCurrentLocation = true;
    private String rangeType = DrivingRangeCriteria.RANGE_TYPE_TIME;
    private int contourValue = 50;
    private String contourColor = "ff0000";
    private String drivingProfile = "auto";
    private boolean showLocations = false;
    private boolean isForPolygon = true;
    private float denoise = 0.5f;
    private float generalize = 1.2f;
    private int speedType = SPEED_TYPE_OPTIMAL;
    private int predectiveType = PREDECTIVE_TYPE_CURRENT;
    private long time = System.currentTimeMillis();

    public static MapmyIndiaDrivingRangeSetting getInstance() {
        return OUR_INSTANCE;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public boolean isUsingCurrentLocation() {
        return isUsingCurrentLocation;
    }

    public void setUsingCurrentLocation(boolean usingCurrentLocation) {
        isUsingCurrentLocation = usingCurrentLocation;
    }

    public String getRangeType() {
        return rangeType;
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }

    public int getContourValue() {
        return contourValue;
    }

    public void setContourValue(int contourValue) {
        this.contourValue = contourValue;
    }

    public String getContourColor() {
        return contourColor;
    }

    public void setContourColor(String contourColor) {
        this.contourColor = contourColor;
    }

    public String getDrivingProfile() {
        return drivingProfile;
    }

    public void setDrivingProfile(String drivingProfile) {
        this.drivingProfile = drivingProfile;
    }

    public boolean isShowLocations() {
        return showLocations;
    }

    public void setShowLocations(boolean showLocations) {
        this.showLocations = showLocations;
    }

    public boolean isForPolygon() {
        return isForPolygon;
    }

    public void setForPolygon(boolean forPolygon) {
        isForPolygon = forPolygon;
    }

    public float getDenoise() {
        return denoise;
    }

    public void setDenoise(float denoise) {
        this.denoise = denoise;
    }

    public float getGeneralize() {
        return generalize;
    }

    public void setGeneralize(float generalize) {
        this.generalize = generalize;
    }

    public int getSpeedType() {
        return speedType;
    }

    public void setSpeedType(int speedType) {
        this.speedType = speedType;
    }

    public int getPredectiveType() {
        return predectiveType;
    }

    public void setPredectiveType(int predectiveType) {
        this.predectiveType = predectiveType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
