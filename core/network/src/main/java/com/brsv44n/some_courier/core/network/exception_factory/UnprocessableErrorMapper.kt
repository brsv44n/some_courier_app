package com.brsv44n.some_courier.core.network.exception_factory

import com.brsv44n.some_courier.core.network.error.ApiError
import com.brsv44n.some_courier.core.network.error.client.UnprocessableContentException

interface UnprocessableErrorMapper {

    operator fun invoke(error: UnprocessableContentException): ApiError?
}
