package com.brsv44n.some_courier.messaging.core

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.brsv44n.some_courier.core.di.annotations.Singleton
import me.tatarka.inject.annotations.Inject

@Inject
@Singleton
class NotificationChannelCreator(
    private val context: Context,
) {

    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke() {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        NotificationChannel.entries.forEach {
            manager.createNotificationChannel(
                android.app.NotificationChannel(
                    context.getString(it.id),
                    context.getString(it.displayName),
                    it.importance
                )
            )
        }
    }
}
