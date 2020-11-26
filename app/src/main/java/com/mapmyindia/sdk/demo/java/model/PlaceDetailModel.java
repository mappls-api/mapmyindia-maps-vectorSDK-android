package com.mapmyindia.sdk.demo.java.model;

/**
 * * Created by Saksham on 26-11-2020.
 **/
public class PlaceDetailModel {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_HEADER = 1;

    private int type;
    private String title;
    private String value;

    public PlaceDetailModel(int type, String title, String value) {
        this.type = type;
        this.title = title;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
