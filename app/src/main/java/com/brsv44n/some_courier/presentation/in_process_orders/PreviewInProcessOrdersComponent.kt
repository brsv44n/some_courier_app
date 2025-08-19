package com.brsv44n.some_courier.presentation.in_process_orders

import androidx.compose.runtime.mutableStateListOf
import com.brsv44n.some_courier.presentation.models.OrderCardUiModel
import com.brsv44n.some_courier.presentation.models.OrderStatus
import com.brsv44n.some_courier.presentation.models.PaymentMethod
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class PreviewInProcessOrdersComponent : InProcessOrdersComponent {

    override val showModal: MutableValue<Boolean> = MutableValue(false)

    private val ordersList: MutableList<OrderCardUiModel> = mutableStateListOf()

    init {
        ordersList.addAll(
            listOf(
                OrderCardUiModel(
                    id = 1,
                    address = "Московский проспект, 24, этаж 11, кв. 77",
                    number = "#J722",
                    comment = "Как будете на месте — позвоните. Выйду, встречу",
                    orderStatus = OrderStatus.DELIVERED,
                    paymentMethod = PaymentMethod.CASH,
                    isWarning = false,
                    time = "11:30"
                ),
                OrderCardUiModel(
                    id = 1,
                    address = "Московский проспект, 24, этаж 11, кв. 77",
                    number = "#J722",
                    comment = "Как будете на месте — позвоните. Выйду, встречу",
                    orderStatus = OrderStatus.DELIVERED,
                    paymentMethod = PaymentMethod.CASH,
                    isWarning = false,
                    time = "11:30"
                ),
                OrderCardUiModel(
                    id = 1,
                    address = "Московский проспект, 24, этаж 11, кв. 77",
                    number = "#J722",
                    comment = "Как будете на месте — позвоните. Выйду, встречу",
                    orderStatus = OrderStatus.IN_PROGRESS,
                    paymentMethod = PaymentMethod.CASH,
                    isWarning = false,
                    time = "11:30"
                ),
                OrderCardUiModel(
                    id = 1,
                    address = "Московский проспект, 24, этаж 11, кв. 77",
                    number = "#J722",
                    comment = "Как будете на месте — позвоните. Выйду, встречу",
                    orderStatus = OrderStatus.CANCELED,
                    paymentMethod = PaymentMethod.CASH,
                    isWarning = false,
                    time = "11:30"
                )
            )
        )
    }

    override val ordersState: Value<OrdersInProcessState> =
        MutableValue(
            OrdersInProcessState.Success(
                userAddress = "ул. Красноармейская, 31",
                ordersList = ordersList
            )
        )

    override fun onEvent(event: InProcessOrdersComponent.Event) = Unit
}
