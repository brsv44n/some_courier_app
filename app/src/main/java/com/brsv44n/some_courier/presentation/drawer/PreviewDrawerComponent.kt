package com.brsv44n.some_courier.presentation.drawer

import com.brsv44n.some_courier.presentation.models.UserUiModel
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

internal class PreviewDrawerComponent : DrawerComponent {
    override val uiState: Value<DrawerComponent.UiState> = MutableValue(
        DrawerComponent.UiState(
            currentUser = UserUiModel(
                id = 0,
                name = "Павловская А.",
                currentRestaurant = "Красноармейская, 31",
                phone = "+7 (999) 099-09-09"
            )
        )
    )
    override val modalState: Value<DrawerActiveModal> = MutableValue(DrawerActiveModal.NONE)

    @Suppress("NonBooleanPropertyPrefixedWithIs")
    override val isProgressVisible: Value<Boolean> = MutableValue(false)

    override fun onEvent(event: DrawerComponent.Event) = Unit
}
