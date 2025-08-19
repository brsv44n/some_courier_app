package com.brsv44n.some_courier.location.huawei

import android.content.Context
import com.brsv44n.some_courier.location.CoroutineLocationProvider
import com.brsv44n.some_courier.location.LocationServicesAbstractFactory
import com.brsv44n.some_courier.location.LocationSettingsClient

class HuaweiLocationServicesFactory(
    private val context: Context
) : LocationServicesAbstractFactory {

    private val locationProvider by lazy {
        HuaweiLocationProvider(context)
    }

    private val settingsClient by lazy {
        HuaweiLocationSettingsClient(context)
    }

    override fun provideLocationProvider(): CoroutineLocationProvider = locationProvider

    override fun provideSettingsClient(): LocationSettingsClient = settingsClient
}
