package com.kujawski.pocketscores

import com.kujawski.pocketscores.models.SportsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ESPNApiService {

    @GET("sports/football/nfl/scoreboard")
    fun getScoreboard(): Call<NFLScoreboardResponse>

    @GET("sports/football/nfl/teams")
    fun getTeams(): Call<SportsResponse>

    // Fetch games for a specific team by team ID
    @GET("sports/football/nfl/teams/{teamId}/schedule")
    fun getTeamGames(@Path("teamId") teamId: String): Call<TeamGamesResponse>


}