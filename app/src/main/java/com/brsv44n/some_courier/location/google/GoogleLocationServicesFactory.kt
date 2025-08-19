package com.brsv44n.some_courier.location.google

import android.content.Context
import com.brsv44n.some_courier.location.CoroutineLocationProvider
import com.brsv44n.some_courier.location.LocationServicesAbstractFactory
import com.brsv44n.some_courier.location.LocationSettingsClient

class GoogleLocationServicesFactory(
    private val context: Context
) : LocationServicesAbstractFactory {

    private val locationProvider by lazy {
        GoogleLocationProvider(context)
    }

    private val settingsClient by lazy {
        GoogleLocationSettingsClient(context)
    }

    override fun provideLocationProvider(): CoroutineLocationProvider = locationProvider

    override fun provideSettingsClient(): LocationSettingsClient = settingsClient
}
