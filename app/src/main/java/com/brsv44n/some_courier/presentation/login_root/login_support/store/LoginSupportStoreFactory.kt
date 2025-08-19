package com.brsv44n.some_courier.presentation.login_root.login_support.store

import com.brsv44n.some_courier.domain.models.RemoteSettings
import com.brsv44n.some_courier.domain.repository.SettingsRepository
import com.brsv44n.some_courier.presentation.login_root.login_support.store.LoginSupportStore.Intent
import com.brsv44n.some_courier.presentation.login_root.login_support.store.LoginSupportStore.Label
import com.brsv44n.some_courier.presentation.login_root.login_support.store.LoginSupportStore.State
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.launch

internal class LoginSupportStoreFactory(
    private val dispatchers: CoroutineDispatchers,
    private val storeFactory: StoreFactory,
    private val settingsRepository: SettingsRepository
) {

    @OptIn(ExperimentalMviKotlinApi::class)
    fun provide(): LoginSupportStore = object :
        LoginSupportStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "login",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = coroutineExecutorFactory(mainContext = dispatchers.main) {
                onAction<Unit> {
                    launch { getSettings() }
                }
            },
            reducer = ReducerImpl
        ) {}

    @OptIn(ExperimentalMviKotlinApi::class)
    private suspend fun CoroutineExecutorScope<State, Msg, Label>.getSettings() {
        dispatch(Msg.LoadingStarted)
        settingsRepository.getSettings()
            .onSuccess {
                dispatch(Msg.Success(it))
            }
            .onFailure {
                dispatch(Msg.Failure(it))
            }
        dispatch(Msg.LoadingFinished)
    }

    private sealed interface Msg {
        data class Success(val value: RemoteSettings) : Msg
        data class Failure(val error: Throwable) : Msg
        data object LoadingFinished : Msg
        data object LoadingStarted : Msg
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            Msg.LoadingFinished -> copy(isLoading = false)
            Msg.LoadingStarted -> copy(isLoading = true)
            is Msg.Failure -> copy()
            is Msg.Success -> copy(managersPhone = msg.value.managerPhone)
        }
    }
}
