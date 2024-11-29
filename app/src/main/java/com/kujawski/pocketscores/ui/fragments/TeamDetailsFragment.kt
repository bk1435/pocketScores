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

class TeamDetailsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var apiService: ESPNApiService
    private lateinit var teamLogoImageView: ImageView
    private lateinit var teamNameTextView: TextView


    private val teamMap: MutableMap<String, String?> = mutableMapOf()

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

                    gamesAdapter.submitList(games, favoriteTeamId = teamId, teamMap = teamMap)
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

        val inputStream = requireContext().assets.open("teams.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val teamsJson = JSONObject(jsonString)


        val teamsArray = teamsJson.getJSONArray("sports")
            .getJSONObject(0)
            .getJSONArray("leagues")
            .getJSONObject(0)
            .getJSONArray("teams")


        for (i in 0 until teamsArray.length()) {
            val team = teamsArray.getJSONObject(i).getJSONObject("team")
            val teamIdFromJson = team.getString("id")
            val logoUrl = team.getJSONArray("logos").getJSONObject(0).getString("href")


            teamMap[teamIdFromJson] = logoUrl


            if (teamIdFromJson == teamId) {
                teamNameTextView.text = team.getString("displayName")
                Glide.with(this)
                    .load(logoUrl)
                    .into(teamLogoImageView)
            }
        }


        teamMap.forEach { (id, logo) ->
            Log.d("TeamDetailsFragment", "Team ID: $id, Logo URL: $logo")
        }
    }
}
