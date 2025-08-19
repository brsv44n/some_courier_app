package com.brsv44n.some_courier.presentation.ui_kit

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.core.android_utils.asString
import com.brsv44n.some_courier.core.popup.LatestSubscriberFirstFlow
import com.brsv44n.some_courier.core.popup.MessageNotifier
import com.brsv44n.some_courier.core.popup.Popup
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun Popup(notifier: MessageNotifier) {
    val popupState = remember { PopupHostState() }
    val context = LocalContext.current

    notifier.popupFlow.collectWhenResumed {
        it.getContentIfNotHandled()?.let { popup ->
            popupState.currentPopupData?.dismiss()
            popupState.showPopup(
                popup.title.asString(context),
                popup.message.asString(context),
                popupIconProvider.invoke(popup.type),
                Color(0xFF404050).copy(alpha = 0.8f),
                3000L
            )
        }
    }

    PopupHost(
        modifier = Modifier
            .statusBarsPadding()
            .padding(8.dp)
            .zIndex(10f),
        hostState = popupState
    )
}

val popupIconProvider: (Popup.Type) -> Int = {
    when (it) {
        Popup.Type.DONE -> R.drawable.ic_popup_done
        Popup.Type.ERROR -> R.drawable.ic_popup_error
    }
}

@Composable
private fun <T> LatestSubscriberFirstFlow<T>.collectWhenResumed(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEach: suspend (T) -> Unit
) {
    val flow: Flow<T> = this.subscribe()

    val currentOnEach by rememberUpdatedState(onEach)
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(lifecycleOwner, flow) {
        var collectionJob: Job? = null

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                collectionJob = coroutineScope.launch {
                    flow.collectLatest { value ->
                        currentOnEach(value)
                    }
                }
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                collectionJob?.cancel()
                collectionJob = null
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            collectionJob?.cancel()
            this@collectWhenResumed.unsubscribe(flow)
        }
    }
}
