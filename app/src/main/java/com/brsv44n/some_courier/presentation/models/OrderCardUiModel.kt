package com.brsv44n.some_courier.presentation.models

import com.brsv44n.some_courier.domain.models.Order
import com.brsv44n.some_courier.core.presentation.mapper.DateTimeFormatter

data class OrderCardUiModel(
    val id: Long,
    val orderStatus: OrderStatus,
    val paymentMethod: PaymentMethod,
    val number: String,
    val address: String?,
    val time: String?,
    val isWarning: Boolean,
    val comment: String?,
)

fun Order.toOrderCardUiModel(dateTimeFormatter: DateTimeFormatter) = OrderCardUiModel(
    id = id,
    orderStatus = status,
    paymentMethod = paymentInfo.paymentMethod,
    number = number,
    address = recipientInfo.address,
    time = expectedDeliveryTime?.let { dateTimeFormatter.format(it, "HH:mm") },
    isWarning = isWithIssue,
    comment = comment
)
