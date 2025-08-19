package com.brsv44n.some_courier.location

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.core.utils.AuthHolder
import me.tatarka.inject.annotations.Inject

@Inject
class LocationUtils(
    private val context: Context,
    private val authHolder: AuthHolder
) {

    fun startLocationTracking() {
        LocationService.startService(context)
    }

    fun stopLocationTracking() {
        LocationService.stopService(context)
    }

    fun canStartLocationTracking(): Boolean {
        return isGpsEnabled() && isFineLocationPermissionGranted() && authHolder.isLoggedIn &&
                isLocationNotificationEnabled()
    }

    fun isGpsEnabled(): Boolean {
        val locationManager = ContextCompat.getSystemService(context, LocationManager::class.java)
            .let(::requireNotNull)
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun isFineLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("InlinedApi")
    private fun isLocationNotificationEnabled(): Boolean {
        val isNotificationPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val isLocationNotificationsChannelEnabled = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        )?.getNotificationChannel(
            context.getString(R.string.channel_id_location_tracking)
        )?.importance != NotificationManager.IMPORTANCE_NONE

        return isNotificationPermissionGranted && isLocationNotificationsChannelEnabled
    }
}
