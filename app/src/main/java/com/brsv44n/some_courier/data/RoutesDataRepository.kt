package com.brsv44n.some_courier.data

import com.brsv44n.some_courier.data.remote.dto.toDomain
import com.brsv44n.some_courier.domain.models.Route
import com.brsv44n.some_courier.domain.models.RouteEvent
import com.brsv44n.some_courier.domain.repository.RoutesRepository
import com.brsv44n.some_courier.core.di.annotations.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class RoutesDataRepository(
    private val apiService: ApiService,
    private val sseClient: SseClient
) : RoutesRepository {

    override val routesUpdatesFlow: Flow<RouteEvent?>
        get() = sseClient.events.map {
            when (it) {
                is RouteData.CreateRouteDto -> RouteEvent.RouteCreated(it.value.toDomain)
                is RouteData.ChangeRouteStatus -> RouteEvent.RouteStatusChanged(
                    it.value.id,
                    it.value.status
                )
            }
        }

    override suspend fun getRoute(): Result<Route?> = runCatching {
        apiService.getCurrentRoute().toDomain
    }

    override suspend fun closeRoute(): Result<Unit> = runCatching {
        apiService.closeRoute()
    }

    override fun subscribeForUpdates() {
        sseClient.connect()
    }

    override fun unsubscribeFromUpdates() {
        sseClient.disconnect()
    }
}
