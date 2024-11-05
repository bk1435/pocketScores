package com.kujawski.pocketscores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.models.Event

class TeamGamesAdapter(private val events: List<Event>) : RecyclerView.Adapter<TeamGamesAdapter.TeamGameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return TeamGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamGameViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class TeamGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val teamNameTextView: TextView = itemView.findViewById(R.id.teamNameTextView)
        private val gameDateTextView: TextView = itemView.findViewById(R.id.gameDateTextView)

        fun bind(event: Event) {

            val competition = event.competitions?.firstOrNull()


            val homeTeamName = competition?.competitors?.getOrNull(0)?.team?.displayName ?: "N/A"
            val awayTeamName = competition?.competitors?.getOrNull(1)?.team?.displayName ?: "N/A"


            teamNameTextView.text = itemView.context.getString(R.string.teams_vs, homeTeamName, awayTeamName)


            val gameDate = event.date ?: "N/A"
            gameDateTextView.text = gameDate
        }
    }
}
