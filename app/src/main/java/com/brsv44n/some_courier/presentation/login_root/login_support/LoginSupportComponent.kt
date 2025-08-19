package com.brsv44n.some_courier.presentation.login_root.login_support

interface LoginSupportComponent {

    fun onEvent(event: Event)

    sealed interface Event {
        data object CallManagerClicked : Event
        data object BackClicked : Event
    }

    sealed interface Output {
        data object Exit : Output
    }
}
