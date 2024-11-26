package com.kujawski.pocketscores.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.adapters.TeamAdapter
import com.kujawski.pocketscores.models.LeagueTeam
import com.kujawski.pocketscores.models.SportsResponse
import com.kujawski.pocketscores.network.ESPNApiService
import com.kujawski.pocketscores.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamSelectionFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var teamAdapter: TeamAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var apiService: ESPNApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_team_selection, container, false)
        recyclerView = view.findViewById(R.id.team_selection_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        teamAdapter = TeamAdapter { team -> onTeamSelected(team) }
        recyclerView.adapter = teamAdapter

        sharedPreferences = requireActivity().getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE)
        apiService = RetrofitInstance.api

        fetchTeams()

        return view
    }

    private fun fetchTeams() {
        apiService.getTeams().enqueue(object : Callback<SportsResponse> {
            override fun onResponse(call: Call<SportsResponse>, response: Response<SportsResponse>) {
                response.body()?.sports?.firstOrNull()?.leagues?.firstOrNull()?.teams?.let { teams ->
                    teamAdapter.submitList(teams.map { it.team })
                }
            }

            override fun onFailure(call: Call<SportsResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun onTeamSelected(team: LeagueTeam) {
        sharedPreferences.edit().putString("FAVORITE_TEAM_ID", team.id).apply()
        val action = TeamSelectionFragmentDirections.actionTeamSelectionFragmentToTeamDetailsFragment(team.id)
        findNavController().navigate(action)
    }
}
