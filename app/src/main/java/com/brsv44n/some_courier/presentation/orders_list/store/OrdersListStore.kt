package com.brsv44n.some_courier.presentation.orders_list.store

import com.brsv44n.some_courier.domain.models.Order
import com.brsv44n.some_courier.presentation.orders_list.store.OrdersListStore.Intent
import com.brsv44n.some_courier.presentation.orders_list.store.OrdersListStore.Label
import com.brsv44n.some_courier.presentation.orders_list.store.OrdersListStore.State
import com.arkivanov.mvikotlin.core.store.Store

interface OrdersListStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object Refresh : Intent
        data object LoadNextPage : Intent
        data object Retry : Intent
    }

    sealed interface Label {
        data class RefreshFailed(val throwable: Throwable) : Label
        data class LoadNextPageFailed(val throwable: Throwable) : Label
    }

    data class State(
        val isLoading: Boolean = true,
        val isRefreshing: Boolean = false,
        val isPageLoading: Boolean = false,
        val data: List<Order> = emptyList(),
        val error: Throwable? = null,
        val currentPage: Int = 1,
        val isLastPage: Boolean = false,
        val totalItems: Int = 0,
        val key: Int = 0,
    ) {
        val canLoadNewPage: Boolean
            get() = !isLoading && !isRefreshing && !isPageLoading && !isLastPage

        val canRefresh: Boolean
            get() = !isLoading && !isRefreshing

        val canShowData: Boolean
            get() = data.isNotEmpty() && !isLoading && error == null

        val canShowEmptyState: Boolean
            get() = data.isEmpty() && !isLoading && error == null
    }

}
