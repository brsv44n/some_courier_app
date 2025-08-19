package com.brsv44n.some_courier.core.network.error.server

class BadGatewayException(response: String? = null) : ServerException(response, 502)
