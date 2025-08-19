package com.brsv44n.some_courier.presentation.order

import com.brsv44n.some_courier.presentation.models.OrderUiModel
import com.brsv44n.some_courier.core.presentation.models.ErrorModel
import com.arkivanov.decompose.value.Value

interface OrderComponent {
    val uiState: Value<OrderState>
    val modalState: Value<OrderActiveModal>

    @Suppress("NonBooleanPropertyPrefixedWithIs")
    val isRefreshing: Value<Boolean>

    fun onEvent(event: Event)

    sealed interface Event {
        data object RetryClicked : Event
        data object MapClicked : Event
        data object WarningClicked : Event
        data object WarningConfirmed : Event
        data object FinishOrderClicked : Event
        data object FinishOrderConfirmed : Event
        data object BackClicked : Event
        data object ManagerClicked : Event
        data object ClientClicked : Event
        data object ChangeOrderStatusClicked : Event
        data object ModalDismissed : Event
        data object RefreshPulled : Event
    }

    sealed interface Output {
        data object Exit : Output
    }
}

enum class OrderActiveModal {
    NONE, PROBLEM, FINISH
}

sealed class OrderState {
    data class Success(
        val order: OrderUiModel,
        override val isIssueButtonEnabled: Boolean,
        override val isMapButtonEnabled: Boolean,
        override val isOverlayProgressVisible: Boolean,
        override val isRefreshEnabled: Boolean = true
    ) : OrderState()

    data class Error(val error: ErrorModel) : OrderState()
    data object Loading : OrderState()

    open val isIssueButtonEnabled = false
    open val isMapButtonEnabled = false
    open val isOverlayProgressVisible = false
    open val isRefreshEnabled = false
}
