package com.brsv44n.some_courier.presentation.order.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.models.OrderStatus
import com.brsv44n.some_courier.presentation.order.OrderActiveModal
import com.brsv44n.some_courier.presentation.order.OrderComponent
import com.brsv44n.some_courier.presentation.order.OrderState
import com.brsv44n.some_courier.presentation.order.PreviewOrderComponent
import com.brsv44n.some_courier.presentation.ui_kit.BottomBlock
import com.brsv44n.some_courier.presentation.ui_kit.ChpdOutlinedButton
import com.brsv44n.some_courier.presentation.ui_kit.ChpdPrimaryButton
import com.brsv44n.some_courier.presentation.ui_kit.ConfirmationBottomSheet
import com.brsv44n.some_courier.presentation.ui_kit.ErrorScreen
import com.brsv44n.some_courier.presentation.ui_kit.NavigationBackToolbar
import com.brsv44n.some_courier.presentation.ui_kit.OverlayProgress
import com.brsv44n.some_courier.presentation.ui_kit.ProgressLoader
import com.brsv44n.some_courier.presentation.ui_kit.shimmer.ShimmerType
import com.brsv44n.some_courier.presentation.ui_kit.shimmer.shimmerEffect
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.Typography
import com.brsv44n.some_courier.theme.icon
import com.brsv44n.some_courier.theme.onSurface
import com.brsv44n.some_courier.theme.strokesPrimary
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreenContent(
    modifier: Modifier = Modifier,
    component: OrderComponent,
) {
    val uiState by component.uiState.subscribeAsState()
    val isRefreshing by component.isRefreshing.subscribeAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    val isIssueButtonEnabled = uiState.isIssueButtonEnabled
    val isMapButtonEnabled = uiState.isMapButtonEnabled

    ActionModals(component = component)

    if (uiState.isOverlayProgressVisible) {
        OverlayProgress()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            OrderTopBar(
                topBarTitle = (uiState as? OrderState.Success)?.order?.number,
                titleModifier = Modifier
                    .fillMaxWidth()
                    .shimmerEffect(
                        enabled = uiState is OrderState.Loading || uiState is OrderState.Error,
                        type = ShimmerType.Horizontal,
                        cornerRadius = CornerRadius(8f, 8f)
                    ),
                onWarningClicked = {
                    component.onEvent(OrderComponent.Event.WarningClicked)
                },
                onMapClicked = {
                    component.onEvent(OrderComponent.Event.MapClicked)
                },
                onBackClick = { component.onEvent(OrderComponent.Event.BackClicked) },
                isIssueButtonEnabled = isIssueButtonEnabled,
                isMapButtonEnabled = isMapButtonEnabled
            )
        },
        containerColor = strokesPrimary
    ) { paddings ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddings.calculateTopPadding(),
                    start = paddings.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                    end = paddings.calculateEndPadding(layoutDirection = LayoutDirection.Rtl),
                )
                .pullToRefresh(
                    isRefreshing = isRefreshing,
                    state = pullToRefreshState,
                    onRefresh = { component.onEvent(OrderComponent.Event.RefreshPulled) },
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
            OrderScreenStateContent(uiState = uiState, component = component)
        }
    }
}

@Composable
private fun ActionModals(component: OrderComponent) {
    val modalState by component.modalState.subscribeAsState()
    when (modalState) {
        OrderActiveModal.NONE -> {}
        OrderActiveModal.PROBLEM -> {
            ProblemConfirmationModal(
                onConfirm = { component.onEvent(OrderComponent.Event.WarningConfirmed) },
                onDeny = { component.onEvent(OrderComponent.Event.ModalDismissed) },
            )
        }

        OrderActiveModal.FINISH -> {
            FinishOrderConfirmationModal(
                onConfirm = { component.onEvent(OrderComponent.Event.FinishOrderConfirmed) },
                onDeny = { component.onEvent(OrderComponent.Event.ModalDismissed) }
            )
        }
    }
}

@Composable
private fun BoxScope.OrderScreenStateContent(uiState: OrderState, component: OrderComponent) {
    when (uiState) {
        is OrderState.Error -> {
            ErrorScreen(
                modifier = Modifier.fillMaxSize(),
                onClick = { component.onEvent(OrderComponent.Event.RetryClicked) },
                errorType = uiState.error
            )
        }

        OrderState.Loading -> {
            ProgressLoader(modifier = Modifier.align(Alignment.Center))
        }

        is OrderState.Success -> {
            OrderContent(state = uiState, component = component)
        }
    }
}

