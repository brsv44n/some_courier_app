package com.brsv44n.some_courier.core.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class IdName(
    val id: String,
    val name: String
)
