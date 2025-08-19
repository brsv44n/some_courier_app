package com.brsv44n.some_courier.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.brsv44n.some_courier.location.GpsStateReceiver
import com.brsv44n.some_courier.core.di.annotations.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class PermissionsObserver(
    private val context: Context
) {

    val state: StateFlow<PermissionsState> get() = _state.asStateFlow()

    private val _state = MutableStateFlow(getCurrentState())

    private val gpsStateReceiver = GpsStateReceiver {
        updateState()
    }

    init {
        context.registerReceiver(
            gpsStateReceiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        )
    }

    fun registerGpsReceiver() {
        context.registerReceiver(
            gpsStateReceiver,
            IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        )
    }

    fun unregisterGpsReceiver() {
        context.unregisterReceiver(gpsStateReceiver)
    }

    fun invalidate() {
        updateState()
    }

    @SuppressLint("InlinedApi")
    private fun updateState() {
        _state.value = getCurrentState()
    }

    @SuppressLint("InlinedApi")
    private fun getCurrentState(): PermissionsState {
        val locationManager = ContextCompat.getSystemService(context, LocationManager::class.java)
            .let(::requireNotNull)
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isFineLocationPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val isNotificationsPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        return PermissionsState(
            isGpsEnabled = isGpsEnabled,
            isFineLocationPermissionGranted = isFineLocationPermissionGranted,
            isNotificationsPermissionGranted = isNotificationsPermissionGranted
        )
    }

    class PermissionsState(
        val isGpsEnabled: Boolean = false,
        val isFineLocationPermissionGranted: Boolean = false,
        val isNotificationsPermissionGranted: Boolean = false
    ) {
        val areAllPermissionsGranted: Boolean
            get() = isGpsEnabled && isFineLocationPermissionGranted && isNotificationsPermissionGranted
    }
}
