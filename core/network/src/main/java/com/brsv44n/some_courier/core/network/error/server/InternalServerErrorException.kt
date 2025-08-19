package com.brsv44n.some_courier.core.network.error.server

class InternalServerErrorException(override val message: String) : ServerException(message, 500)
