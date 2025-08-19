package com.brsv44n.some_courier.presentation.in_process_orders.store

import com.brsv44n.some_courier.domain.models.Route
import com.brsv44n.some_courier.presentation.in_process_orders.store.InProcessOrdersStore.Intent
import com.brsv44n.some_courier.presentation.in_process_orders.store.InProcessOrdersStore.Label
import com.brsv44n.some_courier.presentation.in_process_orders.store.InProcessOrdersStore.State
import com.arkivanov.mvikotlin.core.store.Store

internal interface InProcessOrdersStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object CloseRoute : Intent
        data object GetRoute : Intent
        data object Retry : Intent
        data object Refresh : Intent
        data object ViewResumed : Intent
        data object ViewDestroyed : Intent
    }

    sealed interface Label {
        data object ClosingRouteSucceed : Label
        sealed interface FailedLabel : Label {
            data class ClosingRouteFailed(override val error: Throwable) : FailedLabel
            data class RefreshFailed(override val error: Throwable) : FailedLabel

            val error: Throwable
        }
    }

    data class State(
        val route: Route? = null,
        val restaurantAddress: String? = null,
        val error: Throwable? = null,
        val profileError: Throwable? = null,
        val isLoading: Boolean = true,
        val isRouteRefreshing: Boolean = false,
        val isRouteStatusUpdating: Boolean = false
    )
}
