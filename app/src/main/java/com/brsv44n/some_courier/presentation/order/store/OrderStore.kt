package com.brsv44n.some_courier.presentation.order.store

import com.brsv44n.some_courier.domain.models.Order
import com.brsv44n.some_courier.domain.models.User
import com.brsv44n.some_courier.presentation.order.store.OrderStore.Intent
import com.brsv44n.some_courier.presentation.order.store.OrderStore.Label
import com.brsv44n.some_courier.presentation.order.store.OrderStore.State
import com.arkivanov.mvikotlin.core.store.Store

internal interface OrderStore : Store<Intent, State, Label> {
    sealed interface Intent {
        data object UpdateOrderStatus : Intent
        data object Retry : Intent
        data object MarkAsWithProblem : Intent
        data object Refresh : Intent
    }

    sealed interface Label {
        data object StatusUpdatingSucceed : Label
        data object MarkAsWithProblemSucceed : Label

        sealed interface FailedLabel : Label {
            data class StatusUpdatingFailed(override val error: Throwable) : FailedLabel
            data class MarkAsWithProblemFailed(override val error: Throwable) : FailedLabel

            val error: Throwable
        }
    }

    data class State(
        val isLoading: Boolean = true,
        val error: Throwable? = null,
        val order: Order? = null,
        val user: User? = null,
        val isOrderStatusChanging: Boolean = false,
        val isRefreshing: Boolean = false
    )
}
