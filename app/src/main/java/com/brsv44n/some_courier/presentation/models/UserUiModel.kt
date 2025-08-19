package com.brsv44n.some_courier.presentation.models

import com.brsv44n.some_courier.domain.models.User

data class UserUiModel(
    val id: Int,
    val name: String,
    val currentRestaurant: String?,
    val phone: String?,
)

fun User.toDomain() = UserUiModel(
    id = id,
    name = name,
    currentRestaurant = restaurant.address,
    phone = phone
)
