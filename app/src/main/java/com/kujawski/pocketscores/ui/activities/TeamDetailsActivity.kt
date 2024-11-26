package com.kujawski.pocketscores.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.kujawski.pocketscores.adapters.GamesAdapter
import com.kujawski.pocketscores.network.ESPNApiService
import com.kujawski.pocketscores.network.RetrofitInstance
import com.kujawski.pocketscores.models.TeamGamesResponse
import com.kujawski.pocketscores.R

class TeamDetailsActivity : AppCompatActivity() {

    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var apiService: ESPNApiService
    private var teamId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_details)

        val gamesRecyclerView: RecyclerView = findViewById(R.id.gamesRecyclerView)
        gamesAdapter = GamesAdapter()


        gamesRecyclerView.layoutManager = LinearLayoutManager(this)
        gamesRecyclerView.adapter = gamesAdapter

        teamId = intent.getStringExtra("TEAM_ID")
        apiService = RetrofitInstance.api

        teamId?.let { fetchTeamGames(it) }
    }

    private fun fetchTeamGames(teamId: String) {
        Log.d("TeamDetailsActivity", "Fetching games for team ID: $teamId")

        apiService.getTeamGames(teamId).enqueue(object : Callback<TeamGamesResponse> {
            override fun onResponse(call: Call<TeamGamesResponse>, response: Response<TeamGamesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { teamGamesResponse ->
                        val games = teamGamesResponse.events
                        Log.d("API_DEBUG", "Games retrieved: ${games.size}")
                        gamesAdapter.submitList(games)
                    } ?: run {
                        Log.e("TeamDetailsActivity", "Response body is null")
                        Toast.makeText(this@TeamDetailsActivity, "No games data available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("TeamDetailsActivity", "API error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@TeamDetailsActivity, "Failed to load games", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TeamGamesResponse>, t: Throwable) {
                Log.e("TeamDetailsActivity", "Network error: ${t.localizedMessage}")
                Toast.makeText(this@TeamDetailsActivity, "Failed to load games", Toast.LENGTH_SHORT).show()
            }
        })
    }
}