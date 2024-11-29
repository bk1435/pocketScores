package com.kujawski.pocketscores.network

import com.kujawski.pocketscores.NFLScoreboardResponse
import com.kujawski.pocketscores.models.SportsResponse
import com.kujawski.pocketscores.models.TeamGamesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ESPNApiService {

    @GET("sports/football/nfl/scoreboard")
    fun getScoreboard(): Call<NFLScoreboardResponse>

    @GET("sports/football/nfl/teams")
    fun getTeams(): Call<SportsResponse>


    @GET("sports/football/nfl/teams/{teamId}/schedule")
    fun getTeamGames(@Path("teamId") teamId: String): Call<TeamGamesResponse>

    @GET("sports/football/nfl/scoreboard")
    fun getGamesForWeek(
        @Query("seasontype") seasonType: Int,
        @Query("week") week: Int,
        @Header("User-Agent") userAgent: String = "Mozilla/5.0",
        @Header("Accept") accept: String = "application/json"
    ): Call<NFLScoreboardResponse>



}