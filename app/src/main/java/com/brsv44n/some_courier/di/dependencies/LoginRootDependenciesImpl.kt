package com.brsv44n.some_courier.di.dependencies

import com.brsv44n.some_courier.data.ApiService
import com.brsv44n.some_courier.domain.repository.SettingsRepository
import com.brsv44n.some_courier.messaging.core.MessagingTokenProvider
import com.brsv44n.some_courier.presentation.login_root.LoginRootDependencies
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.popup.MessageNotifier
import com.brsv44n.some_courier.core.utils.AuthHolder
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.brsv44n.some_courier.core.utils.ExternalAppNavigator
import com.brsv44n.some_courier.core.utils.ResourceManager
import com.arkivanov.mvikotlin.core.store.StoreFactory
import me.tatarka.inject.annotations.Inject

@Inject
class LoginRootDependenciesImpl(
    override val storeFactory: StoreFactory,
    override val dispatchers: CoroutineDispatchers,
    override val externalAppNavigator: ExternalAppNavigator,
    override val errorHandler: ErrorHandler,
    override val authHolder: AuthHolder,
    override val settingsRepository: SettingsRepository,
    override val api: ApiService,
    override val messageNotifier: MessageNotifier,
    override val resourceManager: ResourceManager,
    override val messagingTokenProvider: MessagingTokenProvider,
) : LoginRootDependencies
