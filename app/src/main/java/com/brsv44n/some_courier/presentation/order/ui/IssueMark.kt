package com.brsv44n.some_courier.presentation.order.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.serviceColorsWarning

@Composable
fun IssueMark(
    modifier: Modifier = Modifier,
    isWithIssue: Boolean,
) {
    Row(
        modifier = modifier.alpha(if (isWithIssue) 1f else 0f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(R.drawable.ic_order_warning),
            contentDescription = null,
            tint = serviceColorsWarning
        )
        Text(
            modifier = Modifier.padding(start = 5.dp),
            text = stringResource(R.string.label_order_info_with_issue),
            style = TextStyles.AndroidBodySmall,
            color = serviceColorsWarning,
        )
    }
}

@Composable
@Preview
private fun PreviewIssueMark() {
    CHPAndroidCourierTheme {
        IssueMark(
            modifier = Modifier,
            isWithIssue = true
        )
    }
}
