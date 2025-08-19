package com.brsv44n.some_courier.core.network.error.server

class ServiceUnavailableException(response: String? = null) : ServerException(response, 503)
