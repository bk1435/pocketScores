package com.kujawski.pocketscores.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.adapters.GamesAdapter
import com.kujawski.pocketscores.models.TeamGamesResponse
import com.kujawski.pocketscores.network.ESPNApiService
import com.kujawski.pocketscores.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamDetailsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var apiService: ESPNApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_team_details, container, false)
        recyclerView = view.findViewById(R.id.team_details_recycler_view)
        gamesAdapter = GamesAdapter()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = gamesAdapter

        apiService = RetrofitInstance.api
        val teamId = arguments?.getString("teamId") ?: return view

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
}
