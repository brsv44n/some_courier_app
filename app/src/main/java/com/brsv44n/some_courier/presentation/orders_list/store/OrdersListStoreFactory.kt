package com.brsv44n.some_courier.presentation.orders_list.store

import com.brsv44n.some_courier.domain.models.Order
import com.brsv44n.some_courier.domain.models.OrdersListType
import com.brsv44n.some_courier.domain.repository.OrderRepository
import com.brsv44n.some_courier.presentation.models.OrderStatus
import com.brsv44n.some_courier.presentation.orders_list.store.OrdersListStore.Intent
import com.brsv44n.some_courier.presentation.orders_list.store.OrdersListStore.Label
import com.brsv44n.some_courier.presentation.orders_list.store.OrdersListStore.State
import com.brsv44n.some_courier.core.domain.entities.PagedList
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class OrdersListStoreFactory(
    private val storeFactory: StoreFactory,
    private val dispatchers: CoroutineDispatchers,
    private val ordersListRepository: OrderRepository,
    private val type: OrdersListType,
    private val storeName: String,
) {

    @Suppress("LongMethod")
    @OptIn(ExperimentalMviKotlinApi::class)
    fun provide(): OrdersListStore =
        object :
            OrdersListStore,
            Store<Intent, State, Label> by storeFactory.create(
                name = storeName,
                initialState = State(),
                reducer = ReducerImpl,
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = coroutineExecutorFactory(dispatchers.main) {
                    var loadJob: Job? = null

                    suspend fun loadPage(
                        page: Int,
                        onStart: () -> Unit,
                        onSuccess: (PagedList<Order>) -> Unit,
                        onFailure: (Throwable) -> Unit,
                        onLastPage: () -> Unit,
                    ) {
                        onStart()
                        ordersListRepository.getOrders(type = type, page = page)
                            .onSuccess { pagedList ->
                                onSuccess(pagedList)
                                if (pagedList.pageSize * pagedList.page > pagedList.total) onLastPage()
                            }.onFailure {
                                onFailure(it)
                            }
                    }

                    onAction<Unit> {
                        loadJob = launch {
                            loadPage(
                                state.currentPage,
                                onStart = { dispatch(Msg.LoadingStarted) },
                                onSuccess = { dispatch(Msg.LoadingSucceed(it)) },
                                onFailure = { dispatch(Msg.LoadingFailed(it)) },
                                onLastPage = { dispatch(Msg.LastPageLoaded) }
                            )
                        }
                        observeOrdersUpdates()
                    }

                    onIntent<Intent.LoadNextPage> {
                        if (state.canLoadNewPage) {
                            loadJob = launch {
                                loadPage(
                                    state.currentPage,
                                    onStart = { dispatch(Msg.LoadNextPageStarted) },
                                    onSuccess = { dispatch(Msg.LoadNextPageSucceed(it)) },
                                    onFailure = {
                                        publish(Label.LoadNextPageFailed(it))
                                        dispatch(Msg.LoadNextPageFailed(it))
                                    },
                                    onLastPage = { dispatch(Msg.LastPageLoaded) }
                                )
                            }
                        }
                    }

                    onIntent<Intent.Refresh> {
                        if (state.canRefresh) {
                            loadJob?.cancel()
                            loadJob = launch {
                                loadPage(
                                    1,
                                    onStart = { dispatch(Msg.RefreshStarted) },
                                    onSuccess = { dispatch(Msg.RefreshSucceed(it)) },
                                    onFailure = {
                                        publish(Label.RefreshFailed(it))
                                        dispatch(Msg.RefreshFailed(it))
                                    },
                                    onLastPage = { dispatch(Msg.LastPageLoaded) }
                                )
                            }
                        }
                    }

                    onIntent<Intent.Retry> {
                        loadJob?.cancel()
                        loadJob = launch {
                            loadPage(
                                state.currentPage,
                                onStart = { dispatch(Msg.LoadingStarted) },
                                onSuccess = { dispatch(Msg.LoadingSucceed(it)) },
                                onFailure = { dispatch(Msg.LoadingFailed(it)) },
                                onLastPage = { dispatch(Msg.LastPageLoaded) }
                            )
                        }
                    }
                }
            ) {}

    @OptIn(ExperimentalMviKotlinApi::class)
    private fun CoroutineExecutorScope<State, Msg, Label>.observeOrdersUpdates() {
        launch {
            ordersListRepository.ordersUpdatesFlow
                .onEach { updatedOrder ->
                    val currentOrders = state.data
                    val updatedOrders = currentOrders.filter { order ->
                        order.id != updatedOrder.id || updatedOrder.status == OrderStatus.NEW
                    }
                    dispatch(Msg.OrdersUpdated(updatedOrders))
                }
                .launchIn(this)
        }
    }

    sealed class Msg {
        data object LoadingStarted : Msg()
        data class LoadingSucceed(val data: PagedList<Order>) : Msg()
        data class LoadingFailed(val throwable: Throwable) : Msg()
        data object RefreshStarted : Msg()
        data class RefreshSucceed(val data: PagedList<Order>) : Msg()
        data class RefreshFailed(val throwable: Throwable) : Msg()
        data object LoadNextPageStarted : Msg()
        data class LoadNextPageSucceed(val data: PagedList<Order>) : Msg()
        data class LoadNextPageFailed(val throwable: Throwable) : Msg()
        data object LastPageLoaded : Msg()
        data class OrdersUpdated(val orders: List<Order>) : Msg()
    }

    private object ReducerImpl : Reducer<State, Msg> {
        @Suppress("LongMethod")
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.LoadingStarted -> {
                    copy(
                        isLoading = true,
                        error = null,
                        data = emptyList(),
                        isLastPage = false,
                        currentPage = 1,
                        isRefreshing = false,
                        isPageLoading = false,
                        totalItems = 0
                    )
                }

                is Msg.LoadingFailed -> {
                    copy(
                        isLoading = false,
                        error = msg.throwable
                    )
                }

                is Msg.LoadingSucceed -> {
                    copy(
                        isLoading = false,
                        data = msg.data.items,
                        currentPage = currentPage + 1,
                        key = key + 1,
                        isLastPage = false,
                        totalItems = msg.data.total
                    )
                }

                Msg.LoadNextPageStarted -> {
                    copy(isPageLoading = true)
                }

                is Msg.LoadNextPageFailed -> {
                    copy(isPageLoading = false)
                }

                is Msg.LoadNextPageSucceed -> {
                    copy(
                        isPageLoading = false,
                        data = data + msg.data.items,
                        currentPage = currentPage + 1,
                        totalItems = msg.data.total
                    )
                }

                Msg.RefreshStarted -> {
                    copy(isRefreshing = true)
                }

                is Msg.RefreshFailed -> {
                    copy(isRefreshing = false)
                }

                is Msg.RefreshSucceed -> {
                    copy(
                        isRefreshing = false,
                        data = msg.data.items,
                        currentPage = 2,
                        error = null,
                        key = key + 1,
                        isLastPage = false,
                        totalItems = msg.data.total
                    )
                }

                Msg.LastPageLoaded -> {
                    copy(isLastPage = true)
                }

                is Msg.OrdersUpdated -> {
                    copy(data = msg.orders, totalItems = msg.orders.size)
                }
            }
    }

}
