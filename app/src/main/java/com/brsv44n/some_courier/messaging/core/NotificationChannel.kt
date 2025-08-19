package com.brsv44n.some_courier.messaging.core

import androidx.core.app.NotificationManagerCompat
import com.brsv44n.some_courier.R

enum class NotificationChannel(
    val id: Int,
    val displayName: Int,
    val importance: Int,
) {

    DEFAULT(
        R.string.default_notification_channel_id,
        R.string.default_notification_channel_name,
        NotificationManagerCompat.IMPORTANCE_DEFAULT
    )
}
