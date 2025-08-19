package com.brsv44n.some_courier.data.remote.dto

import com.brsv44n.some_courier.domain.models.RemoteSettings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RemoteSettingsDto(
    @SerialName("managerPhone") val managerPhone: String,
)

val RemoteSettingsDto.toDomain
    get() = RemoteSettings(
        managerPhone = managerPhone
    )
