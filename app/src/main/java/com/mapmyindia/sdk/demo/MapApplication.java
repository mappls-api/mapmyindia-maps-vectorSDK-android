package com.mapmyindia.sdk.demo;

import android.app.Application;

import com.mapbox.mapboxsdk.MapmyIndia;
import com.mapbox.mapboxsdk.module.http.HttpRequestUtil;
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
      HttpRequestUtil.setLogEnabled(true);
      HttpRequestUtil.setPrintRequestUrlOnFailure(true);
    }


  public String getAtlasClientId() {
    return "SzvSlV-lKpZYFvG2Z_6NKQ0RzHFEPAMQNVaeHCzWARnG1U3bMVuXAToRv6J1hkGwegy3xeUIuoAvpwNUSBC8e3Bz5WyThCwo";
  }

  public String getAtlasClientSecret() {
    return "QkUOZ5yFbmuIhOzVsd_dE9BFy_KWpEnyMSIi962k0r32GQzf6JA6Ik2tC9k9GDtUPFHs_zv78X6mlVjnCmTkKd-EtxmN_onUe9WaSYfWVUQ=";
  }


  public String getAtlasGrantType() {
    return "client_credentials";
  }

  public String getMapSDKKey() {
    return "dd59538de9aa0f97ba171a1d4cb293bc";
  }

  public String getRestAPIKey() {
    return "a40738c26b1e79b0c73e887628a2d75f";
  }


}
