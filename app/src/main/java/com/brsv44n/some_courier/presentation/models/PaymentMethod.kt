package com.brsv44n.some_courier.presentation.models

import com.brsv44n.some_courier.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PaymentMethod(val resId: Int) {
    @SerialName("paid_online")
    PAID_ONLINE(R.string.label_order_paying_state_online),
    @SerialName("cash")
    CASH(R.string.label_order_paying_state_cash),
    @SerialName("card")
    CARD(R.string.label_order_paying_state_card),
}
