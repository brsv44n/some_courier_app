package com.brsv44n.some_courier.core.data.caching

class CacheLifetime(
    val millis: Long
) {

    companion object {
        val INFINITE = CacheLifetime(Long.MAX_VALUE)
        fun millis(millis: Long) = CacheLifetime(millis)
        fun seconds(seconds: Long) = CacheLifetime(seconds * 1000)
        fun minutes(minutes: Long) = CacheLifetime(minutes * 60 * 1000)
        fun hours(hours: Long) = CacheLifetime(hours * 60 * 60 * 1000)
    }
}
