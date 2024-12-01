package com.kujawski.pocketscores.network

import com.google.gson.GsonBuilder
import com.kujawski.pocketscores.models.Game
import com.kujawski.pocketscores.models.Score
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://site.api.espn.com/apis/site/v2/"


    private val gson = GsonBuilder()
        .registerTypeAdapter(Score::class.java, ScoreDeserializer())
        .registerTypeAdapter(Game::class.java, GameDeserializer())
        .create()


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()


    val api: ESPNApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ESPNApiService::class.java)
    }
}
