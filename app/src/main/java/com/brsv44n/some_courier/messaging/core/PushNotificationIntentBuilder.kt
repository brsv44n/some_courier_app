package com.brsv44n.some_courier.messaging.core

import android.content.Intent

interface PushNotificationIntentBuilder {
    operator fun invoke(): Intent
}
