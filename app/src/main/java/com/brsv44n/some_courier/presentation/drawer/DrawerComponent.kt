package com.brsv44n.some_courier.presentation.drawer

import com.brsv44n.some_courier.presentation.models.UserUiModel
import com.brsv44n.some_courier.core.presentation.models.ErrorModel
import com.arkivanov.decompose.value.Value

interface DrawerComponent {

    val uiState: Value<UiState>
    val modalState: Value<DrawerActiveModal>

    @Suppress("NonBooleanPropertyPrefixedWithIs")
    val isProgressVisible: Value<Boolean>

    fun onEvent(event: Event)

    data class UiState(
        val isLoading: Boolean = false,
        val currentUser: UserUiModel? = null,
        val error: ErrorModel? = null,
    )

    sealed class Event {
        data object OrdersClicked : Event()
        data object HistoryClicked : Event()
        data object ProfileClicked : Event()
        data object SettingsClicked : Event()
        data object LogoutClicked : Event()
        data object ModalDismissed : Event()
        data object LogoutConfirmed : Event()
    }

    sealed interface Output {
        data object OpenLogin : Output
        data object HistoryClicked : Output
        data object ProfileClicked : Output
        data object OrdersClicked : Output
        data object SettingsClicked : Output
    }
}

enum class DrawerActiveModal {
    NONE, LOGOUT
}
