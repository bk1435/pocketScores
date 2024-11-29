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
import com.kujawski.pocketscores.adapters.WeeksAdapter
import com.kujawski.pocketscores.models.Week

class AroundLeagueFragment : Fragment() {

    private val allWeeks: List<Week> by lazy { generateWeeks() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            weeks.add(Week(weekNumber = i, label = "Week $i"))
        }


        val playoffLabels = mapOf(
            -4 to "Wild Card Round",
            -3 to "Divisional Round",
            -2 to "Conference Championships",
            -1 to "Super Bowl"
        )

        weeks.addAll(
            playoffLabels.map { (weekNumber, label) ->
                Week(weekNumber = weekNumber, label = label)
            }
        )


        weeks.forEach { Log.d("AroundLeagueFragment", "Week ${it.weekNumber}: ${it.label}") }

        return weeks
    }

}
