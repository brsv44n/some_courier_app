package com.brsv44n.some_courier.core.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DefaultImageVariantDto(
    @SerialName("default")
    val default: List<ImageFormatVariantDto>
)
