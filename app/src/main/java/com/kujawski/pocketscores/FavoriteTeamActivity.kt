package com.kujawski.pocketscores

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.models.NFLTeam
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavoriteTeamActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_team)

        sharedPreferences = getSharedPreferences("com.kujawski.pocketscores", MODE_PRIVATE)

        recyclerView = findViewById(R.id.teamRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchTeams()
    }

    private fun fetchTeams() {
        val apiService = RetrofitInstance.api
        val call = apiService.getNFLTeams()

        call.enqueue(object : Callback<List<NFLTeam>> {
            override fun onResponse(call: Call<List<NFLTeam>>, response: Response<List<NFLTeam>>) {
                if (response.isSuccessful) {
                    val teams = response.body() ?: emptyList()
                    recyclerView.adapter = TeamAdapter(teams) { team ->
                        // Save selected team to SharedPreferences
                        sharedPreferences.edit().putString("FAVORITE_TEAM_ID", team.id).apply()
                    }
                }
            }

            override fun onFailure(call: Call<List<NFLTeam>>, t: Throwable) {
                // Handle failure
            }
        })
    }
}