@Composable
private fun OrderContent(state: OrderState.Success, component: OrderComponent) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier) }
            item {
                OrderDetailsCard(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    order = state.order
                )
            }
            item {
                ChangeCard(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    paymentMethod = state.order.paymentMethod,
                    price = state.order.price,
                    comment = state.order.paymentComment
                )
            }
            when (state.order.orderStatus) {
                OrderStatus.NEW -> {
                    item {
                        NewOrderContent(
                            onManagerClicked = {
                                component.onEvent(OrderComponent.Event.ManagerClicked)
                            },
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        )
                    }
                }

                OrderStatus.IN_PROGRESS -> {
                    item {
                        InProgressOrderContent(
                            onManagerClicked = {
                                component.onEvent(OrderComponent.Event.ManagerClicked)
                            },
                            onClientClicked = {
                                component.onEvent(OrderComponent.Event.ClientClicked)
                            },
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        )
                    }
                }

                else -> {}
            }
        }

        if (
            state.order.orderStatus == OrderStatus.IN_PROGRESS ||
            state.order.orderStatus == OrderStatus.NEW
        ) {
            BottomBlock(
                modifier = Modifier.fillMaxWidth()
            ) {
                ChpdPrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                        .navigationBarsPadding()
                        .imePadding(),
                    onClick = {
                        if (state.order.orderStatus == OrderStatus.IN_PROGRESS) {
                            component.onEvent(OrderComponent.Event.FinishOrderClicked)
                        } else {
                            component.onEvent(OrderComponent.Event.ChangeOrderStatusClicked)
                        }
                    },
                    enabled = !state.order.isWithIssue,
                    contentPadding = PaddingValues(vertical = 28.dp),
                    content = {
                        Text(
                            if (state.order.orderStatus == OrderStatus.IN_PROGRESS) {
                                stringResource(R.string.action_order_info_order_complete)
                            } else {
                                stringResource(R.string.action_order_info_get_order)
                            }
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProblemConfirmationModal(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDeny: () -> Unit,
) {
    ConfirmationBottomSheet(
        modifier = modifier,
        title = stringResource(R.string.title_confirmation_modal_problem),
        description = stringResource(R.string.label_confirmation_modal_problem),
        onConfirm = onConfirm,
        onDeny = onDeny
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinishOrderConfirmationModal(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDeny: () -> Unit,
) {
    ConfirmationBottomSheet(
        modifier = modifier,
        title = stringResource(R.string.title_confirmation_modal_finish_order),
        description = stringResource(R.string.label_confirmation_modal_finish_order),
        onConfirm = onConfirm,
        onDeny = onDeny
    )
}

@Composable
fun OrderTopBar(
    topBarTitle: String?,
    titleModifier: Modifier = Modifier,
    onWarningClicked: () -> Unit,
    onMapClicked: () -> Unit,
    onBackClick: () -> Unit,
    isIssueButtonEnabled: Boolean,
    isMapButtonEnabled: Boolean,
) {
    NavigationBackToolbar(
        title = {
            Text(
                modifier = titleModifier,
                text = topBarTitle.orEmpty(),
                style = Typography.titleLarge,
                color = onSurface,
                textAlign = TextAlign.Start
            )
        },
        actions = {
            WarningIconButton(
                onWarningClicked = onWarningClicked,
                isIssueButtonEnabled = isIssueButtonEnabled
            )
            MapIconButton(
                isMapButtonEnabled = isMapButtonEnabled,
                onMapClicked = onMapClicked
            )
        },
        onBackClick = onBackClick
    )
}

@Composable
fun WarningIconButton(
    isIssueButtonEnabled: Boolean,
    onWarningClicked: () -> Unit,
) {
    IconButton(
        onClick = onWarningClicked,
        enabled = isIssueButtonEnabled
    ) {
        Icon(
            modifier = Modifier
                .width(24.dp)
                .alpha(
                    if (isIssueButtonEnabled) 1f else 0.4f
                ),
            painter = painterResource(R.drawable.ic_order_warning),
            contentDescription = null,
            tint = icon
        )
    }
}

@Composable
fun MapIconButton(
    isMapButtonEnabled: Boolean,
    onMapClicked: () -> Unit,
) {
    IconButton(
        onClick = onMapClicked,
        enabled = isMapButtonEnabled
    ) {
        Icon(
            modifier = Modifier
                .width(24.dp)
                .alpha(
                    if (isMapButtonEnabled) 1f else 0.4f
                ),
            painter = painterResource(R.drawable.ic_map),
            contentDescription = null,
            tint = icon
        )
    }
}

@Composable
fun NewOrderContent(
    modifier: Modifier = Modifier,
    onManagerClicked: () -> Unit,
    contentPadding: PaddingValues,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ChpdOutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            contentPadding = PaddingValues(vertical = 16.dp),
            onClick = onManagerClicked,
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_support_agent),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(text = stringResource(R.string.action_login_support_screen_contact_manager))
                }
            }
        )
    }
}

@Composable
fun InProgressOrderContent(
    onManagerClicked: () -> Unit,
    onClientClicked: () -> Unit,
    contentPadding: PaddingValues,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ChpdOutlinedButton(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 16.dp),
            onClick = onManagerClicked,
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_support_agent),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(text = stringResource(R.string.action_order_info_call_manager))
                }
            }
        )

        ChpdOutlinedButton(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 16.dp),
            onClick = onClientClicked,
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_call),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(text = stringResource(R.string.action_order_info_call_client))
                }
            }
        )
    }
}

@Composable
@Preview
private fun PreviewOrderScreen() {
    CHPAndroidCourierTheme {
        OrderScreenContent(
            modifier = Modifier.fillMaxSize(),
            component = PreviewOrderComponent(),
        )
    }
}
