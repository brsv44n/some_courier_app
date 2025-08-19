package com.brsv44n.some_courier.core.domain.entities

class Phone(
    val number: String = "",
    val countryCode: String = "+7"
) {
    val formatted: String
        get() = "$countryCode$number"

    val isCompleted: Boolean
        get() = number.length == 10

    val isInvalid: Boolean
        get() = number.isNotEmpty() && number.first() != '9'
}
