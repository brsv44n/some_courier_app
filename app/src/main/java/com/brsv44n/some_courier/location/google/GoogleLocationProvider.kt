package com.brsv44n.some_courier.location.google

import android.Manifest
import android.content.Context
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.brsv44n.some_courier.location.CoroutineLocationProvider
import com.brsv44n.some_courier.core.domain.entities.PointEntity
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import me.tatarka.inject.annotations.Inject

@Inject
class GoogleLocationProvider(
    private val context: Context
) : CoroutineLocationProvider {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @RequiresPermission(value = Manifest.permission.ACCESS_FINE_LOCATION)
    override fun locationFlow(interval: Long): Flow<PointEntity> {
        val locationRequest = LocationRequest.Builder(interval).build()
        return callbackFlow {
            val locationListener =
                LocationListener { p0 -> trySend(PointEntity(p0.latitude, p0.longitude)) }
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
