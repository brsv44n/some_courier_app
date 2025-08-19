package com.brsv44n.some_courier.presentation.in_process_orders

import com.brsv44n.some_courier.presentation.models.OrderCardUiModel
import com.brsv44n.some_courier.core.presentation.models.ErrorModel
import com.arkivanov.decompose.value.Value

interface InProcessOrdersComponent {
    val ordersState: Value<OrdersInProcessState>
    val showModal: Value<Boolean>

    fun onEvent(event: Event)

    sealed interface Event {
        data object RetryClicked : Event
        data object OpenRouteClicked : Event
        data class OrderClicked(val orderId: Long) : Event
        data object ModalDismissed : Event
        data object EndRouteClicked : Event
        data object EndRouteConfirmed : Event
        data object RefreshPulled : Event
    }

    sealed interface Output {
        data object Exit : Output
        data class OrderClicked(val orderId: Long) : Output
    }
}

sealed class OrdersInProcessState {
    data object Loading : OrdersInProcessState()
    data class Empty(
        override val isRefreshing: Boolean = false,
        override val isRouteStatusUpdating: Boolean = false
    ) : OrdersInProcessState()
    data class Error(
        val error: ErrorModel,
        override val isRefreshing: Boolean = false
    ) : OrdersInProcessState()
    data class Success(
        val userAddress: String = "",
        val ordersList: List<OrderCardUiModel> = emptyList(),
        val showEndRouteButton: Boolean = false,
        override val isRefreshing: Boolean = false,
        override val isRouteStatusUpdating: Boolean = false
    ) : OrdersInProcessState()

    open val isRefreshing: Boolean = false
    open val isRouteStatusUpdating: Boolean = false
}
