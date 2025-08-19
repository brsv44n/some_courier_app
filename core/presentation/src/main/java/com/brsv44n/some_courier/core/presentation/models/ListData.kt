package com.brsv44n.some_courier.core.presentation.models

class ListData<T>(
    override val value: List<T>,
    override val hasNextPage: Boolean
) : Data<List<T>> {
    override fun isEmpty(): Boolean {
        return value.isEmpty()
    }
}
