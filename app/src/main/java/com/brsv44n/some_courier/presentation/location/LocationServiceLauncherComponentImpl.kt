package com.brsv44n.some_courier.presentation.location

import com.brsv44n.some_courier.location.LocationUtils
import com.brsv44n.some_courier.core.utils.AuthHolder
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.coroutines.repeatOnLifecycle
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class LocationServiceLauncherComponentImpl(
    private val locationUtils: LocationUtils,
    private val authHolder: AuthHolder,
    dispatchers: CoroutineDispatchers,
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    private val scope = coroutineScope(dispatchers.main + SupervisorJob())

    init {
        scope.launch {
            lifecycle.repeatOnLifecycle(
                minActiveState = Lifecycle.State.RESUMED
            ) {
                authHolder.tokenFlow.collect {
                    if (locationUtils.canStartLocationTracking()) {
                        locationUtils.startLocationTracking()
                    } else {
                        locationUtils.stopLocationTracking()
                    }
                }
            }
        }
    }
}

@Inject
class LocationServiceLauncherComponentFactory(
    private val locationUtils: LocationUtils,
    private val dispatchers: CoroutineDispatchers,
    private val authHolder: AuthHolder
) {
    fun invoke(componentContext: ComponentContext): LocationServiceLauncherComponentImpl {
        return LocationServiceLauncherComponentImpl(
            locationUtils = locationUtils,
            dispatchers = dispatchers,
            componentContext = componentContext,
            authHolder = authHolder
        )
    }
}
