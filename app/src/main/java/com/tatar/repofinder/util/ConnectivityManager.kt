package com.tatar.repofinder.util

import android.content.Context
import android.net.ConnectivityManager

class ConnectivityManager(val context: Context) {

    fun hasInternetConnection(): Boolean {

        var isConnected: Boolean = false

        var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo = connectivityManager.activeNetworkInfo

        if (activeNetworkInfo.isConnected) {
            isConnected = true
        }

        return isConnected
    }
}