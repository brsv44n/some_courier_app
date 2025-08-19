package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.bgPrimary
import com.brsv44n.some_courier.theme.onSurface
import com.brsv44n.some_courier.theme.outline
import com.brsv44n.some_courier.theme.secondaryContainer

@Composable
internal fun TabSwitch(
    modifier: Modifier = Modifier,
    selectedTab: Int,
    leftTabText: String,
    rightTabText: String,
    onSelect: (Int) -> Unit = {},
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .border(
                width = 1.dp,
                color = outline,
                shape = RoundedCornerShape(100.dp)
            )
            .background(
                shape = RoundedCornerShape(100.dp),
                color = Color.Transparent,
            )
            .height(intrinsicSize = IntrinsicSize.Max),
    ) {
        TabItem(
            modifier = Modifier.weight(1f),
            text = leftTabText,
            isSelected = selectedTab == 0,
            onClick = { onSelect(0) }
        )
        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            thickness = 1.dp,
            color = outline
        )
        TabItem(
            modifier = Modifier.weight(1f),
            text = rightTabText,
            isSelected = selectedTab == 1,
            onClick = { onSelect(1) }
        )
    }
}

@Composable
fun TabItem(
    modifier: Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val targetBackgroundColor = if (isSelected) secondaryContainer else bgPrimary
    val animatedBackgroundColor by animateColorAsState(targetValue = targetBackgroundColor)

    Box(
        modifier = modifier
            .background(animatedBackgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = onSurface,
        )
    }
}

@Preview(widthDp = 360, showBackground = true)
@Composable
private fun PreviewTabSwitch() {
    CHPAndroidCourierTheme {
        var selected by remember { mutableIntStateOf(0) }
        var clicks by remember { mutableIntStateOf(0) }

        Box(modifier = Modifier.fillMaxSize()) {
            TabSwitch(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                selectedTab = selected,
                leftTabText = stringResource(R.string.action_switch_tab_in_process_with_digit, clicks),
                rightTabText = stringResource(R.string.action_switch_tab_new_with_digit, clicks),
                onSelect = { current ->
                    selected = if (current == 0) 0 else 1
                    clicks++
                }
            )
        }
    }
}
