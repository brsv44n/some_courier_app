package com.brsv44n.some_courier.presentation.authenticated_user_root.store

import com.brsv44n.some_courier.domain.models.User
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.domain.repository.SettingsRepository
import com.brsv44n.some_courier.messaging.core.MessagingTokenProvider
import com.brsv44n.some_courier.presentation.authenticated_user_root.store.AuthenticatedUserStore.Intent
import com.brsv44n.some_courier.presentation.authenticated_user_root.store.AuthenticatedUserStore.Label
import com.brsv44n.some_courier.presentation.authenticated_user_root.store.AuthenticatedUserStore.State
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.launch

internal class AuthenticatedUserStoreFactory(
    private val dispatchers: CoroutineDispatchers,
    private val storeFactory: StoreFactory,
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    private val messagingTokenProvider: MessagingTokenProvider
) {
    @OptIn(ExperimentalMviKotlinApi::class)
    fun provide(): AuthenticatedUserStore = object :
        AuthenticatedUserStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "authenticatedUserStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = coroutineExecutorFactory(mainContext = dispatchers.main) {
                onAction<Unit> {
                    launch {
                        messagingTokenProvider.getToken()?.let {
                            settingsRepository.updatePushToken(pushToken = it)
                        }
                        dispatch(Msg.TokenUpdated)
                    }
                    loadProfile()
                }
            },
            reducer = ReducerImpl
        ) {}

    @OptIn(ExperimentalMviKotlinApi::class)
    private fun CoroutineExecutorScope<State, Msg, Label>.loadProfile() = launch {
        profileRepository.getUser().collect {
            when {
                it.value != null -> dispatch(Msg.ProfileLoadingSucceed(it.getValue()))
            }
        }
    }

    private sealed interface Msg {
        data object TokenUpdated : Msg
        data class ProfileLoadingSucceed(val user: User) : Msg
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State {
            return when (msg) {
                is Msg.ProfileLoadingSucceed -> copy(user = msg.user)
                else -> copy()
            }
        }
    }
}
