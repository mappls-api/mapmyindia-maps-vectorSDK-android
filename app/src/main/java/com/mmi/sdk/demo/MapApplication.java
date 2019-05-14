package com.mmi.sdk.demo;

import android.app.Application;

import com.mapbox.mapboxsdk.MapmyIndia;
import com.mmi.services.account.MapmyIndiaAccountManager;

import timber.log.Timber;

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
        MapmyIndiaAccountManager.getInstance().setAtlasGrantType(getAtlasGrantType());
        MapmyIndia.getInstance(this);
    }

    String getMapSDKKey() {
        return "";
    }

    String getRestAPIKey() {
        return "";
    }

    private String getAtlasClientId() {
        return "";
    }

    private String getAtlasClientSecret() {
        return "";
    }

    private String getAtlasGrantType() {
        return "client_credentials";
    }


}
