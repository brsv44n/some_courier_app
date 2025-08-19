package com.brsv44n.some_courier.presentation.orders_history

import com.brsv44n.some_courier.presentation.models.OrdersByDateUiModel
import com.brsv44n.some_courier.core.presentation.models.ErrorModel

sealed class OrdersHistoryUiState {
    data object Loading : OrdersHistoryUiState()
    data class Error(val error: ErrorModel) : OrdersHistoryUiState()
    data class Success(
        val isRefreshingEnabled: Boolean = false,
        val orders: List<OrdersByDateUiModel> = emptyList(),
        val isPageLoading: Boolean = false,
    ) : OrdersHistoryUiState()

    data class Empty(
        override val isRefreshing: Boolean = false,
    ) : OrdersHistoryUiState() {
        override val isRefreshEnabled: Boolean = true
    }

    open val isRefreshing: Boolean = false
    open val isRefreshEnabled: Boolean = false
}
