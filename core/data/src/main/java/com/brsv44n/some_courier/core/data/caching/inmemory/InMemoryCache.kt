package com.brsv44n.some_courier.core.data.caching.inmemory

import com.brsv44n.some_courier.core.data.caching.Cache
import com.brsv44n.some_courier.core.data.caching.CachedResource
import java.util.concurrent.ConcurrentHashMap

class InMemoryCache<KEY : Any, VALUE : Any> : Cache<KEY, VALUE> {

    private val cache = ConcurrentHashMap<KEY, CachedResource<VALUE>>()

    override suspend fun get(key: KEY): CachedResource<VALUE>? {
        return cache[key]
    }

    override suspend fun set(key: KEY, value: CachedResource<VALUE>) {
        cache[key] = value
    }

    override suspend fun remove(key: KEY) {
        cache.remove(key)
    }

    override suspend fun clear() {
        cache.clear()
    }
}
