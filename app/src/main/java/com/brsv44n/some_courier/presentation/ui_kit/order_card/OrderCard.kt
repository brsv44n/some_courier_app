package com.brsv44n.some_courier.presentation.ui_kit.order_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.models.OrderCardUiModel
import com.brsv44n.some_courier.presentation.models.OrderStatus
import com.brsv44n.some_courier.presentation.models.PaymentMethod
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.bgPrimary
import com.brsv44n.some_courier.theme.onSurface
import com.brsv44n.some_courier.theme.onSurfaceVariant
import com.brsv44n.some_courier.theme.orderComment
import com.brsv44n.some_courier.theme.serviceColorsWarning
import com.brsv44n.some_courier.theme.strokesPrimary
import com.brsv44n.some_courier.theme.textTitle

@Composable
internal fun OrderCard(
    modifier: Modifier = Modifier,
    orderCardUiModel: OrderCardUiModel,
    onClick: () -> Unit,
    onOrderNumberPositioned: (Float) -> Unit = {},
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            contentColor = onSurface,
            containerColor = bgPrimary,
            disabledContentColor = onSurfaceVariant,
            disabledContainerColor = bgPrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        onClick = onClick
    ) {
        val padding = 16.dp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OrderStatusItem(
                    orderStatus = orderCardUiModel.orderStatus
                )
                OrderPayingStatus(
                    paymentMethod = orderCardUiModel.paymentMethod
                )
                Spacer(modifier = Modifier.weight(1f))
                if (orderCardUiModel.isWarning) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_order_warning),
                        tint = serviceColorsWarning,
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            var numberOffsetY by remember { mutableFloatStateOf(0f) }
            val density = LocalDensity.current
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { layoutCoordinates ->
                        val windowCoordinates = layoutCoordinates.positionInParent()
                        numberOffsetY = windowCoordinates.y + with(density) { padding.toPx() }
                        onOrderNumberPositioned(numberOffsetY)
                    },
                text = orderCardUiModel.number,
                textAlign = TextAlign.Start,
                style = TextStyles.AndroidTitleMedium,
                color = textTitle
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    modifier = Modifier.weight(1f),
                    text = orderCardUiModel.address.orEmpty(),
                    textAlign = TextAlign.Start,
                    style = TextStyles.AndroidBodyLarge,
                    color = textTitle
                )

                Spacer(modifier = Modifier.width(24.dp))

                orderCardUiModel.time?.let {
                    Text(
                        modifier = Modifier,
                        text = it,
                        textAlign = TextAlign.Start,
                        style = TextStyles.AndroidBodyLarge,
                        color = textTitle
                    )
                }
            }

            orderCardUiModel.comment?.let {

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = strokesPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it,
                    textAlign = TextAlign.Start,
                    style = TextStyles.AndroidBodyMedium,
                    color = orderComment
                )
            }
        }
    }
}

@Composable
@Preview
private fun PreviewOrderCard() {
    CHPAndroidCourierTheme {
        Column {

            OrderCard(
                modifier = Modifier,
                orderCardUiModel = OrderCardUiModel(
                    id = 1,
                    address = "Московский проспект, 24, этаж 11, кв. 77",
                    number = "#J722",
                    comment = "Как будете на месте — позвоните. Выйду, встречу",
                    orderStatus = OrderStatus.NEW,
                    paymentMethod = PaymentMethod.CASH,
                    isWarning = false,
                    time = "21.02"
                ),
                onClick = {}
            )

            OrderCard(
                modifier = Modifier,
                orderCardUiModel = OrderCardUiModel(
                    id = 1,
                    address = "Московский проспект, 24, этаж 11, кв. 77",
                    number = "#J722",
                    comment = null,
                    orderStatus = OrderStatus.NEW,
                    paymentMethod = PaymentMethod.CASH,
                    isWarning = true,
                    time = "22.02"
                ),
                onClick = {}
            )
        }
    }
}
