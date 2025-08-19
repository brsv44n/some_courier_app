package com.brsv44n.some_courier.presentation.login_root.login_support.store

import com.brsv44n.some_courier.presentation.login_root.login_support.store.LoginSupportStore.Intent
import com.brsv44n.some_courier.presentation.login_root.login_support.store.LoginSupportStore.Label
import com.brsv44n.some_courier.presentation.login_root.login_support.store.LoginSupportStore.State
import com.arkivanov.mvikotlin.core.store.Store

internal interface LoginSupportStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object Refresh : Intent
    }

    sealed interface Label

    data class State(
        val managersPhone: String? = null,
        val isLoading: Boolean = false
    )
}
