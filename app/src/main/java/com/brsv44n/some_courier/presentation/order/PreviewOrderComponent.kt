package com.brsv44n.some_courier.presentation.order

import com.brsv44n.some_courier.presentation.models.OrderStatus
import com.brsv44n.some_courier.presentation.models.OrderUiModel
import com.brsv44n.some_courier.presentation.models.PaymentMethod
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

internal class PreviewOrderComponent : OrderComponent {

    @Suppress("NonBooleanPropertyPrefixedWithIs")
    override val isRefreshing: Value<Boolean> = MutableValue(false)

    override val uiState: Value<OrderState> = MutableValue(
        OrderState.Success(
            order = OrderUiModel(
                id = 1,
                address = "Московский проспект, 24, этаж 11, кв. 77",
                number = "#J722",
                comment = "Как будете на месте — позвоните. Выйду, встречу",
                orderStatus = OrderStatus.IN_PROGRESS,
                paymentMethod = PaymentMethod.CASH,
                time = "11:30",
                price = "1000",
                clientName = "Иван",
                paymentComment = null,
                isWithIssue = false,
            ),
            isIssueButtonEnabled = true,
            isMapButtonEnabled = true,
            isOverlayProgressVisible = false,
        )
    )

    override val modalState: MutableValue<OrderActiveModal> = MutableValue(OrderActiveModal.NONE)

    override fun onEvent(event: OrderComponent.Event) {
        when (event) {
            OrderComponent.Event.WarningClicked -> {
                modalState.value = OrderActiveModal.PROBLEM
            }

            OrderComponent.Event.ModalDismissed -> {
                modalState.value = OrderActiveModal.NONE
            }

            OrderComponent.Event.FinishOrderClicked -> {
                modalState.value = OrderActiveModal.FINISH
            }

            OrderComponent.Event.FinishOrderConfirmed -> {
                modalState.value = OrderActiveModal.NONE
            }

            OrderComponent.Event.WarningConfirmed -> {
                modalState.value = OrderActiveModal.NONE
            }

            else -> {}
        }
    }
}
