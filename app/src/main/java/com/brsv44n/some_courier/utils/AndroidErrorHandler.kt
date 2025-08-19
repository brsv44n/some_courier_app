package com.brsv44n.some_courier.utils

import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.core.di.annotations.Singleton
import com.brsv44n.some_courier.core.network.error.ApiError
import com.brsv44n.some_courier.core.network.error.NetworkUnavailable
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.presentation.models.ErrorModel
import com.brsv44n.some_courier.core.utils.Text
import me.tatarka.inject.annotations.Inject

@Singleton
@Inject
class AndroidErrorHandler : ErrorHandler {

    override fun getErrorMessage(error: Throwable): Text = when (error) {
        is NetworkUnavailable -> Text.Resource(R.string.title_error_screen_no_internet)
        is ApiError -> error.message?.let(Text::Simple) ?: Text.Resource(R.string.error_api_unknown)
        else -> Text.Resource(R.string.unknown_error)
    }

    override fun getErrorModel(error: Throwable): ErrorModel = when (error) {
        is NetworkUnavailable -> ErrorModel.NoInternet
        else -> ErrorModel.Unknown
    }
}
