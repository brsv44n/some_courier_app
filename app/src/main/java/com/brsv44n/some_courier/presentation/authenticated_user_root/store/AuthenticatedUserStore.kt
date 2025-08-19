package com.brsv44n.some_courier.presentation.authenticated_user_root.store

import com.brsv44n.some_courier.domain.models.User
import com.brsv44n.some_courier.presentation.authenticated_user_root.store.AuthenticatedUserStore.Intent
import com.brsv44n.some_courier.presentation.authenticated_user_root.store.AuthenticatedUserStore.Label
import com.brsv44n.some_courier.presentation.authenticated_user_root.store.AuthenticatedUserStore.State
import com.arkivanov.mvikotlin.core.store.Store

internal interface AuthenticatedUserStore : Store<Intent, State, Label> {

    sealed interface Intent

    sealed interface Label

    data class State(
        val token: String? = null,
        val user: User? = null
    )
}
