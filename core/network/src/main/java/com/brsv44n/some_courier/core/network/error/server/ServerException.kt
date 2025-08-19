package com.brsv44n.some_courier.core.network.error.server

import com.brsv44n.some_courier.core.network.error.ApiError

open class ServerException(val response: String? = null, override val code: Int) : ApiError()
