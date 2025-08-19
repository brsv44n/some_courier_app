package com.brsv44n.some_courier.core.data.caching

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.ConcurrentHashMap

class SharedRequest<PARAM : Any, RESULT : Any>(
    private val request: suspend (PARAM) -> RESULT
) {

    private val requests = ConcurrentHashMap<PARAM, Deferred<RESULT>>()

    @Suppress("DeferredResultUnused")
    suspend fun get(param: PARAM): RESULT = coroutineScope {
        val deferred = requests.getOrPut(param) {
            async { request(param) }
        }
        try {
            return@coroutineScope deferred.await()
        } finally {
            requests.remove(param)
        }
    }
}
