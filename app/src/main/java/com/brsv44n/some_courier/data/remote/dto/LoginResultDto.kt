package com.brsv44n.some_courier.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LoginResultDto(
    @SerialName("accessToken") val accessToken: String,
)
