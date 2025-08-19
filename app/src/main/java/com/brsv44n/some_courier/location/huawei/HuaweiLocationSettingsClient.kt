package com.brsv44n.some_courier.location.huawei

import android.content.Context
import com.brsv44n.some_courier.location.LocationSettingsClient
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationServices
import com.huawei.hms.location.LocationSettingsRequest
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HuaweiLocationSettingsClient(
    context: Context
) : LocationSettingsClient {

    private val settingsClient = LocationServices.getSettingsClient(context)

    override suspend fun checkLocationSettings(): Result<Unit> {
        return suspendCoroutine { continuation ->
            settingsClient.checkLocationSettings(
                LocationSettingsRequest.Builder().addLocationRequest(
                    LocationRequest()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(1000)
                ).build()
            ).addOnFailureListener {
                if (it is ResolvableApiException) {
                    continuation.resume(Result.failure(HuaweiResolvableException(it)))
                } else {
                    continuation.resume(Result.failure(it))
                }
            }.addOnSuccessListener {
                continuation.resume(Result.success(Unit))
            }
        }
    }
}
