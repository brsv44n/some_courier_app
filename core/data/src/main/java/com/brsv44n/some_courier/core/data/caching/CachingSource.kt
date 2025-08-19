package com.brsv44n.some_courier.core.data.caching

import com.brsv44n.some_courier.core.data.caching.inmemory.InMemoryCache
import com.brsv44n.some_courier.core.domain.entities.Data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

open class CachingSource<PARAM : Any, DATA : Any>(
    private val cache: Cache<PARAM, DATA> = InMemoryCache(),
    private val source: suspend (PARAM) -> DATA
) {

    private val sharedRequest = SharedRequest<PARAM, DATA> { param -> source(param) }

    suspend fun get(
        param: PARAM,
        cacheStrategy: CacheStrategy = CacheStrategy.CACHE_ELSE_REQUEST,
        cacheLifetime: CacheLifetime = CacheLifetime.INFINITE
    ): DATA = getAsFlow(param, cacheStrategy, cacheLifetime)
        .filter { !it.isLoading }
        .map { it.value ?: throw it.getError() }
        .first()

    open fun getAsFlow(
        param: PARAM,
        cacheStrategy: CacheStrategy = CacheStrategy.CACHE_ELSE_REQUEST,
        cacheLifetime: CacheLifetime = CacheLifetime.INFINITE
    ): Flow<Data<DATA>> = flow {
        when (cacheStrategy) {
            CacheStrategy.NO_CACHE -> {
                runCatching {
                    emit(Data(isLoading = true))
                    getAndSaveToCache(param)
                }.onFailure {
                    emit(Data(error = it))
                }.onSuccess {
                    emit(Data(value = it))
                }
            }

            CacheStrategy.CACHE_THEN_REQUEST -> {
                cache.get(param)?.getIfNotExpired(cacheLifetime)
                    ?.let { emit(Data(isLoading = true, value = it)) }
                    ?: run { emit(Data(isLoading = true)) }
                runCatching {
                    getAndSaveToCache(param)
                }.onFailure {
                    emit(Data(error = it))
                }.onSuccess {
                    emit(Data(value = it))
                }
            }

            CacheStrategy.CACHE_ELSE_REQUEST -> {
                cache.get(param)?.getIfNotExpired(cacheLifetime)?.let { emit(Data(value = it)) }
                    ?: run {
                        runCatching {
                            emit(Data(isLoading = true))
                            getAndSaveToCache(param)
                        }.onFailure {
                            emit(Data(error = it))
                        }.onSuccess {
                            emit(Data(value = it))
                        }
                    }
            }
        }
    }

    open suspend fun put(
        param: PARAM,
        data: DATA
    ) {
        cache.set(param, CachedResource(data, System.currentTimeMillis()))
    }

    open suspend fun update(
        param: PARAM,
        update: DATA.() -> DATA
    ) {
        cache.get(param)?.value?.let { cachedData ->
            val updatedData = cachedData.update()
            cache.set(param, CachedResource(updatedData, System.currentTimeMillis()))
        }
    }

    open suspend fun clear() {
        cache.clear()
    }

    protected open suspend fun getAndSaveToCache(param: PARAM): DATA {
        val data = sharedRequest.get(param)
        cache.set(param, CachedResource(data, System.currentTimeMillis()))
        return data
    }
}
