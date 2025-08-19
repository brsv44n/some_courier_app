package com.brsv44n.some_courier.data

import com.brsv44n.some_courier.domain.models.Route
import com.brsv44n.some_courier.domain.models.RouteEvent
import com.brsv44n.some_courier.domain.models.RouteStatus
import com.brsv44n.some_courier.domain.repository.RoutesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject

@Inject
class FakeRoutesRepository : RoutesRepository {
    override val routesUpdatesFlow: Flow<RouteEvent?>
        get() = flow {}

    override suspend fun getRoute(): Result<Route> = Result.success(
        Route(
            id = 1,
            status = RouteStatus.Created,
            orders = emptyList()
        )
    )

    override suspend fun closeRoute(): Result<Unit> = Result.success(Unit)
    override fun subscribeForUpdates() = Unit

    override fun unsubscribeFromUpdates() = Unit
}
