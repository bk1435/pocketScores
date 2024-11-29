package com.kujawski.pocketscores.network

import com.google.gson.GsonBuilder
import com.kujawski.pocketscores.models.Game
import com.kujawski.pocketscores.models.Score
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://site.api.espn.com/apis/site/v2/"


    private val gson = GsonBuilder()
        .registerTypeAdapter(Score::class.java, ScoreDeserializer())
        .registerTypeAdapter(Game::class.java, GameDeserializer())
        .create()

    val api: ESPNApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ESPNApiService::class.java)
    }
}