package com.brsv44n.some_courier.domain.models

sealed class RouteEvent {
    class RouteCreated(val route: Route) : RouteEvent()
    class RouteStatusChanged(val routeId: Long, val routeStatus: RouteStatus) : RouteEvent()
}
