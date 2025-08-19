package com.brsv44n.some_courier.data.remote.dto

import com.brsv44n.some_courier.domain.models.PaymentInfo
import com.brsv44n.some_courier.presentation.models.PaymentMethod
import com.brsv44n.some_courier.core.domain.entities.Money
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PaymentInfoDto(
    @SerialName("price") val price: Long,
    @SerialName("paymentMethod") val paymentMethod: PaymentMethod,
)

val PaymentInfoDto.toDomain
    get() = PaymentInfo(
        price = Money(price),
        paymentMethod = paymentMethod
    )
