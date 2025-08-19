package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.brsv44n.some_courier.theme.TextStyles
import com.brsv44n.some_courier.theme.bgPrimary
import kotlin.math.roundToInt

private enum class DragPosition {
    Start, End
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Popup(
    title: String,
    message: String,
    icon: Painter?,
    backgroundColor: Color,
    onDismiss: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        var popupHeight by remember { mutableIntStateOf(0) }
        val density = LocalDensity.current
        val topPadding = WindowInsets.statusBars.getTop(density) + with(density) { 8.dp.toPx() }
        val state = remember {
            AnchoredDraggableState(
                initialValue = DragPosition.Start,
                positionalThreshold = { distance ->
                    distance * 0.5f
                },
                anchors = DraggableAnchors {
                    DragPosition.Start at 0f
                    DragPosition.End at -1f
                },
                velocityThreshold = { with(density) { 100.dp.toPx() } },
                snapAnimationSpec = tween(),
                decayAnimationSpec = exponentialDecay()
            )
        }
        SideEffect {
            state.dispatchRawDelta(0f)
        }
        LaunchedEffect(state.currentValue) {
            if (state.currentValue == DragPosition.End) {
                onDismiss()
            }
        }
        LaunchedEffect(popupHeight) {
            state.updateAnchors(
                DraggableAnchors {
                    DragPosition.Start at 0f
                    DragPosition.End at -(popupHeight.toFloat() + topPadding)
                }
            )
        }
        Surface(
            modifier = Modifier
                .onSizeChanged { popupHeight = it.height }
                .wrapContentSize()
                .offset {
                    IntOffset(
                        x = 0,
                        y = state
                            .requireOffset()
                            .roundToInt()
                    )
                }
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Vertical
                ),
            color = backgroundColor,
            shape = RoundedCornerShape(8.dp)
        ) {
            CompositionLocalProvider(LocalContentColor provides bgPrimary) {
                Row(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    icon?.let {
                        Row {
                            Icon(
                                painter = it,
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = title,
                            color = LocalContentColor.current,
                            style = TextStyles.AndroidLabelMediumProminent
                        )
                        Text(
                            modifier = Modifier.padding(top = 3.dp),
                            text = message,
                            color = LocalContentColor.current,
                            style = TextStyles.AndroidBodySmall
                        )
                    }
                    IconButton(
                        modifier = Modifier.size(28.dp),
                        onClick = onDismiss
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.ic_close_thin),
                            contentDescription = null,
                            tint = bgPrimary
                        )
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 320)
@Composable
private fun PopupContentPreview() {
    CHPAndroidCourierTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Popup(
                "Title",
                "Message",
                painterResource(id = R.drawable.ic_popup_done),
                Color(0xFF413E42).copy(alpha = 0.8f)
            )
            Popup(
                "Title",
                "Message",
                painterResource(id = R.drawable.ic_popup_error),
                Color(0xFF413E42).copy(alpha = 0.8f)
            )
        }
    }
}
