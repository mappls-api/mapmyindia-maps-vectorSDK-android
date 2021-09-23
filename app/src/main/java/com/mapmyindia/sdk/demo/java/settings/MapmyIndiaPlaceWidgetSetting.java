package com.mapmyindia.sdk.demo.java.settings;

import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions;

/**
 * * Created by Saksham on 29-04-2021.
 **/
public class MapmyIndiaPlaceWidgetSetting {

    private static final MapmyIndiaPlaceWidgetSetting OUR_INSTANCE = new MapmyIndiaPlaceWidgetSetting();

    private boolean isDefault = true;
    private int signatureVertical = PlaceOptions.GRAVITY_TOP;
    private int signatureHorizontal = PlaceOptions.GRAVITY_LEFT;
    private int logoSize = PlaceOptions.SIZE_MEDIUM;
    private Point location;
    private String filter;
    private boolean enableHistory = false;
    private String pod;
    private String hint = "Search Here";
    private boolean enableTextSearch;
    private int backgroundColor= android.R.color.white;
    private int toolbarColor= android.R.color.white;

    public static MapmyIndiaPlaceWidgetSetting getInstance() {
        return OUR_INSTANCE;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
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

    public boolean isEnableHistory() {
        return enableHistory;
    }

    public void setEnableHistory(boolean enableHistory) {
        this.enableHistory = enableHistory;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public boolean isEnableTextSearch() {
        return enableTextSearch;
    }

    public void setEnableTextSearch(boolean enableTextSearch) {
        this.enableTextSearch = enableTextSearch;
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
}
