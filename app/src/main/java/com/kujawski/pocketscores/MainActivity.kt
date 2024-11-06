package com.kujawski.pocketscores

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kujawski.pocketscores.databinding.ActivityMainBinding
import com.kujawski.pocketscores.models.ESPNModels
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var teamAdapter: TeamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupRecyclerView()
        loadTeams()
    }

    private fun setupRecyclerView() {
        teamAdapter = TeamAdapter { team -> saveFavoriteTeam(team) }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = teamAdapter
        }
    }

    private fun loadTeams() {
        RetrofitInstance.apiService.getTeams().enqueue(object : Callback<ESPNModels.ApiResponse> {
            override fun onResponse(
                call: Call<ESPNModels.ApiResponse>,
                response: Response<ESPNModels.ApiResponse>
            ) {
                if (response.isSuccessful) {
                    val teams = response.body()?.sports?.firstOrNull()?.leagues?.firstOrNull()?.teams?.map { it.team } ?: emptyList()

                    Log.d("MainActivity", "Teams loaded: $teams")

                    if (teams.isNotEmpty()) {
                        teamAdapter.setTeams(teams)
                    } else {
                        Log.e("MainActivity", "No teams found in response")
                    }
                } else {
                    Log.e("MainActivity", "Response unsuccessful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ESPNModels.ApiResponse>, t: Throwable) {
                Log.e("MainActivity", "Failed to load teams: ${t.message}")
            }
        })
    }

    private fun saveFavoriteTeam(team: ESPNModels.Team) {
        val intent = Intent(this, TeamDetailsActivity::class.java).apply {
            putExtra("team_name", team.name)
        }
        startActivity(intent)
    }
}
