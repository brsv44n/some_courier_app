package com.brsv44n.some_courier.core.presentation

import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun <T : Any> Store<*, T, *>.asValue(): Value<T> = object : Value<T>() {
    override val value: T get() = state

    override fun subscribe(observer: (T) -> Unit): Cancellation {

        val disposable = states(com.arkivanov.mvikotlin.rx.observer(onNext = observer))

        return Cancellation {
            disposable.dispose()
        }
    }
}

fun <T : Any> StateFlow<T>.asValue(scope: CoroutineScope) = object : Value<T>() {
    override val value: T get() = this@asValue.value

    override fun subscribe(observer: (T) -> Unit): Cancellation {
        val job = scope.launch { collect { observer(it) } }
        return Cancellation {
            job.cancel()
        }
    }
}

fun <T : Any> Value<T>.asFlow(): StateFlow<T> = ValueStateFlow(this)

private class ValueStateFlow<out T : Any>(
    private val v: Value<T>,
) : StateFlow<T> {
    override val value: T get() = v.value
    override val replayCache: List<T> get() = listOf(v.value)

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        val flow = MutableStateFlow(v.value)
        val observer: (T) -> Unit = { flow.value = it }
        val cancellation = v.subscribe(observer)

        try {
            flow.collect(collector)
        } finally {
            cancellation.cancel()
        }
    }
}
