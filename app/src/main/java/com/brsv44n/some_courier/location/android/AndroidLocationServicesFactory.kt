package com.brsv44n.some_courier.location.android

import android.content.Context
import com.brsv44n.some_courier.location.CoroutineLocationProvider
import com.brsv44n.some_courier.location.LocationServicesAbstractFactory
import com.brsv44n.some_courier.location.LocationSettingsClient

class AndroidLocationServicesFactory(
    private val context: Context
) : LocationServicesAbstractFactory {

    private val locationProvider by lazy {
        AndroidLocationProvider(context)
    }

    private val settingsClient by lazy {
        AndroidLocationSettingsClient(context)
    }

    override fun provideLocationProvider(): CoroutineLocationProvider = locationProvider

    override fun provideSettingsClient(): LocationSettingsClient = settingsClient
}
