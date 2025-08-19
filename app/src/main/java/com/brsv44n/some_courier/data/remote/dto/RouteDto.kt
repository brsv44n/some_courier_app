package com.brsv44n.some_courier.data.remote.dto

import com.brsv44n.some_courier.domain.models.Route
import com.brsv44n.some_courier.domain.models.RouteStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RouteResponseDto(
    @SerialName("route") val route: RouteDto?
)

@Serializable
class RouteDto(
    @SerialName("id") val id: Long,
    @SerialName("courierId") val courierId: Long,
    @SerialName("status") val status: RouteStatus,
    @SerialName("orders") val orders: List<OrderDto>
)

@Serializable
class UpdateRouteStatusDto(
    @SerialName("id") val id: Long,
    @SerialName("courierId") val courierId: Long,
    @SerialName("status") val status: RouteStatus,
)

val RouteResponseDto.toDomain
    get() = route?.toDomain

val RouteDto.toDomain
    get() = Route(
        id = id,
        status = status,
        orders = orders.map { it.toDomain }
    )
