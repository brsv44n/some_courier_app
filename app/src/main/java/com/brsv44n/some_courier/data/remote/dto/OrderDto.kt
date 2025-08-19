package com.brsv44n.some_courier.data.remote.dto

import com.brsv44n.some_courier.domain.models.Order
import com.brsv44n.some_courier.presentation.models.OrderStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.joda.time.format.ISODateTimeFormat

@Serializable
class OrderDto(
    @SerialName("id") val id: Long,
    @SerialName("status") val status: OrderStatus,
    @SerialName("number") val number: String,
    @SerialName("expectedDeliveryTime") val expectedDeliveryTime: String?,
    @SerialName("paymentInfo") val paymentInfo: PaymentInfoDto,
    @SerialName("recipientInfo") val recipientInfo: RecipientDto,
    @SerialName("comment") val comment: String?,
    @SerialName("hasTrouble") val isWithIssue: Boolean,
    @SerialName("queueNumber") val queueNumber: Int? = null,
)

val OrderDto.toDomain
    get() = Order(
        id = id,
        status = status,
        number = number,
        expectedDeliveryTime = expectedDeliveryTime?.let {
            ISODateTimeFormat.dateTimeParser().withZoneUTC()
                .parseDateTime(expectedDeliveryTime)
        },
        paymentInfo = paymentInfo.toDomain,
        recipientInfo = recipientInfo.toDomain,
        comment = if (comment.isNullOrBlank()) null else comment,
        isWithIssue = isWithIssue
    )
