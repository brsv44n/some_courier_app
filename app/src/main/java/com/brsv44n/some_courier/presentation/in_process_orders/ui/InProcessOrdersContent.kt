package com.brsv44n.some_courier.presentation.in_process_orders.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.in_process_orders.InProcessOrdersComponent
import com.brsv44n.some_courier.presentation.in_process_orders.OrdersInProcessState
import com.brsv44n.some_courier.presentation.in_process_orders.PreviewInProcessOrdersComponent
import com.brsv44n.some_courier.presentation.models.OrderCardUiModel
import com.brsv44n.some_courier.presentation.models.OrderStatus
import com.brsv44n.some_courier.presentation.ui_kit.ChpdOutlinedButton
import com.brsv44n.some_courier.presentation.ui_kit.ChpdPrimaryButton
import com.brsv44n.some_courier.presentation.ui_kit.ConfirmationBottomSheet
import com.brsv44n.some_courier.presentation.ui_kit.ErrorScreen
import com.brsv44n.some_courier.presentation.ui_kit.OverlayProgress
import com.brsv44n.some_courier.presentation.ui_kit.ProgressLoader
import com.brsv44n.some_courier.presentation.ui_kit.order_card.OrderCard
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.bgPrimary
import com.brsv44n.some_courier.theme.grey2
import com.brsv44n.some_courier.theme.orderComment
import com.brsv44n.some_courier.theme.serviceSuccess
import com.brsv44n.some_courier.theme.textTitle
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InProcessOrdersContent(
    modifier: Modifier = Modifier,
    component: InProcessOrdersComponent,
) {
    val ordersState by component.ordersState.subscribeAsState()
    val showModal by component.showModal.subscribeAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    if (showModal) {
        RouteClosedConfirmationModal(
            onConfirm = { component.onEvent(InProcessOrdersComponent.Event.EndRouteConfirmed) },
            onDeny = { component.onEvent(InProcessOrdersComponent.Event.ModalDismissed) }
        )
    }

    if (ordersState.isRouteStatusUpdating) {
        OverlayProgress()
    }

    Box(
        modifier = modifier
            .pullToRefresh(
                isRefreshing = ordersState.isRefreshing,
                state = pullToRefreshState,
                onRefresh = { component.onEvent(InProcessOrdersComponent.Event.RefreshPulled) },
                enabled = !ordersState.isRefreshing
            )
    ) {
        PullToRefreshDefaults.Indicator(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1f),
            state = pullToRefreshState,
            isRefreshing = ordersState.isRefreshing
        )

        when (val state = ordersState) {
            is OrdersInProcessState.Empty -> {
                CenteredText(
                    modifier = Modifier.fillMaxSize(),
                    text = stringResource(R.string.label_orders_in_process_empty),
                    style = TextStyles.AndroidBodyLarge,
                    color = orderComment
                )
            }

            is OrdersInProcessState.Error -> {
                ErrorScreen(
                    modifier = Modifier.fillMaxSize(),
                    onClick = { component.onEvent(InProcessOrdersComponent.Event.RetryClicked) },
                    errorType = state.error
                )
            }

            OrdersInProcessState.Loading -> {
                ProgressLoader(modifier = Modifier.align(Alignment.Center))
            }

            is OrdersInProcessState.Success -> {
                SuccessContent(Modifier.fillMaxSize(), state, component)
            }
        }
    }
}

@Composable
private fun CenteredText(
    modifier: Modifier,
    text: String,
    style: TextStyle,
    color: Color,
) {
    Column(
        modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, style = style, color = color)
    }
}

