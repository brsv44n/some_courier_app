package com.brsv44n.some_courier.core.data.caching

import com.brsv44n.some_courier.core.data.caching.inmemory.InMemoryCache
import com.brsv44n.some_courier.core.domain.entities.Data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import java.util.concurrent.ConcurrentHashMap

class SharedCachingSource<PARAM : Any, DATA : Any>(
    cache: Cache<PARAM, DATA> = InMemoryCache(),
    source: suspend (PARAM) -> DATA
) : CachingSource<PARAM, DATA>(cache, source) {

    private val flows = ConcurrentHashMap<PARAM, MutableStateFlow<Data<DATA>>>()

    override fun getAsFlow(
        param: PARAM,
        cacheStrategy: CacheStrategy,
        cacheLifetime: CacheLifetime
    ): Flow<Data<DATA>> {
        val sharedFlow = flows.getOrPut(param) { MutableStateFlow(Data()) }
        val resultFlow = super.getAsFlow(param, cacheStrategy, cacheLifetime)

        return flow {
            emitAll(resultFlow)
            emitAll(sharedFlow)
        }.distinctUntilChanged()
    }

    override suspend fun put(param: PARAM, data: DATA) {
        super.put(param, data)
        flows[param]?.emit(Data(value = data))
    }

    override suspend fun clear() {
        super.clear()
        flows.values.forEach { it.emit(Data()) }
    }

    override suspend fun getAndSaveToCache(param: PARAM): DATA {
        val currentValue = flows[param]?.firstOrNull()
        flows[param]?.emit(Data(isLoading = true, value = currentValue?.value))
        return runCatching {
            super.getAndSaveToCache(param).also {
                flows[param]?.emit(Data(value = it))
            }
        }.onFailure {
            flows[param]?.emit(Data(error = it, value = currentValue?.value))
        }.getOrThrow()
    }

}
