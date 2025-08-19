package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.buttonFilledBgDisabled
import com.brsv44n.some_courier.theme.buttonFilledContentDisabled
import com.brsv44n.some_courier.theme.buttonFilledContentEnabled
import com.brsv44n.some_courier.theme.buttonOutlinedContentDefault
import com.brsv44n.some_courier.theme.buttonOutlinedStrokeEnabled

@Composable
fun ChpdPrimaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showLoading: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 18.dp),
    shape: Shape = RoundedCornerShape(12.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentColor: Color = buttonFilledContentEnabled,
    disabledContentColor: Color = buttonFilledContentDisabled,
    containerColor: Color = buttonOutlinedContentDefault,
    disabledContainerColor: Color = buttonFilledBgDisabled,
    loadingContainerColor: Color = buttonFilledBgDisabled,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = enabled && !showLoading,
        shape = shape,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (showLoading) {
                disabledContentColor
            } else {
                containerColor
            },
            disabledContainerColor = if (showLoading) {
                loadingContainerColor
            } else {
                disabledContainerColor
            },
            contentColor = contentColor,
        )
    ) {
        content()
    }
}

@Composable
fun ChpdBorderlessButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 28.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    contentColor: Color = buttonOutlinedContentDefault,
    disabledContentColor: Color = buttonFilledContentDisabled,

    shape: Shape = RoundedCornerShape(12.dp),
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor,
            disabledContentColor = disabledContentColor,
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        shape = shape
    ) {
        content()
    }
}

@Composable
fun ChpdOutlinedButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(10.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    contentColor: Color = buttonOutlinedContentDefault,
    disabledContentColor: Color = buttonFilledContentDisabled,
    shape: Shape = RoundedCornerShape(12.dp),
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor,
            disabledContentColor = disabledContentColor,
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        shape = shape,
        border = BorderStroke(
            width = 1.dp,
            color = buttonOutlinedStrokeEnabled
        )
    ) {
        content()
    }
}

@Composable
@Preview
private fun PreviewBorderlessButton() {
    CHPAndroidCourierTheme {

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ChpdPrimaryButton(onClick = {}) { Text("primary button") }
            ChpdBorderlessButton(onClick = { }) {
                Text("borderless button")
            }
            ChpdOutlinedButton(onClick = { }) {
                Text("outlined button")
            }
        }
    }
}
