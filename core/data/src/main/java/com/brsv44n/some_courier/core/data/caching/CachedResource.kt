package com.brsv44n.some_courier.core.data.caching

data class CachedResource<T : Any>(
    val value: T,
    val timestamp: Long
) {

    fun getIfNotExpired(cacheLifetime: CacheLifetime): T? {
        return if (System.currentTimeMillis() - timestamp < cacheLifetime.millis) value else null
    }
}
