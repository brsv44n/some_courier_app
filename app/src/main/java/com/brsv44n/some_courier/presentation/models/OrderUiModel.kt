package com.brsv44n.some_courier.presentation.models

import com.brsv44n.some_courier.domain.models.Order
import com.brsv44n.some_courier.core.presentation.formatCurrencyToRoubles
import com.brsv44n.some_courier.core.presentation.mapper.DateTimeFormatter

data class OrderUiModel(
    val id: Long,
    val orderStatus: OrderStatus,
    val paymentMethod: PaymentMethod,
    val number: String,
    val address: String?,
    val time: String?,
    val clientName: String,
    val price: String,
    val comment: String?,
    val paymentComment: String?,
    val isWithIssue: Boolean,
)

fun Order.toOrderUiModel(dateTimeFormatter: DateTimeFormatter) = OrderUiModel(
    id = id,
    orderStatus = status,
    paymentMethod = paymentInfo.paymentMethod,
    number = number,
    address = recipientInfo.address,
    time = expectedDeliveryTime?.let { dateTimeFormatter.format(it, "HH:mm") },
    clientName = recipientInfo.name,
    price = paymentInfo.price.roubles.formatCurrencyToRoubles(),
    comment = comment,
    paymentComment = comment,
    isWithIssue = isWithIssue,
)
