package com.brsv44n.some_courier.presentation.permissions

import com.brsv44n.some_courier.data.PermissionsObserver
import com.brsv44n.some_courier.location.LocationServicesFactoryProvider
import com.brsv44n.some_courier.location.ResolvableException
import com.brsv44n.some_courier.core.presentation.asValue
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.brsv44n.some_courier.core.utils.ExternalAppNavigator
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class PermissionsComponentImpl(
    private val locationServicesFactoryProvider: LocationServicesFactoryProvider,
    private val externalAppNavigator: ExternalAppNavigator,
    private val permissionsObserver: PermissionsObserver,
    dispatchers: CoroutineDispatchers,
    componentContext: ComponentContext
) : PermissionsComponent, ComponentContext by componentContext {

    override val uiState: Value<PermissionsComponent.UiState>
        get() = permissionsObserver.state.asValue(scope).map { permissionsState ->
            PermissionsComponent.UiState(
                isLocationPermissionGranted = permissionsState.isFineLocationPermissionGranted,
                isGpsEnabled = permissionsState.isGpsEnabled,
                isNotificationsPermissionGranted = permissionsState.isNotificationsPermissionGranted
            )
        }

    override val resolvableExceptionHandlingFlow: Flow<ResolvableException>
        get() = _resolvableExceptionHandlingFlow

    private val _resolvableExceptionHandlingFlow = MutableSharedFlow<ResolvableException>()

    private val scope = coroutineScope(dispatchers.main + SupervisorJob())

    override fun onTurnOnGpsClicked() {
        scope.launch {
            locationServicesFactoryProvider.getFactory()
                .provideSettingsClient()
                .checkLocationSettings()
                .onFailure {
                    if (it is ResolvableException) {
                        _resolvableExceptionHandlingFlow.emit(it)
                    } else {
                        externalAppNavigator.openLocationSettings()
                    }
                }
        }
    }

    override fun onNotificationPermissionDenied() {
        externalAppNavigator.openAppSettings()
    }

    override fun onNotificationPermissionGranted() {
        permissionsObserver.invalidate()
    }

    override fun onFineLocationPermissionDenied() {
        externalAppNavigator.openAppSettings()
    }

    override fun onFineLocationPermissionGranted() {
        permissionsObserver.invalidate()
    }
}

@Inject
class PermissionsComponentFactory(
    private val dispatchers: CoroutineDispatchers,
    private val externalAppNavigator: ExternalAppNavigator,
    private val locationServicesFactoryProvider: LocationServicesFactoryProvider,
    private val permissionsObserver: PermissionsObserver
) {
    operator fun invoke(
        context: ComponentContext
    ): PermissionsComponent = PermissionsComponentImpl(
        dispatchers = dispatchers,
        permissionsObserver = permissionsObserver,
        componentContext = context,
        externalAppNavigator = externalAppNavigator,
        locationServicesFactoryProvider = locationServicesFactoryProvider
    )
}
