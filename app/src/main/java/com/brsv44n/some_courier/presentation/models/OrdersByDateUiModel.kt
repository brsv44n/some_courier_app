package com.brsv44n.some_courier.presentation.models

import com.brsv44n.some_courier.domain.models.Order
import com.brsv44n.some_courier.core.presentation.mapper.DateTimeFormatter

data class OrdersByDateUiModel(
    val date: String?,
    val orders: List<OrderCardUiModel>,
)

fun List<Order>.toOrdersByDateUiModel(dateTimeFormatter: DateTimeFormatter): List<OrdersByDateUiModel> {
    return this
        .groupBy { it.expectedDeliveryTime?.toLocalDate() }
        .map { (date, orders) ->
            OrdersByDateUiModel(
                date = date?.let { dateTimeFormatter.format(date, "dd.MM.yyyy") },
                orders = orders.map { it.toOrderCardUiModel(dateTimeFormatter) }
            )
        }
        .sortedByDescending { it.date }
}
