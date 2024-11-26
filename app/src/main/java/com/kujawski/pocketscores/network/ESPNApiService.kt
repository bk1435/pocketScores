package com.kujawski.pocketscores.network

import com.kujawski.pocketscores.NFLScoreboardResponse
import com.kujawski.pocketscores.models.Game
import com.kujawski.pocketscores.models.SportsResponse
import com.kujawski.pocketscores.models.TeamGamesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ESPNApiService {

    @GET("sports/football/nfl/scoreboard")
    fun getScoreboard(): Call<NFLScoreboardResponse>

    @GET("sports/football/nfl/teams")
    fun getTeams(): Call<SportsResponse>

    // Fetch games for a specific team by team ID
    @GET("sports/football/nfl/teams/{teamId}/schedule")
    fun getTeamGames(@Path("teamId") teamId: String): Call<TeamGamesResponse>

    @GET("sports/football/nfl/scoreboard")
    fun getAllGames(
        @Query("dates") dateRange: String = "20240905-20240204", // Full 2024 season
        @Query("limit") limit: Int = 500  // Fetch all games
    ): Call<List<Game>>



}