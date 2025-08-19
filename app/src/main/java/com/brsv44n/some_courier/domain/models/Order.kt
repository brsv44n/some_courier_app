package com.brsv44n.some_courier.domain.models

import com.brsv44n.some_courier.presentation.models.OrderStatus
import org.joda.time.DateTime

data class Order(
    val id: Long,
    val status: OrderStatus,
    val number: String,
    val expectedDeliveryTime: DateTime?,
    val paymentInfo: PaymentInfo,
    val recipientInfo: Recipient,
    val comment: String?,
    val isWithIssue: Boolean,
) {
    val canBeMarkedProblem: Boolean = status == OrderStatus.IN_PROGRESS && !isWithIssue
    val canBeOpenedInMap: Boolean =
        recipientInfo.location != null && status == OrderStatus.IN_PROGRESS
}
