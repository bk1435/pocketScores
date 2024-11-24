package com.kujawski.pocketscores

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.kujawski.pocketscores.models.Score
import java.lang.reflect.Type

class ScoreDeserializer : JsonDeserializer<Score> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Score {
        val jsonObject = json.asJsonObject


        println("Deserializing Score JSON: $jsonObject")


        val scoreValue = jsonObject["value"]?.asInt ?: jsonObject["score"]?.asInt
        return Score(value = scoreValue)
    }
}

