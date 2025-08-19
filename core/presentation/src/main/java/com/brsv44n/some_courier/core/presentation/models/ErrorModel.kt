package com.brsv44n.some_courier.core.presentation.models

import com.brsv44n.some_courier.core.presentation.ImageValue
import com.brsv44n.some_courier.core.presentation.R
import com.brsv44n.some_courier.core.utils.Text

sealed class ErrorModel(
    open val icon: ImageValue,
    open val title: Text,
    open val description: Text
) {

    data object NoInternet : ErrorModel(
        icon = ImageValue.Resource(R.drawable.ic_error_network),
        title = Text.Resource(R.string.error_title_network),
        description = Text.Resource(R.string.error_message_network)
    )

    data object Unknown : ErrorModel(
        icon = ImageValue.Resource(R.drawable.ic_error_unknown),
        title = Text.Resource(R.string.error_title_unknown),
        description = Text.Resource(R.string.error_message_unknown)
    )

}
