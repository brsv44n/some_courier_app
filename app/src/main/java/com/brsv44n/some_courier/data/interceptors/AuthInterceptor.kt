package com.brsv44n.some_courier.data.interceptors

import com.brsv44n.some_courier.core.di.annotations.Singleton
import com.brsv44n.some_courier.core.utils.AuthHolder
import me.tatarka.inject.annotations.Inject
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
@Inject
class AuthInterceptor(
    private val authHolder: AuthHolder,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain
                .request()
                .newBuilder()
                .apply {
                    authHolder.getTokens()?.accessToken?.let {
                        addHeader(
                            "Authorization",
                            "Bearer $it"
                        )
                    }
                }
                .build()
        )
}
