package com.brsv44n.some_courier.presentation.login_root.login

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

internal class PreviewLoginComponent : LoginComponent {
    override val uiState: Value<LoginComponent.UiState>
        get() = _uiState

    private val _uiState = MutableValue(LoginComponent.UiState().copy(showInputError = true))

    override fun onEvent(event: LoginComponent.Event) = when (event) {
        is LoginComponent.Event.PhoneChanged -> {
            _uiState.value = _uiState.value.copy(
                phone = event.phone,
                isLoginButtonEnabled = event.phone.isNotBlank()
            )
        }

        LoginComponent.Event.ClearClicked -> {
            _uiState.value = _uiState.value.copy(phone = "", isClearButtonVisible = false)
        }

        LoginComponent.Event.LoginClicked -> {
            _uiState.value = _uiState.value.copy(showInputError = !_uiState.value.showInputError)
        }

        else -> {
            Unit
        }
    }
}
