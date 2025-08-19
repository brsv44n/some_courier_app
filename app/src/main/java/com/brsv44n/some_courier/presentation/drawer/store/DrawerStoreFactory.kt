package com.brsv44n.some_courier.presentation.drawer.store

import com.brsv44n.some_courier.domain.models.User
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.domain.use_case.LogoutUseCase
import com.brsv44n.some_courier.presentation.drawer.store.DrawerStore.Intent
import com.brsv44n.some_courier.presentation.drawer.store.DrawerStore.Label
import com.brsv44n.some_courier.presentation.drawer.store.DrawerStore.State
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.launch

internal class DrawerStoreFactory(
    private val dispatchers: CoroutineDispatchers,
    private val storeFactory: StoreFactory,
    private val profileRepository: ProfileRepository,
    private val logoutUseCase: LogoutUseCase
) {

    @OptIn(ExperimentalMviKotlinApi::class)
    fun provide(): DrawerStore = object :
        DrawerStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "drawer_store",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = coroutineExecutorFactory(mainContext = dispatchers.main) {

                onAction<Unit> {
                    loadProfile()
                }

                onIntent<Intent.LogoutClicked> {
                    launch { logout() }
                }
            },
            reducer = ReducerImpl
        ) {}

    @OptIn(ExperimentalMviKotlinApi::class)
    private fun CoroutineExecutorScope<State, Msg, Label>.loadProfile() = launch {
        profileRepository.getUser().collect {
            when {
                it.isLoading -> dispatch(Msg.LoadingStarted)
                it.error != null -> dispatch(Msg.LoadingFailed(it.getError()))
                it.value != null -> dispatch(Msg.LoadingSucceed(it.getValue()))
            }
        }
    }

    @OptIn(ExperimentalMviKotlinApi::class)
    private fun CoroutineExecutorScope<State, Msg, Label>.logout() = launch {
        dispatch(Msg.LoggingOut(true))
        logoutUseCase.invoke(isTriggeredByUser = true)
        publish(Label.LoggedOut)
        dispatch(Msg.LoggingOut(false))
    }

    private sealed interface Msg {
        data object LoadingStarted : Msg
        data class LoadingFailed(val error: Throwable) : Msg
        data class LoadingSucceed(val user: User) : Msg
        data class LoggingOut(val isLoading: Boolean) : Msg
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.LoadingFailed -> copy(error = msg.error, isLoading = false)
            Msg.LoadingStarted -> copy(isLoading = true, error = null)
            is Msg.LoadingSucceed -> copy(user = msg.user, isLoading = false, error = null)
            is Msg.LoggingOut -> copy(isLoggingOut = msg.isLoading)
        }
    }
}
