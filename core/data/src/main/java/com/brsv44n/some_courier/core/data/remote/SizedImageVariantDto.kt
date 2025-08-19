package com.brsv44n.some_courier.core.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SizedImageVariantDto(
    @SerialName("small")
    val small: ImageVariantDto = ImageVariantDto("", 0, 0),
    @SerialName("medium")
    val medium: ImageVariantDto = ImageVariantDto("", 0, 0),
    @SerialName("large")
    val large: ImageVariantDto = ImageVariantDto("", 0, 0)
)
