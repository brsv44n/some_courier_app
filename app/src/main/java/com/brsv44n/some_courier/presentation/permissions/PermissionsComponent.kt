package com.brsv44n.some_courier.presentation.permissions

import com.brsv44n.some_courier.location.ResolvableException
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow

interface PermissionsComponent {

    val uiState: Value<UiState>
    val resolvableExceptionHandlingFlow: Flow<ResolvableException>

    fun onTurnOnGpsClicked() = Unit

    fun onNotificationPermissionDenied() = Unit

    fun onNotificationPermissionGranted() = Unit

    fun onFineLocationPermissionDenied() = Unit

    fun onFineLocationPermissionGranted() = Unit

    data class UiState(
        val isLocationPermissionGranted: Boolean,
        val isGpsEnabled: Boolean,
        val isNotificationsPermissionGranted: Boolean
    )
}
