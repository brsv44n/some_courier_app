package com.brsv44n.some_courier.location.huawei

import android.Manifest
import android.content.Context
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.brsv44n.some_courier.location.CoroutineLocationProvider
import com.brsv44n.some_courier.core.domain.entities.PointEntity
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationResult
import com.huawei.hms.location.LocationServices
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import me.tatarka.inject.annotations.Inject

@Inject
class HuaweiLocationProvider(
    private val context: Context
) : CoroutineLocationProvider {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @RequiresPermission(value = Manifest.permission.ACCESS_FINE_LOCATION)
    override fun locationFlow(interval: Long): Flow<PointEntity> {
        val locationRequest = LocationRequest.create().setInterval(interval)
        return callbackFlow {
            val locationListener = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.locations.firstOrNull()?.let {
                        trySend(PointEntity(it.latitude, it.longitude))
                    }
                }

            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationListener,
                Looper.getMainLooper()
            )
            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationListener)
            }
        }
    }
}
