package com.mapmyindia.sdk.demo.java.settings;

import com.mapmyindia.sdk.geojson.Point;
import com.mapmyindia.sdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mmi.services.api.directions.DirectionsCriteria;

import java.util.List;

public class MapmyIndiaDirectionWidgetSetting {
    private static final MapmyIndiaDirectionWidgetSetting INSTANCE= new MapmyIndiaDirectionWidgetSetting();

    public static MapmyIndiaDirectionWidgetSetting getInstance() {
        return INSTANCE;
    }


    private boolean isDefault = true;

    private boolean showAlternative = true;
    private boolean showStartNavigation= true;
    private boolean steps = true;
    private  String resource = DirectionsCriteria.RESOURCE_ROUTE;
    private String profile=DirectionsCriteria.PROFILE_DRIVING;
    private String overview =DirectionsCriteria.OVERVIEW_FULL;
    private List<String> excludes;
    private Point destination;

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
    private boolean isShowPOISearch = false;

    public Point getDestination() {
        return destination;
    }

    public void setDestination(Point destination) {
        this.destination = destination;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public boolean isShowAlternative() {
        return showAlternative;
    }

    public void setShowAlternative(boolean showAlternative) {
        this.showAlternative = showAlternative;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public boolean isSteps() {
        return steps;
    }

    public void setSteps(boolean steps) {
        this.steps = steps;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    public boolean isShowStartNavigation() {
        return showStartNavigation;
    }

    public void setShowStartNavigation(boolean showStartNavigation) {
        this.showStartNavigation = showStartNavigation;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
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

    public boolean isTokenizeAddress() {
        return tokenizeAddress;
    }

    public void setTokenizeAddress(boolean tokenizeAddress) {
        this.tokenizeAddress = tokenizeAddress;
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

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
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

    public Integer getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(Integer historyCount) {
        this.historyCount = historyCount;
    }

    public Double getZoom() {
        return zoom;
    }

    public void setZoom(Double zoom) {
        this.zoom = zoom;
    }

    public boolean isShowPOISearch() {
        return isShowPOISearch;
    }

    public void setShowPOISearch(boolean showPOISearch) {
        isShowPOISearch = showPOISearch;
    }
}
