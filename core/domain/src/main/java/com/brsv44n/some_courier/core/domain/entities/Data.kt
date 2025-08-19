package com.brsv44n.some_courier.core.domain.entities

data class Data<T>(
    val isLoading: Boolean = false,
    val value: T? = null,
    val error: Throwable? = null
) {

    val isRefreshing: Boolean
        get() = isLoading && value != null

    @JvmName("getErrorNotNull")
    fun getError(): Throwable = requireNotNull(error)

    @JvmName("getValueNotNull")
    fun getValue(): T = requireNotNull(value)
}
