package com.brsv44n.some_courier.core.presentation.models

interface Data<T> {
    val value: T
    val hasNextPage: Boolean
    fun isEmpty(): Boolean

    fun canLoadMore(): Boolean {
        return hasNextPage && !isEmpty()
    }
}
