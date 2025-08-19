package com.brsv44n.some_courier.presentation.order.store

import com.brsv44n.some_courier.domain.models.Order
import com.brsv44n.some_courier.domain.models.User
import com.brsv44n.some_courier.domain.repository.OrderRepository
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.presentation.models.OrderStatus
import com.brsv44n.some_courier.presentation.order.store.OrderStore.Intent
import com.brsv44n.some_courier.presentation.order.store.OrderStore.Label
import com.brsv44n.some_courier.presentation.order.store.OrderStore.State
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

internal class OrderStoreFactory(
    private val dispatchers: CoroutineDispatchers,
    private val storeFactory: StoreFactory,
    private val orderRepository: OrderRepository,
    private val profileRepository: ProfileRepository,
    private val orderId: Long,
) {
    @OptIn(ExperimentalMviKotlinApi::class)
    fun provide(): OrderStore =
        object :
            OrderStore,
            Store<Intent, State, Label> by storeFactory.create(
                name = "order_store",
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = coroutineExecutorFactory(mainContext = dispatchers.main) {
                    var profileJob: Job? = null

                    suspend fun refreshOrder(
                        onStart: () -> Unit,
                        onSuccess: (Order) -> Unit,
                        onFailure: (Throwable) -> Unit,
                    ) {
                        onStart()
                        orderRepository.getOrder(orderId)
                            .onSuccess { order ->
                                onSuccess(order)
                            }.onFailure {
                                onFailure(it)
                            }
                    }

                    onAction<Unit> {
                        getOrder(orderId)
                        profileJob = loadProfile()
                    }

                    onIntent<Intent.Retry> {
                        getOrder(orderId)
                        profileJob?.cancel()
                        profileJob = loadProfile()
                    }

                    onIntent<Intent.UpdateOrderStatus> {
                        val currentStatus = state.order?.status ?: return@onIntent
                        val nextStatus =
                            OrderStatus.entries.getOrNull(currentStatus.ordinal + 1)
                                ?: return@onIntent
                        if (nextStatus == OrderStatus.CANCELED) return@onIntent
                        changeOrderStatus(
                            orderId = orderId,
                            newStatus = nextStatus
                        )
                    }

                    onIntent<Intent.MarkAsWithProblem> {
                        if (state.order?.isWithIssue != true) markAsWithProblem(orderId)
                    }

                    onIntent<Intent.Refresh> {
                        if (state.isLoading) return@onIntent
                        launch {
                            refreshOrder(
                                onStart = { dispatch(Msg.RefreshStarted) },
                                onSuccess = { dispatch(Msg.RefreshSucceed(it)) },
                                onFailure = {
                                    dispatch(Msg.RefreshFailed(it))
                                },
                            )
                        }
                    }
                },
                reducer = ReducerImpl
            ) {}

    @OptIn(ExperimentalMviKotlinApi::class)
    private fun CoroutineExecutorScope<State, Msg, Label>.getOrder(orderId: Long) = launch {
        dispatch(Msg.LoadingStarted)
        orderRepository.getOrder(orderId)
            .onSuccess {
                dispatch(Msg.LoadingSucceed(it))
            }
            .onFailure {
                dispatch(Msg.LoadingFailed(it))
            }
    }

    @OptIn(ExperimentalMviKotlinApi::class)
    private fun CoroutineExecutorScope<State, Msg, Label>.loadProfile() = launch {
        profileRepository.getUser().collect {
            when {
                it.value != null -> dispatch(Msg.ProfileLoadingSucceed(it.getValue()))
            }
        }
    }

    @OptIn(ExperimentalMviKotlinApi::class)
    private fun CoroutineExecutorScope<State, Msg, Label>.changeOrderStatus(
        orderId: Long,
        newStatus: OrderStatus,
    ) = launch {
        dispatch(Msg.StatusUpdateStateChanged(true))
        orderRepository.changeOrderStatus(
            orderId = orderId,
            newOrderStatus = newStatus
        )
            .onSuccess {
                publish(Label.StatusUpdatingSucceed)
                dispatch(Msg.LoadingSucceed(data = it))
            }
            .onFailure {
                publish(Label.FailedLabel.StatusUpdatingFailed(it))
                dispatch(Msg.StatusUpdateStateChanged(false))
            }
    }

    @OptIn(ExperimentalMviKotlinApi::class)
    private fun CoroutineExecutorScope<State, Msg, Label>.markAsWithProblem(orderId: Long) =
        launch {
            dispatch(Msg.StatusUpdateStateChanged(true))
            orderRepository.markOrderAsWithProblem(orderId = orderId)
                .onSuccess {
                    dispatch(Msg.LoadingSucceed(data = it))
                    publish(Label.MarkAsWithProblemSucceed)
                }
                .onFailure {
                    publish(Label.FailedLabel.MarkAsWithProblemFailed(it))
                    dispatch(Msg.StatusUpdateStateChanged(false))
                }
        }

    private sealed interface Msg {
        data class ProfileLoadingSucceed(val user: User) : Msg
        data object LoadingStarted : Msg
        data class LoadingFailed(val error: Throwable) : Msg
        data class LoadingSucceed(val data: Order) : Msg
        data class StatusUpdateStateChanged(val isUpdating: Boolean) : Msg
        data object RefreshStarted : Msg
        data class RefreshSucceed(val data: Order) : Msg
        data class RefreshFailed(val throwable: Throwable) : Msg
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.LoadingFailed -> copy(error = msg.error, isLoading = false)
            Msg.LoadingStarted -> copy(isLoading = true, error = null)
            is Msg.LoadingSucceed -> copy(
                order = msg.data,
                isLoading = false,
                error = null,
                isOrderStatusChanging = false
            )

            is Msg.ProfileLoadingSucceed -> copy(user = msg.user)
            is Msg.StatusUpdateStateChanged -> copy(isOrderStatusChanging = msg.isUpdating)

            Msg.RefreshStarted -> copy(isRefreshing = true)

            is Msg.RefreshSucceed -> copy(
                order = msg.data,
                isRefreshing = false,
                error = null
            )

            is Msg.RefreshFailed -> copy(isRefreshing = false)
        }
    }
}
