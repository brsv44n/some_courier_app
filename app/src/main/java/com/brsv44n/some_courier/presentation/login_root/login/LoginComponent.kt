package com.brsv44n.some_courier.presentation.login_root.login

import com.arkivanov.decompose.value.Value

interface LoginComponent {

    val uiState: Value<UiState>

    fun onEvent(event: Event)

    data class UiState(
        val phone: String = "",
        val isClearButtonVisible: Boolean = false,
        val isLoginButtonEnabled: Boolean = false,
        val isInputsEnabled: Boolean = true,
        val showInputError: Boolean = false,
        val isLoginProgressVisible: Boolean = false,
        val invalidPhoneError: String = "",
    )

    sealed interface Event {
        data class PhoneChanged(val phone: String) : Event
        data object ClearClicked : Event
        data object LoginClicked : Event
        data object SupportClicked : Event
    }

    sealed interface Output {
        data object LoginSucceed : Output
        data object Exit : Output
        data object LoginSupport : Output
    }
}
