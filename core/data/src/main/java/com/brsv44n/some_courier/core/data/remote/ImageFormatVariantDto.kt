package com.brsv44n.some_courier.core.data.remote

import com.brsv44n.some_courier.core.domain.ImageVariant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageFormatVariantDto(
    @SerialName("urls")
    val urls: List<ImageFormatDto>,
    @SerialName("width")
    val width: Int,
    @SerialName("height")
    val height: Int
)

val ImageFormatVariantDto.toDomain: ImageVariant
    get() = ImageVariant(
        width = width,
        height = height,
        url = urls.first().url
    )
