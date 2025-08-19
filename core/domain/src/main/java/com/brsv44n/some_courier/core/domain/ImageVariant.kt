package com.brsv44n.some_courier.core.domain

import kotlinx.serialization.Serializable

@Serializable
data class ImageVariant(
    val width: Int,
    val height: Int,
    val url: String
)
