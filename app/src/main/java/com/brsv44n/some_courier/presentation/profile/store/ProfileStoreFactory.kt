package com.brsv44n.some_courier.presentation.profile.store

import com.brsv44n.some_courier.domain.models.User
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.presentation.profile.store.ProfileStore.Intent
import com.brsv44n.some_courier.presentation.profile.store.ProfileStore.Label
import com.brsv44n.some_courier.presentation.profile.store.ProfileStore.State
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ProfileStoreFactory(
    private val dispatchers: CoroutineDispatchers,
    private val storeFactory: StoreFactory,
    private val profileRepository: ProfileRepository,
) {

    @OptIn(ExperimentalMviKotlinApi::class)
    fun provide(): ProfileStore = object :
        ProfileStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "profile_store",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = coroutineExecutorFactory(mainContext = dispatchers.main) {
                var loadProfileJob: Job? = null

                onAction<Unit> {
                    loadProfileJob?.cancel()
                    loadProfileJob = loadProfile()
                }

                onIntent<Intent.Retry> {
                    loadProfileJob?.cancel()
                    loadProfileJob = loadProfile()
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

    private sealed interface Msg {
        data object LoadingStarted : Msg
        data class LoadingFailed(val error: Throwable) : Msg
        data class LoadingSucceed(val user: User) : Msg
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.LoadingFailed -> copy(error = msg.error, isLoading = false)
            Msg.LoadingStarted -> copy(isLoading = true, error = null)
            is Msg.LoadingSucceed -> copy(user = msg.user, isLoading = false, error = null)
        }
    }
}
