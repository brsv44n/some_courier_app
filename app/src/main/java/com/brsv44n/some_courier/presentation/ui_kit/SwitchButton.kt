package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme

@Composable
fun SwitchButton(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isEnabled: Boolean = true,
) {
    val colors = SwitchColors(
        checkedThumbColor = Color(0xFF381E72),
        checkedTrackColor = Color(0xFFD0BCFF),
        checkedBorderColor = Color(0xFFE0E0E0),
        checkedIconColor = Color(0xFFE0E0E0),
        uncheckedThumbColor = Color(0xFF7E8294),
        uncheckedTrackColor = Color(0xFFF6F6F9),
        uncheckedBorderColor = Color(0xFF7E8294),
        uncheckedIconColor = Color(0xFFE0E0E0),
        disabledCheckedIconColor = Color(0xFFFFFFFF),
        disabledCheckedThumbColor = Color(0xFFFFFFFF),
        disabledCheckedTrackColor = Color(0xFFDCDFF0),
        disabledCheckedBorderColor = Color(0xFFE0E0E0),
        disabledUncheckedIconColor = Color(0xFFFFFFFF),
        disabledUncheckedThumbColor = Color(0xFFA5ABB3),
        disabledUncheckedTrackColor = Color(0xFFFFFFFF),
        disabledUncheckedBorderColor = Color(0xFFA5ABB3)
    )

    Switch(
        modifier = modifier,
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        colors = colors,
        enabled = isEnabled
    )
}

@Preview
@Composable
private fun PreviewSwitchTrue() {
    CHPAndroidCourierTheme {
        var isChecked by remember { mutableStateOf(true) }
        Box(modifier = Modifier.fillMaxSize()) {
            SwitchButton(
                modifier = Modifier.align(Alignment.Center),
                isChecked = isChecked,
                isEnabled = true,
                onCheckedChange = { isChecked = !isChecked }
            )
        }
    }
}
