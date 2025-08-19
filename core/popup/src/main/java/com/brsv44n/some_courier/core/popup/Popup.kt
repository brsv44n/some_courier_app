package com.brsv44n.some_courier.core.popup

import com.brsv44n.some_courier.core.utils.Text

sealed class Popup(
    val title: Text,
    val message: Text,
    val type: Type,
) {
    enum class Type {
        DONE, ERROR
    }

    class Done(
        message: Text,
        title: Text = Text.Resource(R.string.title_popup_done),
    ) : Popup(title, message, Type.DONE)

    class Error(
        message: Text,
        title: Text = Text.Resource(R.string.title_popup_error),
    ) : Popup(title, message, Type.ERROR)
}
