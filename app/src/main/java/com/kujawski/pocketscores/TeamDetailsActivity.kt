package com.kujawski.pocketscores

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamDetailsActivity : AppCompatActivity() {

    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var teamId: String
    private lateinit var teamName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_details)

        // Retrieve the team ID and name passed from MainActivity
        teamId = intent.getStringExtra("team_id") ?: ""
        teamName = intent.getStringExtra("team_name") ?: ""

        findViewById<TextView>(R.id.teamNameTextView).text = teamName

        loadRecentGames()
    }

    private fun loadRecentGames() {
        val apiService = RetrofitInstance.apiService

        apiService.getTeamGames(teamId).enqueue(object : Callback<TeamGamesResponse> {
            override fun onResponse(
                call: Call<TeamGamesResponse>,
                response: Response<TeamGamesResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val gamesList = response.body()?.events ?: emptyList()
                    gamesAdapter = GamesAdapter(gamesList)
                    findViewById<RecyclerView>(R.id.gamesRecyclerView).apply {
                        layoutManager = LinearLayoutManager(this@TeamDetailsActivity)
                        adapter = gamesAdapter
                    }
                }
            }

            override fun onFailure(call: Call<TeamGamesResponse>, t: Throwable) {
                // Handle the failure (log or show error message)
            }
        })
    }
}
