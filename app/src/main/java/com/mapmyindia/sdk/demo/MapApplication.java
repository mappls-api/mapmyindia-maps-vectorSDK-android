package com.mapmyindia.sdk.demo;

import android.app.Application;

import com.mapbox.mapboxsdk.MapmyIndia;
import com.mmi.services.account.MapmyIndiaAccountManager;

/**
 * Created by CEINFO on 29-06-2018.
 */

public class MapApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MapmyIndiaAccountManager.getInstance().setRestAPIKey(getRestAPIKey());
        MapmyIndiaAccountManager.getInstance().setMapSDKKey(getMapSDKKey());
        MapmyIndiaAccountManager.getInstance().setAtlasClientId(getAtlasClientId());
        MapmyIndiaAccountManager.getInstance().setAtlasClientSecret(getAtlasClientSecret());
        MapmyIndia.getInstance(this);
    }

    public String getAtlasClientId() {
        return "";
    }

    public String getAtlasClientSecret() {
        return "";
    }

    public String getMapSDKKey() {
        return "";
    }

    public String getRestAPIKey() {
        return "";
    }

}
