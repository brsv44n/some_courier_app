package com.brsv44n.some_courier.core.popup

import com.brsv44n.some_courier.core.utils.Event
import com.brsv44n.some_courier.core.utils.Text

interface MessageNotifier {

    val popupFlow: LatestSubscriberFirstFlow<Event<Popup>>
        get() = LatestSubscriberFirstFlow()

    fun showDone(message: Text, title: Text? = null) {}

    fun showError(message: Text, title: Text? = null) {}

    fun showDone(message: String, title: String? = null) {
        showDone(Text.Simple(message), title?.let { Text.Simple(it) })
    }
}
