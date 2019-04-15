package com.tatar.repofinder.util

import android.content.Context
import android.net.ConnectivityManager

class ConnectionManager(val context: Context) {

    fun hasInternetConnection(): Boolean {

        var isConnected: Boolean = false

        var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo = connectivityManager.activeNetworkInfo

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            isConnected = true
        }

        return isConnected
    }
}