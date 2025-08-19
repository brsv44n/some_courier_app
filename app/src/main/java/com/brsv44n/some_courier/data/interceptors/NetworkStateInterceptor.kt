package com.brsv44n.some_courier.data.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.brsv44n.some_courier.core.di.annotations.Singleton
import com.brsv44n.some_courier.core.network.error.NetworkUnavailable
import me.tatarka.inject.annotations.Inject
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
@Inject
class NetworkStateInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) throw NetworkUnavailable()
        return chain.proceed(chain.request())
    }

    private fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return checkMAndAbove(cm)
    }

    @Suppress("ReturnCount")
    private fun checkMAndAbove(connectivityManager: ConnectivityManager): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}
