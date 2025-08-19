package com.brsv44n.some_courier.data.remote.dto

import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.domain.models.Restaurant
import com.brsv44n.some_courier.core.utils.ResourceManager
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RestaurantDto(
    @SerialName("id") val id: Int,
    @SerialName("address") val address: String?,
    @SerialName("managerPhone") val managerPhone: String?,
)

fun RestaurantDto.toDomain(
    resourceManager: ResourceManager,
) = Restaurant(
    id = id,
    address = address,
    managerPhone = managerPhone ?: resourceManager.getString(R.string.data_default_support_number)
)
