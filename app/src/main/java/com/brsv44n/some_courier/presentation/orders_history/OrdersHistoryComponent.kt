package com.brsv44n.some_courier.presentation.orders_history

import com.brsv44n.some_courier.presentation.models.OrdersByDateUiModel
import com.brsv44n.some_courier.core.presentation.models.ErrorModel
import com.arkivanov.decompose.value.Value

interface OrdersHistoryComponent {

    val uiState: Value<OrdersByDateState>

    @Suppress("NonBooleanPropertyPrefixedWithIs")
    val isRefreshing: Value<Boolean>

    fun onEvent(event: Event)

    sealed interface Event {
        data object RetryClicked : Event
        data class OrderClicked(val orderId: Long) : Event
        data object LoadNextPage : Event
        data object RefreshPulled : Event
    }

    sealed interface Output {
        data object Exit : Output
        data class OrderClicked(val orderId: Long) : Output
    }
}

sealed class OrdersByDateState {
    data object Loading : OrdersByDateState()
    data class Empty(
        override val isRefreshEnabled: Boolean = true,
    ) : OrdersByDateState()

    data class Error(val error: ErrorModel) : OrdersByDateState()
    data class Success(
        val ordersList: List<OrdersByDateUiModel> = emptyList(),
        val isPageLoading: Boolean = true,
        val isOrdersOver: Boolean = true,
        override val isRefreshEnabled: Boolean = true,
    ) : OrdersByDateState()

    open val isRefreshEnabled: Boolean
        get() = false
}
