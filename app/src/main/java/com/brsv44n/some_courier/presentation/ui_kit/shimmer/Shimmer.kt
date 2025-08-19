package com.brsv44n.some_courier.presentation.ui_kit.shimmer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize

fun Modifier.shimmerEffect(
    enabled: Boolean,
    type: ShimmerType = ShimmerType.Linear,
    cornerRadius: CornerRadius = CornerRadius.Zero,
): Modifier = composed {

    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translationMultiplier by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 3000, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )

    val shimmerAlpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "shimmerAlpha"
    )

    drawWithContent {
        val brush = getShimmerBrush(type, translationMultiplier, size)

        drawContent()
        drawRoundRect(
            color = Color(0xFFFCFCFC),
            alpha = shimmerAlpha,
            cornerRadius = cornerRadius
        )
        drawRoundRect(
            brush = brush,
            alpha = shimmerAlpha,
            cornerRadius = cornerRadius
        )
    }
}

fun Modifier.shimmerEffect(
    type: ShimmerType = ShimmerType.Linear,
    shape: Shape = RectangleShape
): Modifier = composed {

    var size by remember { mutableStateOf(IntSize.Zero) }

    return@composed onGloballyPositioned { size = it.size }
        .shimmerBackground(type, shape, size)
}

private fun Modifier.shimmerBackground(
    type: ShimmerType,
    shape: Shape,
    size: IntSize
): Modifier = composed {

    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translationMultiplier by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 3000, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )

    val brush = getShimmerBrush(type, translationMultiplier, size.toSize())

    return@composed background(brush, shape)
}

private fun getShimmerBrush(type: ShimmerType, translationMultiplier: Float, size: Size): Brush {
    val shimmerColors = listOf(
        Color(0x00F2F2F7),
        Color(0xFFF2F2F7),
        Color(0x00F2F2F7),
    )

    return when (type) {
        ShimmerType.Linear -> {
            Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(2 * translationMultiplier * size.width, 0f),
                end = Offset(2 * translationMultiplier * size.width + size.width, size.height),
                tileMode = TileMode.Mirror
            )
        }

        ShimmerType.Horizontal -> {
            Brush.horizontalGradient(
                colors = shimmerColors,
                startX = 2 * translationMultiplier * size.width,
                endX = 2 * translationMultiplier * size.width + size.width,
                tileMode = TileMode.Mirror
            )
        }
    }
}
