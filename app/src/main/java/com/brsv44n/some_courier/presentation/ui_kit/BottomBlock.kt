package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.bgPrimary

@Composable
fun BottomBlock(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RectangleShape,
        color = bgPrimary,
        shadowElevation = 3.dp
    ) {
        content()
    }
}

@Preview
@Composable
private fun BottomBlockPreview() {
    CHPAndroidCourierTheme {
        BottomBlock(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(10.dp)
                    .background(color = Color.Cyan)
            )
        }
    }
}
