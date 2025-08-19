package com.brsv44n.some_courier.core.data.remote

import com.brsv44n.some_courier.core.domain.entities.IdName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class IdNameDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)

fun IdNameDto.toDomain() = IdName(
    id = id,
    name = name
)
