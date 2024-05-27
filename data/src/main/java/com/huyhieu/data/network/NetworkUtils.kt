package com.huyhieu.data.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

object NetworkUtils {

    var isAvailable = false

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isAvailable = true
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val isUnmetered =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            isAvailable = false
        }
    }

//    val connectivityManager by lazy { App.ins.getSystemService(ConnectivityManager::class.java) as ConnectivityManager }
//
//    fun requestNetwork() {
//        connectivityManager.requestNetwork(networkRequest, networkCallback)
//    }
//
//    fun getState() {
//        logDebug("${connectivityManager.isDefaultNetworkActive} - ${connectivityManager.isActiveNetworkMetered}")
//    }
}