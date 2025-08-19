package com.brsv44n.some_courier.messaging.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageAction(
    val type: Type,
    val targetId: Int?,
) : Parcelable {

    enum class Type {
        ORDER
    }
}
