package com.brsv44n.some_courier.location.android

import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.brsv44n.some_courier.location.LocationSettingsClient

class AndroidLocationSettingsClient(
    context: Context
) : LocationSettingsClient {

    private val locationManager =
        ContextCompat.getSystemService(context, LocationManager::class.java).let(::requireNotNull)

    override suspend fun checkLocationSettings(): Result<Unit> {
        return if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Result.failure(GpsDisabledResolvableApiException())
        } else {
            Result.success(Unit)
        }
    }
}
