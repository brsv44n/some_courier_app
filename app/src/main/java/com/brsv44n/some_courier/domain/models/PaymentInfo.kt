package com.brsv44n.some_courier.domain.models

import com.brsv44n.some_courier.presentation.models.PaymentMethod
import com.brsv44n.some_courier.core.domain.entities.Money

data class PaymentInfo(
    val price: Money,
    val paymentMethod: PaymentMethod,
)
