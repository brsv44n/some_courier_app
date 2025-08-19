package com.brsv44n.some_courier.presentation.permissions

import com.brsv44n.some_courier.location.ResolvableException
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class PreviewPermissionsComponent : PermissionsComponent {
    override val uiState: Value<PermissionsComponent.UiState> = MutableValue(
        PermissionsComponent.UiState(
            isLocationPermissionGranted = false,
            isGpsEnabled = false,
            isNotificationsPermissionGranted = false
        )
    )
    override val resolvableExceptionHandlingFlow: Flow<ResolvableException> = emptyFlow()
}
