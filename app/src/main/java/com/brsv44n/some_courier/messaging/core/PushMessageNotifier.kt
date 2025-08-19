package com.brsv44n.some_courier.messaging.core

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.core.di.annotations.Singleton
import me.tatarka.inject.annotations.Inject
import kotlin.random.Random

@Singleton
@Inject
class PushMessageNotifier(
    private val context: Context,
    private val pushNotificationIntentBuilder: PushNotificationIntentBuilder
) {

    companion object {
        const val EXTRA_ACTION_BUTTON = "action_button"
    }

    private val notificationManager by lazy { NotificationManagerCompat.from(context) }

    @SuppressLint("MissingPermission")
    operator fun invoke(message: MessageContent) {
        if (isNotificationPermissionNotGranted()) return
        val intent = pushNotificationIntentBuilder.invoke()
            .also { it.putExtra(EXTRA_ACTION_BUTTON, message.action) }
        val pendingIntent = buildPendingIntent(intent)
        val notification =
            NotificationCompat.Builder(context, context.getString(message.channel.id))
                .setAutoCancel(true)
                .setContentTitle(message.title)
                .setContentText(message.message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message.message))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_push_notification_new_route)

        notificationManager.notify(Random.nextInt(), notification.build())
    }

    private fun buildPendingIntent(intent: Intent) = PendingIntent.getActivity(
        context,
        Random.nextInt(),
        intent,
        pendingIntentFlags()
    )

    private fun pendingIntentFlags() =
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

    private fun isNotificationPermissionNotGranted() =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && context.checkSelfPermission(
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED

}
