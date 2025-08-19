package com.brsv44n.some_courier.core.domain

data class SizedImageVariants(
    val small: List<ImageVariant>,
    val medium: List<ImageVariant>,
    val large: List<ImageVariant>
)
