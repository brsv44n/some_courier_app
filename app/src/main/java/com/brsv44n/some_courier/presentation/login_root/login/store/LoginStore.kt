package com.brsv44n.some_courier.presentation.login_root.login.store

import com.brsv44n.some_courier.presentation.login_root.login.store.LoginStore.Intent
import com.brsv44n.some_courier.presentation.login_root.login.store.LoginStore.Label
import com.brsv44n.some_courier.presentation.login_root.login.store.LoginStore.State
import com.arkivanov.mvikotlin.core.store.Store

internal interface LoginStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class PhoneChanged(val phone: String) : Intent
        data object ClearClicked : Intent
        data object LoginClicked : Intent
    }

    sealed interface Label {
        data object LoginSucceed : Label
        data class LoginFailed(val error: Throwable) : Label
    }

    data class State(
        val phone: String = "",
        val isLoginInProgress: Boolean = false,
        val showInputError: Boolean = false,
        val invalidPhoneError: String = ""
    ) {
        val canLogin: Boolean
            get() = phone.isNotBlank()
    }
}
