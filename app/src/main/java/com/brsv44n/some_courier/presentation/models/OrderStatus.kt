package com.brsv44n.some_courier.presentation.models

import com.brsv44n.some_courier.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class OrderStatus(val resId: Int) {
    @SerialName("new")
    NEW(R.string.label_order_state_new),

    @SerialName("in_progress")
    IN_PROGRESS(R.string.label_order_state_in_progress),

    @SerialName("delivered")
    DELIVERED(R.string.label_order_state_delivered),

    @SerialName("cancelled")
    CANCELED(R.string.label_order_state_canceled)
}
