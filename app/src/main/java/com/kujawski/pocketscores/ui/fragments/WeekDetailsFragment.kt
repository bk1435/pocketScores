package com.kujawski.pocketscores.ui.fragments

import android.os.Bundle
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

class WeekDetailsFragment : Fragment() {

    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var viewModel: WeekDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
                gamesAdapter.submitList(games, favoriteTeamId = "123")
            }
        }


        viewModel.loadGamesForWeek(weekNumber)

        return view
    }
}