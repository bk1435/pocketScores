package com.kujawski.pocketscores

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.models.NFLGame
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteTeamPageActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_team_page)

        sharedPreferences = getSharedPreferences("com.kujawski.pocketscores", MODE_PRIVATE)
        recyclerView = findViewById(R.id.favoriteTeamGamesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadFavoriteTeamData()
    }

    private fun loadFavoriteTeamData() {
        val teamId = sharedPreferences.getString("FAVORITE_TEAM_ID", null) ?: return
        val apiService = RetrofitInstance.api
        val call = apiService.getTeamGames(teamId)

        call.enqueue(object : Callback<List<NFLGame>> {
            override fun onResponse(call: Call<List<NFLGame>>, response: Response<List<NFLGame>>) {
                if (response.isSuccessful) {
                    val games = response.body() ?: emptyList()
                    Log.d("API Response", "Parsed games: $games")
                    recyclerView.adapter = TeamGamesAdapter(games)
                } else {
                    Log.e("API Error", "Error response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<NFLGame>>, t: Throwable) {
                Log.e("API Error", "Network error: ${t.localizedMessage}")
            }
        })
    }
}
