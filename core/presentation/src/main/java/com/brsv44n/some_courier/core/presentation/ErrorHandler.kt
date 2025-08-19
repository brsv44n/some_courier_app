package com.brsv44n.some_courier.core.presentation

import com.brsv44n.some_courier.core.presentation.models.ErrorModel
import com.brsv44n.some_courier.core.utils.Text

interface ErrorHandler {

    fun getErrorMessage(error: Throwable): Text

    fun getErrorModel(error: Throwable): ErrorModel
}
