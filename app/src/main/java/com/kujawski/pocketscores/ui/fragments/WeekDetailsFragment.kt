package com.kujawski.pocketscores.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.adapters.GamesAdapter
import com.kujawski.pocketscores.viewmodels.WeekDetailsViewModel
import org.json.JSONObject

class WeekDetailsFragment : Fragment() {

    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var viewModel: WeekDetailsViewModel

    private val teamMap: MutableMap<String, String?> = mutableMapOf()

    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 30000


    private val updateRunnable = object : Runnable {
        override fun run() {
            Log.d("Polling", "Polling for game updates...")
            val weekNumber = arguments?.getInt("weekNumber") ?: -1
            if (weekNumber != -1) {
                Log.d("Polling", "Fetching games for week $weekNumber")
                viewModel.loadGamesForWeek(weekNumber)
            } else {
                Log.d("Polling", "Invalid week number")
            }
            handler.postDelayed(this, updateInterval)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_week_details, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewGames)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        gamesAdapter = GamesAdapter()
        recyclerView.adapter = gamesAdapter

        viewModel = ViewModelProvider(this)[WeekDetailsViewModel::class.java]

        val weekNumber = arguments?.getInt("weekNumber") ?: -1
        if (weekNumber == -1) {
            Toast.makeText(context, "Invalid week selected", Toast.LENGTH_SHORT).show()
            return view
        }

        viewModel.games.observe(viewLifecycleOwner) { games ->
            if (games.isNullOrEmpty()) {
                Toast.makeText(context, "No games found for this week", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("Games", "Games fetched successfully, submitting to adapter")
                populateTeamMap()
                gamesAdapter.submitList(games, favoriteTeamId = "123", teamMap = teamMap)
            }
        }

        viewModel.loadGamesForWeek(weekNumber)


        handler.post(updateRunnable)

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateRunnable)
        Log.d("Polling", "Polling stopped")
    }

    private fun populateTeamMap() {
        Log.d("WeekDetailsFragment", "Populating team map from assets")

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
            val teamId = team.getString("id")
            val logoUrl = team.getJSONArray("logos").getJSONObject(0).getString("href")
            teamMap[teamId] = logoUrl
        }

        teamMap.forEach { (id, logo) ->
            Log.d("WeekDetailsFragment", "Team ID: $id, Logo URL: $logo")
        }
    }
}
