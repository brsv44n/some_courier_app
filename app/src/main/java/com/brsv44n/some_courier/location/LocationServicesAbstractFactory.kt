package com.brsv44n.some_courier.location

interface LocationServicesAbstractFactory {

    fun provideLocationProvider(): CoroutineLocationProvider

    fun provideSettingsClient(): LocationSettingsClient
}
