package com.mapmyindia.sdk.demo.java.settings;

import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions;

public class MapmyIndiaPlacePickerSetting {

    private static final MapmyIndiaPlacePickerSetting OUR_INSTANCE = new MapmyIndiaPlacePickerSetting();

    public static MapmyIndiaPlacePickerSetting getInstance(){
        return OUR_INSTANCE;
    }

    private boolean isDefault = true;
    private int pickerToolbarColor = android.R.color.white;
    private boolean includeDeviceLocation = true;
    private  boolean includeSearch = true;
    private Point location;
    private String filter;
    private boolean enableHistory = false;
    private String pod;
    private boolean tokenizeAddress = true;
    private int backgroundColor= android.R.color.white;
    private int toolbarColor= android.R.color.white;
    private String hint = "Search Here";
    private int signatureVertical = PlaceOptions.GRAVITY_TOP;
    private int signatureHorizontal = PlaceOptions.GRAVITY_LEFT;
    private int logoSize = PlaceOptions.SIZE_MEDIUM;
    private Integer historyCount;
    private Double zoom;


    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    public void setToolbarColor(int toolbarColor) {
        this.toolbarColor = toolbarColor;
    }

    public Double getZoom() {
        return zoom;
    }

    public void setZoom(Double zoom) {
        this.zoom = zoom;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public boolean isTokenizeAddress() {
        return tokenizeAddress;
    }

    public void setTokenizeAddress(boolean tokenizeAddress) {
        this.tokenizeAddress = tokenizeAddress;
    }

    public boolean isEnableHistory() {
        return enableHistory;
    }

    public void setEnableHistory(boolean enableHistory) {
        this.enableHistory = enableHistory;
    }

    public Integer getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(Integer historyCount) {
        this.historyCount = historyCount;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getSignatureVertical() {
        return signatureVertical;
    }

    public void setSignatureVertical(int signatureVertical) {
        this.signatureVertical = signatureVertical;
    }

    public int getSignatureHorizontal() {
        return signatureHorizontal;
    }

    public void setSignatureHorizontal(int signatureHorizontal) {
        this.signatureHorizontal = signatureHorizontal;
    }

    public int getLogoSize() {
        return logoSize;
    }

    public void setLogoSize(int logoSize) {
        this.logoSize = logoSize;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }


    public int getPickerToolbarColor() {
        return pickerToolbarColor;
    }

    public void setPickerToolbarColor(int pickerToolbarColor) {
        this.pickerToolbarColor = pickerToolbarColor;
    }


    public boolean isIncludeDeviceLocation() {
        return includeDeviceLocation;
    }

    public void setIncludeDeviceLocation(boolean includeDeviceLocation) {
        this.includeDeviceLocation = includeDeviceLocation;
    }

    public boolean isIncludeSearch() {
        return includeSearch;
    }

    public void setIncludeSearch(boolean includeSearch) {
        this.includeSearch = includeSearch;
    }
}
