package com.kujawski.pocketscores.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.kujawski.pocketscores.models.Score
import java.lang.reflect.Type

class ScoreDeserializer : JsonDeserializer<Score> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Score {
        return try {

            if (json.isJsonObject) {
                val jsonObject = json.asJsonObject
                val scoreValue = jsonObject["value"]?.asInt ?: jsonObject["score"]?.asInt
                Score(value = scoreValue)
            }

            else if (json.isJsonPrimitive) {
                val value = if (json.asJsonPrimitive.isNumber) {
                    json.asInt
                } else if (json.asJsonPrimitive.isString) {
                    json.asString.toIntOrNull()
                } else null
                Score(value = value)
            }

            else {
                Score(value = null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Score(value = null)
        }
    }
}
