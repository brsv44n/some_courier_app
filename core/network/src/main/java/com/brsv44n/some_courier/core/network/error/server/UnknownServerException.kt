package com.brsv44n.some_courier.core.network.error.server

class UnknownServerException(response: String? = null, code: Int) : ServerException(response, code)
