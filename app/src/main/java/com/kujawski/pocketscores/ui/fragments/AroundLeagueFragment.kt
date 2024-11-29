package com.kujawski.pocketscores.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.adapters.WeeksAdapter
import com.kujawski.pocketscores.models.Week

class AroundLeagueFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_around_league, container, false)
        recyclerView = view.findViewById(R.id.weeks_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)


        val regularSeasonWeeks = (1..18).map { weekNumber ->
            Week(weekNumber, emptyList())
        }


        val postseasonRounds = listOf(
            Week(-4, emptyList()), // Wild Card Round
            Week(-3, emptyList()), // Divisional Round
            Week(-2, emptyList()), // Conference Round
            Week(-1, emptyList())  // Super Bowl
        )


        val allWeeks = regularSeasonWeeks + postseasonRounds


        allWeeks.forEachIndexed { index, week ->
            val label = when (week.weekNumber) {
                -4 -> "Wild Card Round"
                -3 -> "Divisional Round"
                -2 -> "Conference Round"
                -1 -> "Super Bowl"
                else -> "Week ${week.weekNumber}"
            }
            Log.d("AroundLeagueFragment", "Week $index: $label")
        }

        recyclerView.adapter = WeeksAdapter(allWeeks) { week ->
            when (week.weekNumber) {
                -4 -> Toast.makeText(context, "Clicked Wild Card Round", Toast.LENGTH_SHORT).show()
                -3 -> Toast.makeText(context, "Clicked Divisional Round", Toast.LENGTH_SHORT).show()
                -2 -> Toast.makeText(context, "Clicked Conference Round", Toast.LENGTH_SHORT).show()
                -1 -> Toast.makeText(context, "Clicked Super Bowl", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(context, "Clicked Week ${week.weekNumber}", Toast.LENGTH_SHORT).show()
            }
        }


        recyclerView.post {
            recyclerView.scrollToPosition(allWeeks.size - 1)
        }

        return view
    }
}