package com.brsv44n.some_courier.location

interface LocationSettingsClient {

    suspend fun checkLocationSettings(): Result<Unit>
}
