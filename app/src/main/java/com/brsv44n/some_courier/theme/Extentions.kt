package com.brsv44n.some_courier.theme

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.conditional(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier
): Modifier = composed {
    if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}
