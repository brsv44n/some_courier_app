package com.brsv44n.some_courier.messaging.core

import android.content.Context
import android.content.Intent
import com.brsv44n.some_courier.MainActivity
import com.brsv44n.some_courier.core.di.annotations.Singleton
import me.tatarka.inject.annotations.Inject

@Inject
@Singleton
class ChpPushNotificationIntentBuilder(
    private val context: Context,
) : PushNotificationIntentBuilder {

    override fun invoke(): Intent = Intent(context, MainActivity::class.java)
}
