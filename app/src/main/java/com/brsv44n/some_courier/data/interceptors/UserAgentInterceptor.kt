package com.brsv44n.some_courier.data.interceptors

import com.brsv44n.some_courier.core.di.annotations.Singleton
import com.brsv44n.some_courier.core.utils.BuildInfo
import me.tatarka.inject.annotations.Inject
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
@Inject
class UserAgentInterceptor(
    private val buildInfo: BuildInfo
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain
            .request()
            .newBuilder()
            .apply {
                addHeader("Platform", "Android")
                addHeader("Platform-Version", buildInfo.versionCode.toString())
            }
            .build()
        )

}
