package com.brsv44n.some_courier.core.popup

import com.brsv44n.some_courier.core.utils.Event
import com.brsv44n.some_courier.core.utils.Text
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MessageNotifierImpl : MessageNotifier {

    override val popupFlow: LatestSubscriberFirstFlow<Event<Popup>>
        get() = _popupFlow

    private val _popupFlow = LatestSubscriberFirstFlow<Event<Popup>>(replay = 1)

    private val scope = CoroutineScope(SupervisorJob())

    override fun showDone(message: Text, title: Text?) {
        val popup = if (title == null) Popup.Done(message) else Popup.Done(message, title)
        scope.launch { _popupFlow.emit(Event(popup)) }
    }

    override fun showError(message: Text, title: Text?) {
        val popup = if (title == null) Popup.Error(message) else Popup.Error(message, title)
        scope.launch { _popupFlow.emit(Event(popup)) }
    }
}
