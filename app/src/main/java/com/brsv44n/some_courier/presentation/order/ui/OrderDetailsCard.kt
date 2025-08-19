package com.brsv44n.some_courier.presentation.order.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.presentation.models.OrderStatus
import com.brsv44n.some_courier.presentation.models.OrderUiModel
import com.brsv44n.some_courier.presentation.models.PaymentMethod
import com.brsv44n.some_courier.presentation.ui_kit.order_card.OrderPayingStatus
import com.brsv44n.some_courier.presentation.ui_kit.order_card.OrderStatusItem
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.bgPrimary
import com.brsv44n.some_courier.theme.orderComment
import com.brsv44n.some_courier.theme.strokesPrimary
import com.brsv44n.some_courier.theme.textTitle

@Composable
fun OrderDetailsCard(
    modifier: Modifier = Modifier,
    order: OrderUiModel,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgPrimary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OrderStatusItem(
                    modifier = Modifier,
                    orderStatus = order.orderStatus
                )
                IssueMark(
                    modifier = Modifier,
                    isWithIssue = order.isWithIssue
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    modifier = Modifier.weight(1f),
                    text = order.address.orEmpty(),
                    textAlign = TextAlign.Start,
                    style = TextStyles.AndroidBodyLarge,
                    color = textTitle
                )

                Spacer(modifier = Modifier.width(24.dp))

                order.time?.let {
                    Text(
                        modifier = Modifier,
                        text = it,
                        textAlign = TextAlign.Start,
                        style = TextStyles.AndroidBodyLarge,
                        color = textTitle
                    )
                }

            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp,
                color = strokesPrimary
            )
            order.comment?.let {
                InfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.label_order_info_comment),
                    value = order.comment
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp,
                color = strokesPrimary
            )
            InfoRow(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.label_order_info_client),
                value = order.clientName
            )
        }
    }
}

@Composable
fun ChangeCard(
    modifier: Modifier = Modifier,
    paymentMethod: PaymentMethod,
    price: String,
    comment: String?,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgPrimary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = price,
                    textAlign = TextAlign.Start,
                    style = TextStyles.AndroidBodyLarge,
                    color = textTitle
                )
                Spacer(modifier = Modifier.width(8.dp))
                OrderPayingStatus(
                    paymentMethod = paymentMethod
                )
            }

            if (paymentMethod == PaymentMethod.CASH) {
                comment?.let {
                    HorizontalDivider(
                        modifier = Modifier.padding(top = 18.dp, bottom = 12.dp),
                        thickness = 1.dp,
                        color = strokesPrimary
                    )
                    InfoRow(
                        modifier = Modifier.fillMaxWidth(),
                        label = stringResource(R.string.label_order_info_change),
                        value = comment
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    modifier: Modifier,
    label: String,
    value: String,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = label,
            textAlign = TextAlign.Start,
            style = TextStyles.AndroidLabelMediumProminent,
            color = orderComment
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            textAlign = TextAlign.Start,
            style = TextStyles.AndroidBodyLarge,
            color = textTitle
        )
    }
}

@Composable
@Preview
private fun PreviewOrderDetailsCard() {
    CHPAndroidCourierTheme {
        OrderDetailsCard(
            modifier = Modifier,
            order = OrderUiModel(
                id = 1,
                address = "Московский проспект, 24, этаж 11, кв. 77",
                number = "#J722",
                comment = "Как будете на месте — позвоните. Выйду, встречу",
                orderStatus = OrderStatus.NEW,
                paymentMethod = PaymentMethod.CASH,
                time = "11:30",
                price = "1000",
                clientName = "Иван",
                paymentComment = null,
                isWithIssue = false,
            )
        )
    }
}

@Composable
@Preview
fun PreviewPaymentCard(
    modifier: Modifier = Modifier,

    ) {
    CHPAndroidCourierTheme {
        ChangeCard(
            modifier = modifier,
            paymentMethod = PaymentMethod.CASH,
            comment = "Как будете на месте — позвоните. Выйду, встречу",
            price = "1000000000"
        )
    }
}
