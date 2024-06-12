package com.huyhieu.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkUtils {

    /**
     * Listen for network changes
     */
    private var networkCallback: NetworkCallback? = null
    fun requestNetworkCallback(context: Context) {
        networkCallback = NetworkCallback()
        networkCallback?.requestNetwork(context)
    }

    fun unregisterNetworkCallback() {
        networkCallback?.unregisterNetworkCallback()
    }

    val isNetworkAvailable get() = networkCallback?.isAvailable() ?: false

    /**Check for available networks without listening for network changes*/
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}