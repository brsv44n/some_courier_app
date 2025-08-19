package com.brsv44n.some_courier.core.data.remote

import com.brsv44n.some_courier.core.domain.ImageVariant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ImageVariantDto(
    @SerialName("url")
    val url: String,
    @SerialName("width")
    val width: Int,
    @SerialName("height")
    val height: Int
)

val ImageVariantDto.toDomain: () -> ImageVariant
    get() = { ImageVariant(width, height, url) }
