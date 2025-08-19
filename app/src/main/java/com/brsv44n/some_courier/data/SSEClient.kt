package com.brsv44n.some_courier.data

import com.brsv44n.some_courier.data.remote.dto.RouteDto
import com.brsv44n.some_courier.data.remote.dto.UpdateRouteStatusDto
import com.brsv44n.some_courier.core.utils.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonClassDiscriminator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("kind")
sealed class RouteData {
    @Serializable
    @SerialName("create-route")
    data class CreateRouteDto(
        @SerialName("route") val value: RouteDto,
    ) : RouteData()

    @Serializable
    @SerialName("change-route-status")
    data class ChangeRouteStatus(
        @SerialName("route") val value: UpdateRouteStatusDto,
    ) : RouteData()
}

/**
 * Событие от SSE сервера
 */
data class EventDto(
    val id: String?,
    val type: String?,
    val data: String,
    val timestamp: Long = System.currentTimeMillis(),
)

/**
 * SSE клиент с автоматическим переподключением
 */
class SseClient(
    private val url: String,
    private val okHttpClient: OkHttpClient = defaultOkHttpClient(),
    private val dispatchers: CoroutineDispatchers,
    private val reconnectDelay: Duration = 5.seconds,
    private val maxReconnectAttempts: Int = 30,
) {
    companion object {
        private fun defaultOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS) // Для SSE нужен бесконечный read timeout
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }
    }

    @Suppress
    private val scope = CoroutineScope(SupervisorJob() + dispatchers.io)
    private val _events = MutableSharedFlow<EventDto>(replay = 0, extraBufferCapacity = 64)
    private var eventSource: EventSource? = null
    private var isConnectedOrConnecting = false
    private var reconnectAttempts = 0

    /**
     * Flow событий от сервера
     */
    @Suppress("SwallowedException")
    val events: Flow<RouteData> = _events.asSharedFlow().map {
        Timber.tag("SSEClient").d("Event received: $it")
        try {
            Json.decodeFromString<RouteData>(it.data)
        } catch (e: Throwable) {
            null
        }
    }.filterNotNull()

    /**
     * Подключение к SSE серверу
     */
    fun connect() {
        Timber.tag("SSEClient").d("Connect called")
        if (isConnectedOrConnecting) {
            return
        }

        scope.launch {
            connectInternal()
            Timber.tag("SSEClient").d("Connected")
        }
    }

    /**
     * Отключение от SSE сервера
     */
    fun disconnect() {
        Timber.tag("SSEClient").d("Disconnect called")
        isConnectedOrConnecting = false
        eventSource?.cancel()
        eventSource = null
        reconnectAttempts = 0
    }

    /**
     * Очистка ресурсов
     */
    fun close() {
        disconnect()
        scope.cancel()
    }

    private suspend fun connectInternal() {
        isConnectedOrConnecting = true
        if (reconnectAttempts >= maxReconnectAttempts) {
            Timber.tag("SSEClient")
                .d("Превышено максимальное количество попыток переподключения: $maxReconnectAttempts")
            return
        }

        try {
            val request = Request.Builder()
                .url(url)
                .header("Accept", "text/event-stream")
                .header("Cache-Control", "no-cache")
                .build()

            val listener = object : EventSourceListener() {
                override fun onOpen(eventSource: EventSource, response: Response) {
                    Timber.tag("SSEClient").d("SSE подключение установлено")
                    isConnectedOrConnecting = true
                    reconnectAttempts = 0
                }

                override fun onEvent(
                    eventSource: EventSource,
                    id: String?,
                    type: String?,
                    data: String
                ) {
                    val eventDto = EventDto(id, type, data)
                    scope.launch {
                        _events.emit(eventDto)
                    }
                }

                override fun onClosed(eventSource: EventSource) {
                    Timber.tag("SSEClient").d("SSE подключение закрыто")
                    isConnectedOrConnecting = false
                }

                override fun onFailure(
                    eventSource: EventSource,
                    t: Throwable?,
                    response: Response?
                ) {
                    Timber.tag("SSEClient")
                        .d("SSE ошибка: ${t?.message}, response: ${response?.code}")
                    isConnectedOrConnecting = false

                    // Автоматическое переподключение при ошибке
                    if (reconnectAttempts < maxReconnectAttempts) {
                        scope.launch {
                            delay(reconnectDelay)
                            reconnectAttempts++
                            Timber.tag("SSEClient").d("Попытка переподключения #$reconnectAttempts")
                            connectInternal()
                        }
                    }
                }
            }

            eventSource = EventSources.createFactory(okHttpClient)
                .newEventSource(request, listener)

        } catch (e: Exception) {
            Timber.tag("SSEClient").d("Ошибка при создании SSE подключения: ${e.message}")
            isConnectedOrConnecting = false

            if (reconnectAttempts < maxReconnectAttempts) {
                delay(reconnectDelay)
                reconnectAttempts++
                Timber.tag("SSEClient").d("Попытка переподключения #$reconnectAttempts")
                connectInternal()
            }
        }
    }
}
