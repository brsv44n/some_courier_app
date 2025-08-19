package com.brsv44n.some_courier.core.network.error.server

class GatewayTimeoutException(response: String? = null) : ServerException(response, 504)
