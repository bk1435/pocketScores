package com.kujawski.pocketscores.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.adapters.WeeksAdapter
import com.kujawski.pocketscores.models.Week

class AroundLeagueFragment : Fragment() {

    private val allWeeks: List<Week> by lazy { generateWeeks() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_around_league, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewWeeks)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = WeeksAdapter(allWeeks)

        recyclerView.post {
            recyclerView.scrollToPosition(allWeeks.size - 1)
        }

        return view
    }

    private fun generateWeeks(): List<Week> {
        val weeks = mutableListOf<Week>()
        for (i in 1..18) {
            weeks.add(Week(weekNumber = i))
        }
        weeks.addAll(
            listOf(
                Week(weekNumber = -4, label = "Wild Card Round"),
                Week(weekNumber = -3, label = "Divisional Round"),
                Week(weekNumber = -2, label = "Conference Round"),
                Week(weekNumber = -1, label = "Super Bowl")
            )
        )
        return weeks
    }

}
