package com.brsv44n.some_courier.messaging.core

data class MessageContent(
    val title: String,
    val message: String,
    val channel: NotificationChannel,
    val action: MessageAction?
)
