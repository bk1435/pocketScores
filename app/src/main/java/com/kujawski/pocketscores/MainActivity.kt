package com.kujawski.pocketscores

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.models.LeagueTeam
import com.kujawski.pocketscores.models.SportsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var teamAdapter: TeamAdapter
    private lateinit var apiService: ESPNApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        teamAdapter = TeamAdapter { team -> onTeamSelected(team) }
        recyclerView.adapter = teamAdapter


        apiService = RetrofitInstance.api


        fetchTeams()
    }

    private fun fetchTeams() {
        Log.d("MainActivity", "Starting fetchTeams()")

        apiService.getTeams().enqueue(object : Callback<SportsResponse> {
            override fun onResponse(call: Call<SportsResponse>, response: Response<SportsResponse>) {
                Log.d("MainActivity", "API response received")

                if (response.isSuccessful) {
                    Log.d("MainActivity", "Response is successful")

                    val leagueTeams = response.body()?.sports
                        ?.firstOrNull()
                        ?.leagues
                        ?.firstOrNull()
                        ?.teams
                        ?.map { it.team }

                    if (leagueTeams != null && leagueTeams.isNotEmpty()) {
                        Log.d("MainActivity", "Teams loaded: ${leagueTeams.size}")
                        teamAdapter.submitList(leagueTeams)
                    } else {
                        Log.d("MainActivity", "No teams found in response")
                        Toast.makeText(this@MainActivity, "No teams found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Response error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@MainActivity, "Failed to retrieve teams: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SportsResponse>, t: Throwable) {
                Log.e("MainActivity", "Network error: ${t.message}")
                Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    private fun onTeamSelected(team: LeagueTeam) {

        val intent = Intent(this, TeamDetailsActivity::class.java)
        intent.putExtra("TEAM_ID", team.id)
        startActivity(intent)
    }
}