package com.brsv44n.some_courier.core.network.error.client

import com.brsv44n.some_courier.core.network.error.ApiError

open class UnprocessableContentException(
    val errors: Map<String, List<Error>>
) : ApiError(errors.entries.firstOrNull()?.value?.firstOrNull()?.message)

data class Error(
    val code: String,
    val message: String
)
