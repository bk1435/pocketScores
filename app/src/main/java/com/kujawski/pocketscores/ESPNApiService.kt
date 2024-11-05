package com.kujawski.pocketscores

import com.kujawski.pocketscores.models.Event
import com.kujawski.pocketscores.models.NFLScoreboardResponse
import com.kujawski.pocketscores.models.NFLTeam
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ESPNApiService {
    @GET("teams")
    fun getNFLTeams(): Call<List<NFLTeam>>

    @GET("teams/{teamId}/games")
    fun getTeamGames(@Path("teamId") teamId: String): Call<List<Event>>

    @GET("scoreboard")
    fun getNFLScoreboard(): Call<NFLScoreboardResponse>
}
