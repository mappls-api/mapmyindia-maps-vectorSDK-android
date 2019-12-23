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
        MapmyIndiaAccountManager.getInstance().setAtlasGrantType(getAtlasGrantType());
        MapmyIndia.getInstance(this);
    }

    private String getAtlasClientId() {
      return "KD98lApAgPU3F9rwBrx6neKKjZgI4OpoM--iaN8Nt1c=";
    }

    private String getAtlasClientSecret() {
      return "90pO9mH1t7Os22lNAKk4njqmBiGl49u-sRWYhoKOcEeTPc-ayohaKA==";
    }

    private String getAtlasGrantType() {
        return "client_credentials";
    }

  String getMapSDKKey() {

    return "ef50c498865ccc2e9b1407982c390511";

  }

  String getRestAPIKey() {

    return "72416e8fb4860ca6f742af1152324e4f";

  }


}
