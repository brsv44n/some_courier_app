package com.brsv44n.some_courier.core.data.caching

interface Cache<KEY : Any, VALUE : Any> {

    suspend fun get(key: KEY): CachedResource<VALUE>?

    suspend fun set(key: KEY, value: CachedResource<VALUE>)

    suspend fun remove(key: KEY)

    suspend fun clear()
}
