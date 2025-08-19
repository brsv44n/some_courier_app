package com.brsv44n.some_courier.messaging.rustore

import com.brsv44n.some_courier.App
import ru.rustore.sdk.pushclient.messaging.model.RemoteMessage
import ru.rustore.sdk.pushclient.messaging.service.RuStoreMessagingService

class RustoreMessagingService : RuStoreMessagingService() {

    companion object {
        const val SERVICE_NAME = "rustore"
    }

    private val messageNotifier by lazy { (application as App).appDiComponent.pushMessageNotifier }
    private val messageContentExtractor by lazy { (application as App).appDiComponent.messageContentExtractor }

    override fun onMessageReceived(message: RemoteMessage) {
        val messageContent = messageContentExtractor.invoke(message)
        messageNotifier.invoke(messageContent)
    }
}
