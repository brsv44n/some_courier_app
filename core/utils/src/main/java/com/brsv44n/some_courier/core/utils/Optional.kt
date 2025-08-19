package com.brsv44n.some_courier.core.utils

class Optional<T>(private val value: T? = null) {

    companion object {
        fun <T> empty() = Optional<T>()

        @Suppress("FunctionMinLength")
        fun <T> of(value: T) = Optional(value)
    }

    val isPresent: Boolean = value != null

    override fun equals(other: Any?): Boolean {
        if (other !is Optional<*>) return false
        return other.orNull() == this.orNull()
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    fun get() = requireNotNull(value)

    fun orElseGet(provider: () -> T?): T? = value ?: provider.invoke()

    fun orNull(): T? = value

    suspend fun ifPresent(consumer: suspend (T) -> Unit) {
        if (isPresent) consumer.invoke(get())
    }

}

fun <T> T?.asOptional() = if (this == null) Optional.empty() else Optional.of<T>(this)
