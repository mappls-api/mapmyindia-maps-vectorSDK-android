package com.mapmyindia.sdk.demo.java.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by CEINFO on 26-02-2019.
 */
public class CheckInternet {
    /* function to check internet connection*/
    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
