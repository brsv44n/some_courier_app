package com.brsv44n.some_courier.domain.repository

import com.brsv44n.some_courier.domain.models.Route
import com.brsv44n.some_courier.domain.models.RouteEvent
import kotlinx.coroutines.flow.Flow

interface RoutesRepository {
    val routesUpdatesFlow: Flow<RouteEvent?>

    suspend fun getRoute(): Result<Route?>

    suspend fun closeRoute(): Result<Unit>

    fun subscribeForUpdates()

    fun unsubscribeFromUpdates()
}
