package com.brsv44n.some_courier.domain.models

data class User(
    val id: Int,
    val name: String,
    val phone: String,
    val restaurant: Restaurant,
)
