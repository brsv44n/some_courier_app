package com.brsv44n.some_courier.core.presentation

import com.brsv44n.some_courier.core.presentation.models.Data
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutorScope
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PagedStoreFactory<T : Data<*>>(
    private val storeFactory: StoreFactory,
    private val requestFactory: suspend (Int) -> T,
    private val combine: T.(T, Boolean) -> T,
    private val storeName: String,
    private val mainDispatcher: CoroutineDispatcher
) {

    @OptIn(ExperimentalMviKotlinApi::class)
    fun provide(): PagedStore<T> = object :
        PagedStore<T>,
        Store<PagedStore.Intent, PagedStore.State<T>, PagedStore.Label> by storeFactory.create(
            name = storeName,
            initialState = PagedStore.State.Loading,
            reducer = ReducerImpl(
                combine
            ),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = coroutineExecutorFactory(mainDispatcher) {
                var pageLoadingJob: Job? = null
                var refreshingJob: Job? = null

                onAction<Unit> {
                    launch { loadData() }
                }

                onIntent<PagedStore.Intent.Retry> {
                    launch { loadData() }
                }

                onIntent<PagedStore.Intent.Refresh> {
                    if (!state.canRefresh()) return@onIntent
                    pageLoadingJob?.cancel()
                    refreshingJob?.cancel()
                    refreshingJob = launch {
                        dispatch(Msg.RefreshingStarted)
                        runCatching {
                            requestFactory(1)
                        }.onFailure {
                            publish(PagedStore.Label.RefreshError(it))
                            dispatch(Msg.RefreshingFailed(it))
                        }.onSuccess { transactions ->
                            dispatch(Msg.RefreshingSucceeded(transactions))
                        }
                    }
                }

                onIntent<PagedStore.Intent.LoadNextPage> {
                    if (!state.canLoadPage()) return@onIntent
                    val currentPage = when (val state = state) {
                        is PagedStore.State.Content -> state.currentPage
                        else -> return@onIntent
                    }
                    dispatch(Msg.LoadNextPageStarted)
                    pageLoadingJob = launch {
                        runCatching {
                            requestFactory(currentPage + 1)
                        }.onFailure {
                            publish(PagedStore.Label.PageLoadError(it))
                            dispatch(Msg.LoadNextPageFailed(it))
                        }.onSuccess { transactions ->
                            dispatch(Msg.LoadNextPageSucceeded(transactions))
                        }
                    }
                }
            }
        ) {}

    @OptIn(ExperimentalMviKotlinApi::class)
    private suspend fun CoroutineExecutorScope<PagedStore.State<T>, Msg<T>, PagedStore.Label>.loadData() {
        dispatch(Msg.LoadingStarted)
        runCatching {
            requestFactory(1)
        }.onSuccess { data ->
            dispatch(Msg.LoadingSucceeded(data))
        }.onFailure { error ->
            dispatch(Msg.LoadingFailed(error))
        }
    }

    private fun <T> PagedStore.State<T>.canRefresh(): Boolean = when (this) {
        is PagedStore.State.Content -> !isRefreshing
        else -> false
    }

    private fun <T> PagedStore.State<T>.canLoadPage(): Boolean = when (this) {
        is PagedStore.State.Content -> !isPageLoading && canLoadMore
        else -> false
    }

    private sealed interface Msg<out T> {
        data object LoadingStarted : Msg<Nothing>
        data class LoadingFailed(val error: Throwable) : Msg<Nothing>
        data class LoadingSucceeded<T : Data<*>>(val data: T) : Msg<T>
        data object RefreshingStarted : Msg<Nothing>
        data class RefreshingFailed(val error: Throwable) : Msg<Nothing>
        data class RefreshingSucceeded<T : Data<*>>(val data: T) : Msg<T>
        data object LoadNextPageStarted : Msg<Nothing>
        data class LoadNextPageFailed(val error: Throwable) : Msg<Nothing>
        data class LoadNextPageSucceeded<T : Data<*>>(val data: T) : Msg<T>
    }

    private class ReducerImpl<T : Data<*>>(
        private val combine: T.(T, Boolean) -> T
    ) : Reducer<PagedStore.State<T>, Msg<T>> {
        override fun PagedStore.State<T>.reduce(msg: Msg<T>): PagedStore.State<T> = when (msg) {
            Msg.LoadingStarted -> {
                PagedStore.State.Loading
            }

            is Msg.LoadingFailed -> {
                PagedStore.State.Error(msg.error)
            }

            is Msg.LoadingSucceeded -> {
                PagedStore.State.Content(
                    data = msg.data,
                    showEmptyState = msg.data.isEmpty(),
                    currentPage = 1,
                    canLoadMore = msg.data.canLoadMore()
                )
            }

            Msg.RefreshingStarted -> {
                reduceContent {
                    copy(isRefreshing = true)
                }
            }

            is Msg.RefreshingSucceeded -> {
                reduceContent {
                    copy(
                        data = msg.data,
                        showEmptyState = msg.data.isEmpty(),
                        isRefreshing = false,
                        currentPage = 1,
                        canLoadMore = msg.data.canLoadMore()
                    )
                }
            }

            is Msg.RefreshingFailed -> {
                reduceContent {
                    copy(isRefreshing = false)
                }
            }

            Msg.LoadNextPageStarted -> {
                reduceContent {
                    copy(isPageLoading = true)
                }
            }

            is Msg.LoadNextPageFailed -> {
                reduceContent {
                    copy(isPageLoading = false)
                }
            }

            is Msg.LoadNextPageSucceeded -> {
                reduceContent {
                    copy(
                        data = data.combine(msg.data, msg.data.hasNextPage),
                        showEmptyState = data.isEmpty(),
                        isPageLoading = false,
                        currentPage = currentPage + 1,
                        canLoadMore = msg.data.canLoadMore()
                    )
                }
            }
        }

        private fun PagedStore.State<T>.reduceContent(
            block: PagedStore.State.Content<T>.() -> PagedStore.State.Content<T>,
        ): PagedStore.State<T> =
            when (this) {
                is PagedStore.State.Content -> block()
                else -> this
            }
    }
}
