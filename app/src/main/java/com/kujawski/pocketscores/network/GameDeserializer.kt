package com.kujawski.pocketscores.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.kujawski.pocketscores.models.Competition
import com.kujawski.pocketscores.models.Game
import java.lang.reflect.Type

class GameDeserializer : JsonDeserializer<Game> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Game {
        val jsonObject = json.asJsonObject


        val date = jsonObject.get("date").asString


        val competitionsArray = jsonObject.getAsJsonArray("competitions")
        val competitions = competitionsArray.map { competitionElement ->
            context.deserialize<Competition>(competitionElement, Competition::class.java)
        }

        return Game(
            date = date,
            competitions = competitions
        )
    }
}