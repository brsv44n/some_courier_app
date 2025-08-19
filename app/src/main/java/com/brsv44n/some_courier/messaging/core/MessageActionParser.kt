package com.brsv44n.some_courier.messaging.core

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull
import me.tatarka.inject.annotations.Inject

@Inject
class MessageActionParser {

    @Suppress("SwallowedException", "ReturnCount")
    operator fun invoke(action: String): MessageAction? {
        val jsonElement = try {
            Json.decodeFromString(JsonElement.serializer(), action)
        } catch (e: Exception) {
            return null
        }

        if (jsonElement !is JsonObject) return null

        val type = when (val typeValue = jsonElement["type"]) {
            is JsonPrimitive -> when (typeValue.content) {
                "order" -> MessageAction.Type.ORDER
                else -> return null
            }
            else -> return null
        }

        val targetId = jsonElement["target_id"]?.let { (it as? JsonPrimitive)?.intOrNull }

        return MessageAction(type, targetId)
    }
}
