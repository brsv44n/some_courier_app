package com.brsv44n.some_courier.core.presentation

import com.brsv44n.some_courier.core.presentation.models.Data
import com.arkivanov.mvikotlin.core.store.Store

interface PagedStore<T : Data<*>> : Store<PagedStore.Intent, PagedStore.State<T>, PagedStore.Label> {

    sealed interface Intent {
        data object Retry : Intent
        data object Refresh : Intent
        data object LoadNextPage : Intent
    }

    sealed interface Label {
        data class PageLoadError(val error: Throwable) : Label
        data class RefreshError(val error: Throwable) : Label
    }

    sealed interface State<out T> {
        data object Loading : State<Nothing>
        data class Error(val error: Throwable) : State<Nothing>
        data class Content<out T : Data<*>>(
            val data: T,
            val showEmptyState: Boolean = false,
            val isPageLoading: Boolean = false,
            val isRefreshing: Boolean = false,
            val currentPage: Int = 1,
            val canLoadMore: Boolean = true
        ) : State<T>
    }
}
