package com.brsv44n.some_courier.presentation.orders_history.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.models.OrdersByDateUiModel
import com.brsv44n.some_courier.presentation.orders_history.OrdersByDateState
import com.brsv44n.some_courier.presentation.orders_history.OrdersHistoryComponent
import com.brsv44n.some_courier.presentation.orders_history.PreviewOrdersHistoryComponent
import com.brsv44n.some_courier.presentation.ui_kit.ErrorScreen
import com.brsv44n.some_courier.presentation.ui_kit.ProgressLoader
import com.brsv44n.some_courier.presentation.ui_kit.order_card.OrderCard
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.orderComment
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OrdersHistoryContent(
    modifier: Modifier,
    component: OrdersHistoryComponent,
) {
    val uiState by component.uiState.subscribeAsState()
    val isRefreshing by component.isRefreshing.subscribeAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = modifier.pullToRefresh(
            isRefreshing = isRefreshing,
            state = pullToRefreshState,
            onRefresh = { component.onEvent(OrdersHistoryComponent.Event.RefreshPulled) },
            enabled = uiState.isRefreshEnabled
        )
    ) {
        PullToRefreshDefaults.Indicator(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1f),
            state = pullToRefreshState,
            isRefreshing = isRefreshing
        )

        when (val state = uiState) {
            is OrdersByDateState.Loading -> {
                ProgressLoader(modifier = Modifier.align(Alignment.Center))
            }

            is OrdersByDateState.Empty -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.label_orders_history_empty),
                        style = TextStyles.AndroidBodyLarge,
                        color = orderComment
                    )
                }
            }

            is OrdersByDateState.Error -> {
                ErrorScreen(
                    modifier = Modifier.fillMaxSize(),
                    onClick = { component.onEvent(OrdersHistoryComponent.Event.RetryClicked) },
                    errorType = state.error
                )
            }

            is OrdersByDateState.Success -> {

                Box(modifier = Modifier.fillMaxSize()) {
                    OrdersByDateList(
                        modifier = Modifier.fillMaxSize(),
                        ordersByDate = state.ordersList,
                        isPageLoading = state.isPageLoading,
                        onOrderClicked = {
                            component.onEvent(
                                OrdersHistoryComponent.Event.OrderClicked(
                                    it
                                )
                            )
                        },
                        loadNextPage = { component.onEvent(OrdersHistoryComponent.Event.LoadNextPage) }
                    )
                }
            }
        }
    }
}

@Composable
private fun OrdersByDateList(
    modifier: Modifier = Modifier,
    ordersByDate: List<OrdersByDateUiModel>,
    isPageLoading: Boolean,
    onOrderClicked: (Long) -> Unit,
    loadNextPage: () -> Unit,
) {

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .map {
                val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                val totalItems = listState.layoutInfo.totalItemsCount
                lastVisibleItem != null && lastVisibleItem.index >= totalItems - 10
            }
            .filter { it && !isPageLoading }
            .collect { loadNextPage() }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState
    ) {
        for (orderDate in ordersByDate) {
            item {
                orderDate.date?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it,
                        style = TextStyles.AndroidTitleMedium,
                        color = orderComment,
                        textAlign = TextAlign.Start
                    )
                }
            }
            for (order in orderDate.orders) {
                item {
                    OrderCard(
                        modifier = Modifier.fillMaxWidth(),
                        orderCardUiModel = order,
                        onClick = { onOrderClicked(order.id) }
                    )
                }
                if (order == orderDate.orders.last()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
        if (isPageLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                        .navigationBarsPadding()
                ) {
                    ProgressLoader(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
@Preview
private fun PreviewOrdersHistoryContent() {
    CHPAndroidCourierTheme {
        OrdersHistoryContent(
            modifier = Modifier.fillMaxSize(),
            component = PreviewOrdersHistoryComponent()
        )
    }
}
