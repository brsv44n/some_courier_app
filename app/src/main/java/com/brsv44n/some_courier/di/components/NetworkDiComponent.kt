package com.brsv44n.some_courier.di.components

import android.app.Application
import android.content.Context
import com.brsv44n.some_courier.BuildConfig
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.data.ApiService
import com.brsv44n.some_courier.data.SseClient
import com.brsv44n.some_courier.data.interceptors.AuthInterceptor
import com.brsv44n.some_courier.data.interceptors.ErrorInterceptor
import com.brsv44n.some_courier.data.interceptors.NetworkStateInterceptor
import com.brsv44n.some_courier.data.interceptors.UserAgentInterceptor
import com.brsv44n.some_courier.utils.AndroidAuthHolder
import com.brsv44n.some_courier.utils.AndroidCoroutineDispatchers
import com.brsv44n.some_courier.utils.AndroidErrorHandler
import com.brsv44n.some_courier.utils.AndroidNetworkStateChecker
import com.brsv44n.some_courier.core.android_utils.AndroidBuildInfo
import com.brsv44n.some_courier.core.di.annotations.Singleton
import com.brsv44n.some_courier.core.network.NetworkStateChecker
import com.brsv44n.some_courier.core.presentation.ErrorHandler
import com.brsv44n.some_courier.core.utils.AuthHolder
import com.brsv44n.some_courier.core.utils.BuildInfo
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Singleton
@Component
abstract class NetworkDiComponent(
    @get:Provides open val application: Application,
    @get:Provides open val context: Context,
) {
    val buildInfo by lazy {
        AndroidBuildInfo(
            appName = application.getString(R.string.app_name),
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE,
            appIcon = R.mipmap.ic_launcher,
            applicationId = BuildConfig.APPLICATION_ID,
        )
    }

    abstract val dispatchers: CoroutineDispatchers

    abstract val authInterceptor: AuthInterceptor

    abstract val networkStateInterceptor: NetworkStateInterceptor

    abstract val userAgentInterceptor: UserAgentInterceptor

    @Singleton
    abstract val androidAuthHolder: AndroidAuthHolder

    @Singleton
    @Provides
    fun provideBuildInfo(): BuildInfo = buildInfo

    @Singleton
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers = AndroidCoroutineDispatchers()

    @Singleton
    @Provides
    fun provideAuthHolder(): AuthHolder = androidAuthHolder

    @Singleton
    @Provides
    fun AndroidNetworkStateChecker.bind(): NetworkStateChecker = this

    @Provides
    fun AndroidErrorHandler.bind(): ErrorHandler = this

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        networkStateInterceptor: NetworkStateInterceptor,
        userAgentInterceptor: UserAgentInterceptor,
        errorInterceptor: ErrorInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(authInterceptor)
            addInterceptor(networkStateInterceptor)
            addInterceptor(userAgentInterceptor)
            addInterceptor(errorInterceptor)
            if (BuildConfig.DEBUG) addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }.build()
    }

    @Singleton
    @Provides
    @com.brsv44n.some_courier.di.SseClient
    fun provideSseOkHttpClient(
        authInterceptor: AuthInterceptor,
        networkStateInterceptor: NetworkStateInterceptor,
        userAgentInterceptor: UserAgentInterceptor,
        errorInterceptor: ErrorInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(authInterceptor)
            addInterceptor(networkStateInterceptor)
            addInterceptor(userAgentInterceptor)
            addInterceptor(errorInterceptor)
            if (BuildConfig.DEBUG) addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.HEADERS
                }
            )
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(0, TimeUnit.SECONDS) // Для SSE нужен бесконечный read timeout
            writeTimeout(30, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Singleton
    @Provides
    fun providesApiService(
        retrofit: Retrofit,
    ): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideSseClient(@com.brsv44n.some_courier.di.SseClient okHttpClient: OkHttpClient): SseClient {
        return SseClient(
            url = BuildConfig.BASE_URL + "events",
            dispatchers = dispatchers,
            okHttpClient = okHttpClient
        )
    }

}
