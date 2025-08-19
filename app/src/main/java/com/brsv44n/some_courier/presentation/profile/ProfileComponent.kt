package com.brsv44n.some_courier.presentation.profile

import com.brsv44n.some_courier.presentation.models.UserUiModel
import com.brsv44n.some_courier.core.presentation.models.ErrorModel
import com.arkivanov.decompose.value.Value

interface ProfileComponent {
    val uiState: Value<UiState>

    data class UiState(
        val state: ProfileState
    )

    fun retryClicked()
}

sealed class ProfileState {
    data object Loading : ProfileState()
    data class Error(val error: ErrorModel) : ProfileState()
    data class Success(val userEntity: UserUiModel) : ProfileState()
}
