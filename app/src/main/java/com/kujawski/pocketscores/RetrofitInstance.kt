package com.kujawski.pocketscores

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://site.api.espn.com/apis/site/v2/sports/football/nfl/"

    val apiService: ESPNApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ESPNApiService::class.java)
    }
}