@Composable
private fun SuccessContent(
    modifier: Modifier,
    state: OrdersInProcessState.Success,
    component: InProcessOrdersComponent,
) {
    Column(
        modifier = modifier
    ) {
        OrdersList(
            state = state,
            onOrderClicked = { component.onEvent(InProcessOrdersComponent.Event.OrderClicked(it)) }
        )
        ActionButtons(state.ordersList.isNotEmpty(), component)
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun ColumnScope.OrdersList(
    modifier: Modifier = Modifier,
    state: OrdersInProcessState.Success,
    onOrderClicked: (Long) -> Unit,
) {
    val ordersLayoutState =
        remember(state.ordersList.size) { OrdersLayoutState(state.ordersList.size) }
    val bottomAddressYPosition = remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            StatusCanvas(
                modifier = Modifier.fillMaxHeight(),
                orders = state.ordersList,
                orderNumbersPositionsByY = ordersLayoutState.orderNumberInColumnPositionsByY,
                bottomAddressYPosition = bottomAddressYPosition
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 28.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.userAddress,
                    style = TextStyles.AndroidBodyMedium,
                    color = textTitle
                )
                state.ordersList.forEachIndexed { index, order ->
                    OrderCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { layoutCoordinates ->
                                val windowCoordinates = layoutCoordinates.positionInParent()
                                ordersLayoutState.onOrderPositioned(index, windowCoordinates.y)
                            },
                        orderCardUiModel = order,
                        onClick = { onOrderClicked(order.id) },
                        onOrderNumberPositioned = { position ->
                            ordersLayoutState.onOrderNumberPositioned(index, position)
                        }
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { layoutCoordinates ->
                            val windowCoordinates = layoutCoordinates.positionInParent()
                            bottomAddressYPosition.floatValue = windowCoordinates.y
                        },
                    text = state.userAddress,
                    style = TextStyles.AndroidBodyMedium,
                    color = textTitle
                )
            }
        }
    }
}

private class OrdersLayoutState(ordersCount: Int) {
    val orderNumbersPositionsByY = List(ordersCount) { mutableFloatStateOf(0f) }
    val orderCardPositionsByY = List(ordersCount) { mutableFloatStateOf(0f) }
    val orderNumberInColumnPositionsByY = List(ordersCount) {
        derivedStateOf { orderCardPositionsByY[it].floatValue + orderNumbersPositionsByY[it].floatValue }
    }

    fun onOrderPositioned(index: Int, position: Float) {
        orderCardPositionsByY[index].floatValue = position
    }

    fun onOrderNumberPositioned(index: Int, position: Float) {
        orderNumbersPositionsByY[index].floatValue = position
    }
}

@Composable
private fun ActionButtons(
    isVisible: Boolean,
    component: InProcessOrdersComponent,
) {
    val uiState by component.ordersState.subscribeAsState()
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        Surface(
            shape = RectangleShape,
            color = bgPrimary,
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ChpdOutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp),
                    onClick = { component.onEvent(InProcessOrdersComponent.Event.OpenRouteClicked) },
                    contentPadding = PaddingValues(16.dp),
                    content = {
                        Icon(
                            painter = painterResource(R.drawable.ic_map),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(R.string.action_orders_in_process_open_route),
                            textAlign = TextAlign.Center
                        )
                    }
                )
                if (
                    uiState is OrdersInProcessState.Success &&
                    (uiState as OrdersInProcessState.Success).showEndRouteButton
                ) {
                    ChpdPrimaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .navigationBarsPadding()
                            .imePadding(),
                        onClick = { component.onEvent(InProcessOrdersComponent.Event.EndRouteClicked) },
                        containerColor = serviceSuccess,
                        contentPadding = PaddingValues(26.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_route),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(R.string.action_orders_in_process_close_route),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusCanvas(
    modifier: Modifier,
    orders: List<OrderCardUiModel>,
    orderNumbersPositionsByY: List<State<Float>>,
    bottomAddressYPosition: State<Float>,
) {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()

    val canvasWidth = with(density) { 20.dp.toPx() }

    val icStartLocation: Painter = painterResource(id = R.drawable.ic_house_in_circle)
    val icStartedLocation: Painter = painterResource(id = R.drawable.ic_house_in_green_circle)
    val icOrderDelivered: Painter = painterResource(id = R.drawable.ic_success_small)

    val iconSize = Size(icStartLocation.intrinsicSize.width, icStartLocation.intrinsicSize.height)
    val lineStrokeWidth = with(density) { 2.dp.toPx() }
    val circleStrokeWidth = with(density) { 1.dp.toPx() }
    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), phase = 0f)

    Canvas(modifier = modifier) {
        if (orders.first().orderStatus == OrderStatus.DELIVERED) {
            drawIcon(icStartedLocation, Offset.Zero, iconSize)
        } else {
            drawIcon(icStartLocation, Offset.Zero, iconSize)
        }

        orders.forEachIndexed { index, order ->
            val orderPosY = orderNumbersPositionsByY[index].value
            val lineStartPosY = if (index == 0) {
                iconSize.height + with(density) { 2.dp.toPx() }
            } else {
                orderNumbersPositionsByY[index - 1].value + canvasWidth / 2
            }
            drawStatusLine(
                startY = lineStartPosY,
                endY = orderPosY,
                color = if (order.orderStatus == OrderStatus.DELIVERED) serviceSuccess else grey2,
                pathEffect = if (order.orderStatus == OrderStatus.DELIVERED) null else dashEffect,
                strokeWidth = lineStrokeWidth,
                canvasWidth = canvasWidth
            )
            if (index == orders.lastIndex) {
                drawStatusLine(
                    startY = orderPosY,
                    endY = bottomAddressYPosition.value,
                    color = grey2,
                    pathEffect = dashEffect,
                    strokeWidth = lineStrokeWidth,
                    canvasWidth = canvasWidth
                )
                drawIconAtPosition(
                    icStartLocation,
                    bottomAddressYPosition.value + iconSize.height / 2,
                    canvasWidth
                )
            }

            if (order.orderStatus == OrderStatus.DELIVERED) {
                drawIconAtPosition(icOrderDelivered, orderPosY, canvasWidth)
            } else {
                drawOrderCircle(orderPosY, canvasWidth, circleStrokeWidth)
                drawOrderText(index, orderPosY, canvasWidth, textMeasurer)
            }
        }
    }
}

