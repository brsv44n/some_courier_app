package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.buttonOutlinedContentDefault

@Composable
fun ProgressLoader(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        modifier = modifier,
        strokeWidth = 4.dp,
        color = buttonOutlinedContentDefault,
    )
}

@Composable
@Preview
private fun PreviewProgressLoader() {
    CHPAndroidCourierTheme {
        ProgressLoader()
    }
}
