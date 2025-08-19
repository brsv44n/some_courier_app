package com.brsv44n.some_courier.core.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageFormatDto(
    @SerialName("format")
    val format: String,
    @SerialName("url")
    val url: String
)
