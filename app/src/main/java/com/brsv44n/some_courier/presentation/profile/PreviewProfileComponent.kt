package com.brsv44n.some_courier.presentation.profile

import com.brsv44n.some_courier.presentation.models.UserUiModel
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

internal class PreviewProfileComponent : ProfileComponent {
    override val uiState: Value<ProfileComponent.UiState> = MutableValue(
        ProfileComponent.UiState(
            state = ProfileState.Success(
                UserUiModel(
                    id = 1,
                    name = "Анастасия Павловская",
                    currentRestaurant = "Красноармейская, 31",
                    phone = "+7 (999) 099-09-09"
                )
            )
        )
    )

    override fun retryClicked() = Unit
}
