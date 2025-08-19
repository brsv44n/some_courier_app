package com.brsv44n.some_courier.location

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import com.brsv44n.some_courier.R
import me.tatarka.inject.annotations.Inject

@Inject
class LocationNotificationBuilder(
    private val context: Context
) {

    fun createNotification(): Notification {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, context.getString(R.string.channel_id_location_tracking))
        } else {
            NotificationCompat.Builder(context)
        }
        val cancelAction = NotificationCompat.Action.Builder(
            IconCompat.createWithResource(context, android.R.drawable.ic_menu_close_clear_cancel),
            context.getString(R.string.action_notification_location_tracking_stop),
            PendingIntent.getService(
                context,
                0,
                Intent(context, LocationService::class.java).apply {
                    action = LocationService.ACTION_STOP
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        return builder
            .setContentTitle(context.getString(R.string.title_notification_location_tracking))
            .setContentText(context.getString(R.string.label_notification_location_tracking))
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .addAction(cancelAction.build())
            .setSilent(true)
            .build()
    }

    fun createNotificationChannelIfNeeded() {
        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)
            .let(::requireNotNull)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    context.getString(R.string.channel_id_location_tracking),
                    context.getString(R.string.channel_name_location_tracking),
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }
    }
}
