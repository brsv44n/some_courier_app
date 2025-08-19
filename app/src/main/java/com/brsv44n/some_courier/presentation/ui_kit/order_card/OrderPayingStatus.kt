package com.brsv44n.some_courier.presentation.ui_kit.order_card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.presentation.models.PaymentMethod
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.paymentMethodBgPrimary
import com.brsv44n.some_courier.theme.paymentMethodText

@Composable
internal fun OrderPayingStatus(
    modifier: Modifier = Modifier,
    paymentMethod: PaymentMethod,
) {
    Row(
        modifier = modifier
            .background(color = paymentMethodBgPrimary, shape = RoundedCornerShape(40.dp))
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Text(
            text = stringResource(paymentMethod.resId),
            style = TextStyles.AndroidBodySmall,
            color = paymentMethodText
        )
    }
}

@Composable
@Preview
private fun PreviewOrderPayingStatus() {
    CHPAndroidCourierTheme {
        Column {
            PaymentMethod.entries.forEach {
                OrderPayingStatus(
                    modifier = Modifier,
                    paymentMethod = it
                )
            }
        }
    }
}
