package com.kujawski.pocketscores

import com.kujawski.pocketscores.models.ESPNModels
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ESPNApiService {
    @GET("scoreboard")
    fun getScoreboard(): Call<NFLScoreboardResponse>

    @GET("teams")
    fun getTeams(): Call<ESPNModels.ApiResponse>

    @GET("teams/{team_id}/schedule")
    fun getTeamGames(@Path("team_id") teamId: String): Call<TeamGamesResponse>
}
