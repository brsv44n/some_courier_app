package com.brsv44n.some_courier.core.network.error

import java.io.IOException

open class ApiError(
    override val message: String? = null,
    open val code: Int? = null,
) : IOException(message)
