package com.brsv44n.some_courier.location.android

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.brsv44n.some_courier.location.CoroutineLocationProvider
import com.brsv44n.some_courier.core.domain.entities.PointEntity
import com.brsv44n.some_courier.core.utils.Optional
import com.brsv44n.some_courier.core.utils.asOptional
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import me.tatarka.inject.annotations.Inject

@Inject
class AndroidLocationProvider(
    private val context: Context
) : CoroutineLocationProvider {

    private val locationManager by lazy {
        ContextCompat.getSystemService(context, LocationManager::class.java).let(::requireNotNull)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun locationFlow(interval: Long): Flow<PointEntity> =
        merge(
            locationObservable(LocationManager.GPS_PROVIDER, interval),
            locationObservable(LocationManager.NETWORK_PROVIDER, interval)
        ).flatMapLatest {
            if (it.isPresent) {
                flowOf(PointEntity(it.get().latitude, it.get().longitude))
            } else {
                emptyFlow()
            }
        }

    @SuppressLint("MissingPermission")
    private fun locationObservable(
        provider: String,
        interval: Long
    ): Flow<Optional<Location>> {
        return callbackFlow {
            val listener = LocationListener { location ->
                trySend(location.asOptional()).isSuccess
            }
            locationManager.requestLocationUpdates(
                provider,
                interval,
                5f,
                listener
            )
            awaitClose {
                locationManager.removeUpdates(listener)
            }
        }.onStart {
            emit(Optional.empty())
        }.catch {
            emit(Optional.empty())
        }
    }
}
