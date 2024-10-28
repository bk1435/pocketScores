package com.kujawski.pocketscores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.models.NFLGame

class TeamGamesAdapter(private val games: List<NFLGame>) : RecyclerView.Adapter<TeamGamesAdapter.TeamGameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return TeamGameViewHolder(view)
    }

    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(holder: TeamGameViewHolder, position: Int) {
        holder.bind(games[position])
    }

    inner class TeamGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gameDateTextView: TextView = itemView.findViewById(R.id.gameDateTextView)
        private val teamsTextView: TextView = itemView.findViewById(R.id.teamsTextView)
        private val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)

        fun bind(game: NFLGame) {
            // Safely get the first competition, or null if it's not available
            val competition = game.competitions?.getOrNull(0)

            // Safely access home and away team names, defaulting to "N/A" if null
            val homeTeamName = competition?.competitors?.getOrNull(0)?.team?.displayName ?: "N/A"
            val awayTeamName = competition?.competitors?.getOrNull(1)?.team?.displayName ?: "N/A"

            // Set the text for the teams and scores, using default if data is missing
            teamsTextView.text = itemView.context.getString(R.string.teams_vs, homeTeamName, awayTeamName)
            scoreTextView.text = itemView.context.getString(R.string.score_format, "N/A")  // Update if actual score data is available

            // Set a placeholder date or actual date if available
            gameDateTextView.text = "N/A"  // Replace with actual game date if provided by the API
        }
    }
}
