package com.brsv44n.some_courier.location.google

import android.content.Context
import com.brsv44n.some_courier.location.LocationSettingsClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GoogleLocationSettingsClient(
    context: Context
) : LocationSettingsClient {

    private val settingsClient = LocationServices.getSettingsClient(context)

    override suspend fun checkLocationSettings(): Result<Unit> {
        return suspendCoroutine { continuation ->
            settingsClient.checkLocationSettings(
                LocationSettingsRequest.Builder().addLocationRequest(
                    LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        1000L
                    ).build()
                ).build()
            ).addOnFailureListener {
                if (it is ResolvableApiException) {
                    continuation.resume(Result.failure(GoogleResolvableException(it)))
                } else {
                    continuation.resume(Result.failure(it))
                }
            }.addOnSuccessListener {
                continuation.resume(Result.success(Unit))
            }
        }
    }
}
