package com.brsv44n.some_courier.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat
import com.brsv44n.some_courier.core.network.NetworkStateChecker
import me.tatarka.inject.annotations.Inject

@Inject
class AndroidNetworkStateChecker(
    private val context: Context,
) : NetworkStateChecker {

    override fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            ContextCompat.getSystemService(context, ConnectivityManager::class.java)
        return checkMAndAbove(requireNotNull(connectivityManager))
    }

    private fun checkMAndAbove(connectivityManager: ConnectivityManager): Boolean {
        val networkCapabilities = connectivityManager.activeNetwork
            ?.let { connectivityManager.getNetworkCapabilities(it) } ?: return false
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}
