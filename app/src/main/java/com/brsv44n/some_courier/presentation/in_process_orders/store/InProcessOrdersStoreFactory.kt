package com.brsv44n.some_courier.presentation.in_process_orders.store

import com.brsv44n.some_courier.data.OrdersDataRepository
import com.brsv44n.some_courier.domain.models.Route
import com.brsv44n.some_courier.domain.models.RouteEvent
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.domain.repository.RoutesRepository
import com.brsv44n.some_courier.presentation.in_process_orders.store.InProcessOrdersStore.Intent
import com.brsv44n.some_courier.presentation.in_process_orders.store.InProcessOrdersStore.Label
import com.brsv44n.some_courier.presentation.in_process_orders.store.InProcessOrdersStore.State
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class InProcessOrdersStoreFactory(
    private val dispatchers: CoroutineDispatchers,
    private val storeFactory: StoreFactory,
    private val routesRepository: RoutesRepository,
    private val profileRepository: ProfileRepository,
    private val ordersListRepository: OrdersDataRepository,
) {

    @OptIn(ExperimentalMviKotlinApi::class)
    fun provide(): InProcessOrdersStore =
        object :
            InProcessOrdersStore,
            Store<Intent, State, Label> by storeFactory.create(
                name = "order_store",
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = coroutineExecutorFactory(mainContext = dispatchers.main) {
                    onAction<Unit> {
                        routesRepository.routesUpdatesFlow.onEach { routeEvent ->
                            when (routeEvent) {
                                is RouteEvent.RouteCreated -> {
                                    dispatch(Msg.LoadingSucceed(routeEvent.route))
                                }

                                is RouteEvent.RouteStatusChanged -> {
                                    if (state.route?.id == routeEvent.routeId) {
                                        getRoute(true)
                                        dispatch(Msg.LoadingSucceed(state.route?.copy(status = routeEvent.routeStatus)))
                                    }
                                }

                                else -> {
                                    Unit
                                }
                            }

                        }.launchIn(this)

                        subscribeToProfileUpdates()
                        getRoute(false)
                        observeOrdersUpdates()
                    }
                    onIntent<Intent.GetRoute> {
                        getRoute(false)
                    }
                    onIntent<Intent.CloseRoute> {
                        closeRoute()
                    }
                    onIntent<Intent.Retry> {
                        getRoute(false)
                    }
                    onIntent<Intent.Refresh> {
                        getRoute(true)
                    }
                    onIntent<Intent.ViewResumed> {
                        routesRepository.subscribeForUpdates()
                    }
                    onIntent<Intent.ViewDestroyed> {
                        routesRepository.unsubscribeFromUpdates()
                    }

                },
                reducer = ReducerImpl
            ) {}

    @OptIn(ExperimentalMviKotlinApi::class)
    private fun CoroutineExecutorScope<State, Msg, Label>.getRoute(isRefreshing: Boolean) = launch {
        if (isRefreshing) {
            dispatch(Msg.RouteRefreshingStarted)
        } else {
            dispatch(Msg.LoadingStarted)
        }
        profileRepository.fetchUser()
        routesRepository.getRoute()
            .onSuccess {
                dispatch(Msg.LoadingSucceed(it))
            }
            .onFailure {
                if (isRefreshing) {
                    publish(Label.FailedLabel.RefreshFailed(it))
                } else {
                    dispatch(Msg.LoadingFailed(it))
                }
            }
        dispatch(Msg.LoadingFinished)
    }

    @OptIn(ExperimentalMviKotlinApi::class)
    private fun CoroutineExecutorScope<State, Msg, Label>.closeRoute() = launch {
        dispatch(Msg.RouteStatusUpdatingStarted)
        routesRepository.closeRoute()
            .onSuccess {
                dispatch(Msg.LoadingSucceed(null))
                publish(Label.ClosingRouteSucceed)
            }
            .onFailure {
                publish(Label.FailedLabel.ClosingRouteFailed(it))
            }
        dispatch(Msg.LoadingFinished)
    }

    @OptIn(ExperimentalMviKotlinApi::class)
    private fun CoroutineExecutorScope<State, Msg, Label>.subscribeToProfileUpdates() = launch {
        profileRepository.getUser().collect {
            when {
                it.value != null -> dispatch(Msg.AddressLoaded(it.getValue().restaurant.address))
                it.error != null -> dispatch(Msg.LoadingProfileFailed(it.getError()))
            }
        }
    }

    @OptIn(ExperimentalMviKotlinApi::class)
    private fun CoroutineExecutorScope<State, Msg, Label>.observeOrdersUpdates() {
        launch {
            ordersListRepository.ordersUpdatesFlow.onEach {
                getRoute(false)
            }.launchIn(this)
        }
    }

    private sealed interface Msg {
        data object RouteRefreshingStarted : Msg
        data object RouteStatusUpdatingStarted : Msg
        data object LoadingStarted : Msg
        data object LoadingFinished : Msg
        data class LoadingProfileFailed(val error: Throwable) : Msg
        data class LoadingFailed(val error: Throwable) : Msg
        data class AddressLoaded(val address: String?) : Msg
        data class LoadingSucceed(val route: Route?) : Msg
    }

    private object ReducerImpl : Reducer<State, Msg> {
        @Suppress("LongMethod")
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.LoadingStarted -> copy(
                    isLoading = true,
                    error = null,
                    isRouteRefreshing = false
                )

                is Msg.LoadingFailed -> copy(
                    error = msg.error,
                    route = null
                )

                is Msg.LoadingSucceed -> copy(
                    route = msg.route,
                    error = null,
                )

                Msg.RouteRefreshingStarted -> copy(
                    isLoading = false,
                    isRouteRefreshing = true,
                )

                Msg.RouteStatusUpdatingStarted -> copy(
                    isLoading = false,
                    isRouteStatusUpdating = true,
                )

                is Msg.AddressLoaded -> copy(
                    restaurantAddress = msg.address
                )

                is Msg.LoadingProfileFailed -> copy(
                    profileError = msg.error
                )

                Msg.LoadingFinished -> copy(
                    isLoading = false,
                    isRouteRefreshing = false,
                    isRouteStatusUpdating = false
                )
            }
    }
}
