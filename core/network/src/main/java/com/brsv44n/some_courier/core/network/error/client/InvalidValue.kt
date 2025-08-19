package com.brsv44n.some_courier.core.network.error.client

import com.brsv44n.some_courier.core.network.error.ApiError

class InvalidValue(val fields: List<String>) : ApiError()
