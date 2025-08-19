package com.brsv44n.some_courier.domain.models

data class Route(
    val id: Long,
    val status: RouteStatus,
    val orders: List<Order>
)
