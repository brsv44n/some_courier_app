package com.brsv44n.some_courier.domain.models

import com.brsv44n.some_courier.core.domain.entities.PointEntity

data class Recipient(
    val name: String,
    val phone: String,
    val address: String?,
    val location: PointEntity?
)
