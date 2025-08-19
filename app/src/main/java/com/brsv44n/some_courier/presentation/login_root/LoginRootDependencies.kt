package com.brsv44n.some_courier.presentation.login_root

import com.brsv44n.some_courier.data.ApiService
import com.brsv44n.some_courier.domain.repository.SettingsRepository
import com.brsv44n.some_courier.messaging.core.MessagingTokenProvider
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.popup.MessageNotifier
import com.brsv44n.some_courier.core.utils.AuthHolder
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.brsv44n.some_courier.core.utils.ExternalAppNavigator
import com.brsv44n.some_courier.core.utils.ResourceManager
import com.arkivanov.mvikotlin.core.store.StoreFactory

interface LoginRootDependencies {
    val storeFactory: StoreFactory
    val dispatchers: CoroutineDispatchers
    val externalAppNavigator: ExternalAppNavigator
    val errorHandler: ErrorHandler
    val authHolder: AuthHolder
    val settingsRepository: SettingsRepository
    val api: ApiService
    val messageNotifier: MessageNotifier
    val resourceManager: ResourceManager
    val messagingTokenProvider: MessagingTokenProvider
}
