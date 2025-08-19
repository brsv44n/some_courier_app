package com.brsv44n.some_courier.presentation.profile.store

import com.brsv44n.some_courier.domain.models.User
import com.brsv44n.some_courier.presentation.profile.store.ProfileStore.Intent
import com.brsv44n.some_courier.presentation.profile.store.ProfileStore.Label
import com.brsv44n.some_courier.presentation.profile.store.ProfileStore.State
import com.arkivanov.mvikotlin.core.store.Store

internal interface ProfileStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object Retry : Intent
    }

    sealed interface Label

    data class State(
        val isLoading: Boolean = true,
        val error: Throwable? = null,
        val user: User? = null
    )
}
