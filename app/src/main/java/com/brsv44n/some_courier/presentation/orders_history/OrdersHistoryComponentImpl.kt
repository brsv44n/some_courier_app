package com.brsv44n.some_courier.presentation.orders_history

import com.brsv44n.some_courier.domain.models.OrdersListType
import com.brsv44n.some_courier.presentation.models.toOrdersByDateUiModel
import com.brsv44n.some_courier.presentation.orders_list.store.OrdersListStore
import com.brsv44n.some_courier.presentation.orders_list.store.OrdersListStoreFactory
import com.brsv44n.some_courier.core.presentation.asValue
import com.brsv44n.some_courier.core.presentation.models.ErrorModel
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import me.tatarka.inject.annotations.Inject

class OrdersHistoryComponentImpl(
    private val dependencies: OrdersHistoryDependencies,
    private val output: (OrdersHistoryComponent.Output) -> Unit,
    componentContext: ComponentContext,
) : OrdersHistoryComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        OrdersListStoreFactory(
            dispatchers = dependencies.dispatchers,
            storeFactory = dependencies.storeFactory,
            ordersListRepository = dependencies.ordersRepository,
            type = OrdersListType.HISTORY,
            storeName = "orders_history_store"
        ).provide()
    }

    @Suppress("NonBooleanPropertyPrefixedWithIs")
    override val isRefreshing: Value<Boolean>
        get() = store.asValue().map {
            it.isRefreshing
        }

    override val uiState: Value<OrdersByDateState>
        get() = store.asValue().map { storeValue ->
            when {
                storeValue.isLoading -> OrdersByDateState.Loading
                storeValue.error != null -> OrdersByDateState.Error(
                    error = dependencies.errorHandler.getErrorModel(
                        storeValue.error
                    )
                )

                storeValue.canShowData -> OrdersByDateState.Success(
                    ordersList = storeValue.data.toOrdersByDateUiModel(dependencies.dateTimeFormatter),
                    isPageLoading = storeValue.isPageLoading,
                    isOrdersOver = storeValue.isLastPage
                )

                storeValue.canShowEmptyState -> OrdersByDateState.Empty()

                else -> OrdersByDateState.Error(ErrorModel.Unknown)
            }
        }

    override fun onEvent(event: OrdersHistoryComponent.Event) {
        when (event) {
            is OrdersHistoryComponent.Event.OrderClicked -> {
                output(OrdersHistoryComponent.Output.OrderClicked(event.orderId))
            }

            OrdersHistoryComponent.Event.LoadNextPage -> {
                store.accept(OrdersListStore.Intent.LoadNextPage)
            }

            OrdersHistoryComponent.Event.RetryClicked -> {
                store.accept(OrdersListStore.Intent.Retry)
            }

            OrdersHistoryComponent.Event.RefreshPulled -> {
                store.accept(OrdersListStore.Intent.Refresh)
            }
        }
    }
}

@Inject
class OrdersHistoryComponentFactory(
    private val dependencies: OrdersHistoryDependencies,
) {
    fun invoke(
        componentContext: ComponentContext,
        output: (OrdersHistoryComponent.Output) -> Unit,
    ): OrdersHistoryComponent = OrdersHistoryComponentImpl(
        dependencies = dependencies,
        output = output,
        componentContext = componentContext
    )
}
