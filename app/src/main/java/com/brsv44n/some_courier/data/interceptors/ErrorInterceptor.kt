package com.brsv44n.some_courier.data.interceptors

import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.domain.session.UnauthorizedErrorEmitter
import com.brsv44n.some_courier.core.network.error.ApiError
import com.brsv44n.some_courier.core.utils.ResourceManager
import me.tatarka.inject.annotations.Inject
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset

@Inject
class ErrorInterceptor(
    private val unauthorizedErrorEmitter: UnauthorizedErrorEmitter,
    private val resourceManager: ResourceManager,
) : Interceptor {

    @Suppress("SwallowedException", "ReturnCount")
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.isSuccessful) return response
        if (response.code == 401) {
            unauthorizedErrorEmitter.emitUnauthorizedError()
            return response
        }

        val responseBody = response.body
        val source = responseBody?.source()
        source?.request(Long.MAX_VALUE)
        val buffer = source?.buffer
        val body = buffer?.clone()?.readString(Charset.defaultCharset()) ?: return response
        return try {
            val json = JSONObject(body)
            throw parseApiError(json, response.code)
        } catch (e: JSONException) {
            response
        }
    }

    @Suppress("SwallowedException")
    private fun parseApiError(json: JSONObject, errorCode: Int): ApiError {
        return try {
            getErrorMessage(json).takeIf { !it.isNullOrBlank() }?.let { ApiError(it, errorCode) }
                ?: ApiError(resourceManager.getString(R.string.unknown_error), errorCode)
        } catch (e: JSONException) {
            ApiError(resourceManager.getString(R.string.unknown_error), errorCode)
        }
    }

    @Suppress("SwallowedException")
    private fun getErrorMessage(json: JSONObject): String? {
        val message = try {
            json.getString("reason").takeIf { it.isNotBlank() }
        } catch (e: JSONException) {
            null
        }
        return message
    }
}
