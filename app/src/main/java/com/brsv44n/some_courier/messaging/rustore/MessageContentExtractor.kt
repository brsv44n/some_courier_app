package com.brsv44n.some_courier.messaging.rustore

import com.brsv44n.some_courier.messaging.core.MessageActionParser
import com.brsv44n.some_courier.messaging.core.MessageContent
import com.brsv44n.some_courier.messaging.core.NotificationChannel
import me.tatarka.inject.annotations.Inject
import ru.rustore.sdk.pushclient.messaging.model.RemoteMessage

@Inject
class MessageContentExtractor(
    private val messageActionParser: MessageActionParser
) {
    operator fun invoke(message: RemoteMessage): MessageContent {
        val title = message.data["title"].orEmpty()
        val body = message.data["body"].orEmpty()
        val action = message.data["action_button"]?.let(messageActionParser::invoke)
        return MessageContent(title, body, NotificationChannel.DEFAULT, action)
    }
}
