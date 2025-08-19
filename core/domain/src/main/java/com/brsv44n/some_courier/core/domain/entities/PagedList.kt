package com.brsv44n.some_courier.core.domain.entities

data class PagedList<T>(
    val items: List<T>,
    val total: Int,
    val page: Int,
    val pageSize: Int
)
