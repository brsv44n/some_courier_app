package com.brsv44n.some_courier.core.data.remote

import com.brsv44n.some_courier.core.domain.entities.PagedList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PagedListPageSizeResponseDto<T : Any>(
    @SerialName("items")
    val items: List<T>,
    @SerialName("total")
    val total: Int,
    @SerialName("page")
    val page: Int,
    @SerialName("pageSize")
    val pageSize: Int,
)

@Serializable
class PagedListResponseDto<T : Any>(
    @SerialName("items")
    val items: List<T>,
    @SerialName("totalPages")
    val totalPages: Int,
    @SerialName("currentPage")
    val currentPage: Int,
)

@Serializable
class ListResponseDto<T : Any>(
    @SerialName("items")
    val items: List<T>,
)

fun <T : Any, R : Any> PagedListPageSizeResponseDto<T>.toPaginatedList(itemMapper: (T) -> R): PagedList<R> {
    return PagedList(
        items = items.map(itemMapper),
        total = total,
        page = page,
        pageSize = pageSize
    )
}

fun <T : Any, R : Any> PagedListResponseDto<T>.toPaginatedList(itemMapper: (T) -> R): PagedList<R> {
    return PagedList(
        items = items.map(itemMapper),
        total = totalPages,
        page = currentPage,
        pageSize = items.size / totalPages
    )
}
