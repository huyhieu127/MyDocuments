package com.huyhieu.mydocuments.repository.remote

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.huyhieu.mydocuments.libraries.utils.logDebug
import com.huyhieu.mydocuments.App

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
            logDebug("NetworkConfigs - onAvailable")
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val isUnmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            logDebug("NetworkConfigs - onCapabilitiesChanged - $isUnmetered")
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)
            logDebug("NetworkConfigs - onCapabilitiesChanged - $blocked")
        }

        override fun onUnavailable() {
            super.onUnavailable()
            logDebug("NetworkConfigs - onUnavailable")
        }

        // lost network connection
        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            logDebug("NetworkConfigs - onLosing")
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            isAvailable = false
            logDebug("NetworkConfigs - onLost")
        }
    }

    val connectivityManager by lazy { App.ins.getSystemService(ConnectivityManager::class.java) as ConnectivityManager }

    fun requestNetwork() {
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    fun getState() {
        logDebug("${connectivityManager.isDefaultNetworkActive} - ${connectivityManager.isActiveNetworkMetered}")
    }
}