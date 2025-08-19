package com.brsv44n.some_courier.location

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.brsv44n.some_courier.App
import com.brsv44n.some_courier.core.utils.Optional
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import timber.log.Timber
import kotlin.time.Duration.Companion.minutes

class LocationService : Service() {

    companion object {
        const val ACTION_START = "LocationService.ACTION_START"
        const val ACTION_STOP = "LocationService.ACTION_STOP"
        private const val NOTIFICATION_ID = 777

        fun startService(context: Context) {
            val intent = Intent(context, LocationService::class.java).apply {
                action = ACTION_START
            }
            ContextCompat.startForegroundService(context, intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, LocationService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }

    private val appDiComponent by lazy { (application as App).appDiComponent }
    private var locationTrackingJob: Job? = null

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_START) {
            startForeground(
                NOTIFICATION_ID,
                appDiComponent.locationNotificationBuilder.createNotification()
            )
        }
        if (!appDiComponent.locationUtils.canStartLocationTracking() || intent?.action == ACTION_STOP) {
            locationTrackingJob?.cancel()
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }
        startLocationTracking()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    private fun startLocationTracking() {
        locationTrackingJob?.cancel()
        locationTrackingJob = appDiComponent.locationServicesFactoryProvider
            .getFactory()
            .provideLocationProvider()
            .locationFlow(1.minutes.inWholeMilliseconds)
            .map { Optional.of(it) }
            .catch { Timber.tag("LocationService").e(it, "LocationFlow failed") }
            .onEach {
                Timber.tag("LocationService").d("Location: ${it.orNull()}")
            }
            .filter { it.isPresent }
            .map { it.get() }
            .onEach {
                runCatching {
                    appDiComponent.apiService.sendLocation(
                        buildJsonObject {
                            put("latitude", it.latitude)
                            put("longitude", it.longitude)
                        }
                    )
                }.onFailure { error ->
                    Timber.tag("LocationService").e(error, "Failed to send location")
                }
            }
            .flowOn(appDiComponent.dispatchers.io)
            .launchIn(coroutineScope)
    }

}
