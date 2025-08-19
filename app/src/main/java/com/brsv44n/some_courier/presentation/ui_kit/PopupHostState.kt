package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

@Stable
class PopupHostState {

    private val mutex = Mutex()

    var currentPopupData by mutableStateOf<PopupData?>(null)
        private set

    suspend fun showPopup(
        title: String,
        message: String,
        iconRes: Int?,
        backgroundColor: Color,
        duration: Long
    ): PopupResult =
        showPopup(PopupVisualsImpl(title, message, iconRes, backgroundColor, duration))

    suspend fun showPopup(visuals: PopupVisuals): PopupResult = mutex.withLock {
        try {
            return@withLock suspendCancellableCoroutine { continuation ->
                currentPopupData = PopupDataImpl(visuals, continuation)
            }
        } finally {
            currentPopupData = null
        }
    }

    private data class PopupVisualsImpl(
        override val title: String,
        override val message: String,
        override val iconRes: Int?,
        override val backgroundColor: Color,
        override val duration: Long
    ) : PopupVisuals

    private data class PopupDataImpl(
        override val visuals: PopupVisuals,
        private val continuation: CancellableContinuation<PopupResult>
    ) : PopupData {
        override fun dismiss() {
            if (continuation.isActive) continuation.resume(PopupResult.Dismissed)
        }
    }
}

@Composable
fun PopupHost(
    hostState: PopupHostState,
    modifier: Modifier = Modifier,
    popup: @Composable (PopupData) -> Unit = {
        Popup(
            title = it.visuals.title,
            message = it.visuals.message,
            icon = it.visuals.iconRes?.let { res -> painterResource(id = res) },
            backgroundColor = it.visuals.backgroundColor,
            onDismiss = { it.dismiss() }
        )
    }
) {
    val currentPopupData = hostState.currentPopupData
    LaunchedEffect(currentPopupData) {
        if (currentPopupData != null) {
            delay(currentPopupData.visuals.duration)
            currentPopupData.dismiss()
        }
    }
    Crossfade(
        modifier = modifier,
        targetState = currentPopupData,
        label = "popup"
    ) { data -> data?.let { popup.invoke(it) } }
}

enum class PopupResult {
    Dismissed
}

@Stable
interface PopupVisuals {
    val title: String
    val message: String
    val iconRes: Int?
    val backgroundColor: Color
    val duration: Long
}

@Stable
interface PopupData {
    val visuals: PopupVisuals

    fun dismiss()
}
