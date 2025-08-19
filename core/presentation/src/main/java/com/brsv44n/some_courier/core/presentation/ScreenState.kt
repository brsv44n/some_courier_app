package com.brsv44n.some_courier.core.presentation

sealed class ScreenState<out T> {

    data object Empty : ScreenState<Nothing>()

    data object Loading : ScreenState<Nothing>()

    data class Error(val message: String, val canBeRepeated: Boolean = true) :
        ScreenState<Nothing>()

    data class Success<T>(val data: T) : ScreenState<T>()

    fun <R> map(mapper: (T) -> R): ScreenState<R> = when (this) {
        Empty -> Empty
        is Error -> Error(message, canBeRepeated)
        Loading -> Loading
        is Success -> Success(mapper(data))
    }

}
