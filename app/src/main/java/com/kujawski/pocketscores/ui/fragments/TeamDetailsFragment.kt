package com.kujawski.pocketscores.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.adapters.GamesAdapter
import com.kujawski.pocketscores.models.TeamGamesResponse
import com.kujawski.pocketscores.network.ESPNApiService
import com.kujawski.pocketscores.network.RetrofitInstance
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream

class TeamDetailsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var apiService: ESPNApiService
    private lateinit var teamLogoImageView: ImageView
    private lateinit var teamNameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_team_details, container, false)
        recyclerView = view.findViewById(R.id.team_details_recycler_view)
        teamLogoImageView = view.findViewById(R.id.teamLogoImageView)
        teamNameTextView = view.findViewById(R.id.teamNameTextView)

        gamesAdapter = GamesAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = gamesAdapter

        val bottomNavHeight = resources.getDimensionPixelSize(R.dimen.bottom_navigation_height)
        recyclerView.setPadding(0, 0, 0, bottomNavHeight)
        recyclerView.clipToPadding = false

        apiService = RetrofitInstance.api
        val teamId = arguments?.getString("teamId") ?: return view

        loadTeamDetails(teamId)

        apiService.getTeamGames(teamId).enqueue(object : Callback<TeamGamesResponse> {
            override fun onResponse(
                call: Call<TeamGamesResponse>,
                response: Response<TeamGamesResponse>
            ) {
                if (response.isSuccessful) {
                    val games = response.body()?.events ?: emptyList()
                    games.forEach { game ->
                        Log.d("TeamDetailsFragment", "Game Data: ${game.competitions.firstOrNull()?.competitors}")
                    }
                    gamesAdapter.submitList(games, teamId)
                } else {
                    Log.e("TeamDetailsFragment", "Failed to fetch games. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TeamGamesResponse>, t: Throwable) {
                Log.e("TeamDetailsFragment", "API Call Failed: ${t.message}")
            }
        })

        return view
    }

    private fun loadTeamDetails(teamId: String) {
        // Load teams.json from assets
        val inputStream: InputStream = requireContext().assets.open("teams.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        val teamsJson = JSONObject(json)

        // Find the team in the JSON
        val teams = teamsJson.getJSONArray("sports")
            .getJSONObject(0)
            .getJSONArray("leagues")
            .getJSONObject(0)
            .getJSONArray("teams")

        for (i in 0 until teams.length()) {
            val team = teams.getJSONObject(i).getJSONObject("team")
            if (team.getString("id") == teamId) {
                // Set team name
                teamNameTextView.text = team.getString("displayName")

                // Load team logo
                val logoUrl = team.getJSONArray("logos").getJSONObject(0).getString("href")
                Glide.with(this)
                    .load(logoUrl)
                    .into(teamLogoImageView)
                break
            }
        }
    }
}
