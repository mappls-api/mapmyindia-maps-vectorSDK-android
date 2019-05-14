package com.mmi.sdk.demo.kotlin.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by CEINFO on 26-02-2019.
 */
class CheckInternet {
    companion object {
        private fun isNetworkAvailable(ctx: Context): Boolean {
            val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }
    }

}