private fun DrawScope.drawIcon(painter: Painter, offset: Offset, size: Size) {
    translate(top = offset.y) {
        with(painter) {
            draw(size = size, alpha = 1f)
        }
    }
}

private fun DrawScope.drawStatusLine(
    startY: Float,
    endY: Float,
    color: Color,
    pathEffect: PathEffect?,
    strokeWidth: Float,
    canvasWidth: Float,
) {
    drawLine(
        color = color,
        strokeWidth = strokeWidth,
        start = Offset(x = canvasWidth / 2, y = startY),
        end = Offset(x = canvasWidth / 2, y = endY),
        pathEffect = pathEffect
    )
}

private fun DrawScope.drawIconAtPosition(painter: Painter, posY: Float, canvasWidth: Float) {
    translate(top = posY - canvasWidth / 2) {
        with(painter) {
            draw(
                size = Size(intrinsicSize.width, intrinsicSize.height),
                alpha = 1f,
                colorFilter = ColorFilter.tint(
                    color = Color.Transparent,
                    blendMode = BlendMode.SrcOver
                )
            )
        }
    }
}

private fun DrawScope.drawOrderCircle(posY: Float, canvasWidth: Float, strokeWidth: Float) {
    drawCircle(
        color = bgPrimary,
        radius = canvasWidth / 2,
        center = Offset(x = canvasWidth / 2, y = posY),
        style = Stroke(width = strokeWidth),
        blendMode = BlendMode.SrcOver
    )
    drawCircle(
        color = grey2,
        radius = canvasWidth / 2 - strokeWidth,
        center = Offset(x = canvasWidth / 2, y = posY),
        blendMode = BlendMode.SrcOver
    )
}

private fun DrawScope.drawOrderText(
    index: Int,
    posY: Float,
    canvasWidth: Float,
    textMeasurer: TextMeasurer,
) {
    val measuredText = textMeasurer.measure(
        AnnotatedString((index + 1).toString()),
        style = TextStyles.AndroidLabelMediumProminent
    )
    drawText(
        measuredText,
        color = bgPrimary,
        topLeft = Offset(
            x = canvasWidth / 2 - measuredText.size.width / 2,
            y = posY - measuredText.size.height / 2
        ),
        blendMode = BlendMode.SrcOver
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RouteClosedConfirmationModal(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDeny: () -> Unit,
) {
    ConfirmationBottomSheet(
        modifier = modifier,
        title = stringResource(R.string.title_confirmation_modal_end_route),
        description = stringResource(R.string.label_confirmation_modal_end_route),
        onConfirm = onConfirm,
        onDeny = onDeny,
        confirmButtonText = stringResource(R.string.action_confirmation_modal_yes),
        denyButtonText = stringResource(R.string.action_confirmation_modal_no)
    )
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
private fun PreviewInProcessOrdersContent() {
    CHPAndroidCourierTheme {
        InProcessOrdersContent(
            modifier = Modifier.fillMaxSize(),
            component = PreviewInProcessOrdersComponent()
        )
    }
}
