package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.bgToast
import com.brsv44n.some_courier.theme.strokeFocused

@Composable
internal fun NamedRow(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    name: String,
    text: String
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = name,
            style = TextStyles.AndroidLabelMediumProminent,
            color = strokeFocused
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = textModifier,
            text = text,
            style = TextStyles.AndroidBodyLarge,
            color = bgToast
        )
    }
}

@Composable
@Preview
private fun PreviewNamedRow() {
    CHPAndroidCourierTheme {
        NamedRow(
            name = "Title",
            text = "Some text should be here"
        )
    }
}
