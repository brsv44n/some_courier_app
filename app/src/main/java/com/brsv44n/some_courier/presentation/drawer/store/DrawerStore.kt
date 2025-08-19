package com.brsv44n.some_courier.presentation.drawer.store

import com.brsv44n.some_courier.domain.models.User
import com.brsv44n.some_courier.presentation.drawer.store.DrawerStore.Intent
import com.brsv44n.some_courier.presentation.drawer.store.DrawerStore.Label
import com.brsv44n.some_courier.presentation.drawer.store.DrawerStore.State
import com.arkivanov.mvikotlin.core.store.Store

internal interface DrawerStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object LogoutClicked : Intent
    }

    sealed interface Label {
        data object LoggedOut : Label
    }

    data class State(
        val isLoading: Boolean = true,
        val error: Throwable? = null,
        val user: User? = null,
        val isLoggingOut: Boolean = false
    )
}
