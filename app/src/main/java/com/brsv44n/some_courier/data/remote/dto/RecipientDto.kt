package com.brsv44n.some_courier.data.remote.dto

import com.brsv44n.some_courier.domain.models.Recipient
import com.brsv44n.some_courier.core.domain.entities.PointEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RecipientDto(
    @SerialName("name") val name: String,
    @SerialName("phone") val phone: String,
    @SerialName("address") val address: String?,
    @SerialName("latitude") val latitude: Float?,
    @SerialName("longitude") val longitude: Float?,
)

val RecipientDto.toDomain
    get() = Recipient(
        name = name,
        phone = phone,
        address = address,
        location = createPointEntity(latitude?.toDouble(), longitude?.toDouble())
    )

internal fun createPointEntity(latitude: Double?, longitude: Double?): PointEntity? {
    if (latitude == null || longitude == null) {
        return null
    }
    return PointEntity(
        latitude = latitude,
        longitude = longitude
    )
}
