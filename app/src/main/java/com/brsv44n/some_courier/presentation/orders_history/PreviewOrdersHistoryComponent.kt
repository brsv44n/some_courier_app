package com.brsv44n.some_courier.presentation.orders_history

import com.brsv44n.some_courier.presentation.models.OrderCardUiModel
import com.brsv44n.some_courier.presentation.models.OrderStatus
import com.brsv44n.some_courier.presentation.models.OrdersByDateUiModel
import com.brsv44n.some_courier.presentation.models.PaymentMethod
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

internal class PreviewOrdersHistoryComponent : OrdersHistoryComponent {
    @Suppress("NonBooleanPropertyPrefixedWithIs")
    override val isRefreshing: Value<Boolean> = MutableValue(false)
    override val uiState: Value<OrdersByDateState> = MutableValue(
        OrdersByDateState.Success(
            ordersList = listOf(
                OrdersByDateUiModel(
                    date = "22.05",
                    orders = listOf(
                        OrderCardUiModel(
                            id = 1,
                            orderStatus = OrderStatus.DELIVERED,
                            paymentMethod = PaymentMethod.PAID_ONLINE,
                            number = "#J741",
                            address = "ул. Грибоедова, 5А, этаж 2, кв. 20",
                            time = "11:55",
                            isWarning = false,
                            comment = "Не звоните в домофон — ребенок спит. " +
                                    "Лучше позвоните заранее, я выйду, встречу у подъезда"
                        ),
                        OrderCardUiModel(
                            id = 2,
                            orderStatus = OrderStatus.DELIVERED,
                            paymentMethod = PaymentMethod.PAID_ONLINE,
                            number = "#J733",
                            address = "Московский проспект, 24, этаж 11, кв. 77",
                            time = "11:40",
                            isWarning = false,
                            comment = null
                        ),
                        OrderCardUiModel(
                            id = 3,
                            orderStatus = OrderStatus.IN_PROGRESS,
                            paymentMethod = PaymentMethod.PAID_ONLINE,
                            number = "#J724",
                            address = "ул. Грибоедова, 5А, этаж 2, кв. 20",
                            time = "11:55",
                            isWarning = true,
                            comment = "Не звоните в домофон — ребенок спит. " +
                                    "Лучше позвоните заранее, я выйду, встречу у подъезда"
                        ),
                    )
                ),
                OrdersByDateUiModel(
                    date = "21.05",
                    orders = listOf(
                        OrderCardUiModel(
                            id = 1,
                            orderStatus = OrderStatus.CANCELED,
                            paymentMethod = PaymentMethod.PAID_ONLINE,
                            number = "#J708",
                            address = "ул. Грибоедова, 5А, этаж 2, кв. 20",
                            time = "11:55",
                            isWarning = false,
                            comment = "Не звоните в домофон — ребенок спит. " +
                                    "Лучше позвоните заранее, я выйду, встречу у подъезда"
                        ),
                        OrderCardUiModel(
                            id = 2,
                            orderStatus = OrderStatus.DELIVERED,
                            paymentMethod = PaymentMethod.CASH,
                            number = "#J704",
                            address = "Московский проспект, 24, этаж 11, кв. 77",
                            time = "11:40",
                            isWarning = false,
                            comment = null
                        ),
                        OrderCardUiModel(
                            id = 3,
                            orderStatus = OrderStatus.DELIVERED,
                            paymentMethod = PaymentMethod.CASH,
                            number = "#J691",
                            address = "ул. Грибоедова, 5А, этаж 2, кв. 20",
                            time = "11:30",
                            isWarning = true,
                            comment = "Как будете на месте — позвоните. Выйду, встречу"
                        ),
                    )
                )
            )
        )
    )

    override fun onEvent(event: OrdersHistoryComponent.Event) = Unit
}
