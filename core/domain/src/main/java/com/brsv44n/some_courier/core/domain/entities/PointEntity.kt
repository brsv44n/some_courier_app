package com.brsv44n.some_courier.core.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class PointEntity(
    val latitude: Double,
    val longitude: Double
)
