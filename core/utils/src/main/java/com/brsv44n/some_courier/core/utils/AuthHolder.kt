package com.brsv44n.some_courier.core.utils

import kotlinx.coroutines.flow.Flow

interface AuthHolder {

    val tokenFlow: Flow<Optional<Tokens>>
    val isLoggedIn: Boolean

    fun setTokens(tokens: Tokens)

    fun getTokens(): Tokens?

    fun clear()

    data class Tokens(val refreshToken: String?, val accessToken: String)
}
