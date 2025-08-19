package com.brsv44n.some_courier.presentation.ui_kit.order_card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.presentation.models.OrderStatus
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.orderStatusBgCanceled
import com.brsv44n.some_courier.theme.orderStatusBgDelivered
import com.brsv44n.some_courier.theme.orderStatusBgInProgress
import com.brsv44n.some_courier.theme.orderStatusBgNew

@Composable
internal fun OrderStatusItem(
    modifier: Modifier = Modifier,
    orderStatus: OrderStatus,
) {

    Row(
        modifier = modifier
            .background(
                color =
                when (orderStatus) {
                    OrderStatus.NEW -> orderStatusBgNew
                    OrderStatus.IN_PROGRESS -> orderStatusBgInProgress
                    OrderStatus.DELIVERED -> orderStatusBgDelivered
                    OrderStatus.CANCELED -> orderStatusBgCanceled
                },
                shape = RoundedCornerShape(40.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)

    ) {
        Text(
            text = stringResource(orderStatus.resId),
            style = TextStyles.AndroidBodySmall,
            color = Color.White
        )
    }
}

@Composable
@Preview
private fun PreviewOrderStatus() {
    CHPAndroidCourierTheme {
        Column {
            OrderStatus.entries.forEach {
                OrderStatusItem(
                    modifier = Modifier,
                    orderStatus = it
                )
            }
        }
    }
}
