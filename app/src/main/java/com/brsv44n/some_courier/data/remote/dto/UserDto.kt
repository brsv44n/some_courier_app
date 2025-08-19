package com.brsv44n.some_courier.data.remote.dto

import com.brsv44n.some_courier.domain.models.User
import com.brsv44n.some_courier.core.utils.ResourceManager
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("phone") val phone: String,
    @SerialName("restaurant") val restaurant: RestaurantDto,
)

fun UserDto.toDomain(
    resourceManager: ResourceManager,
) = User(
    id = id,
    name = name,
    phone = phone,
    restaurant = restaurant.toDomain(resourceManager)
)
