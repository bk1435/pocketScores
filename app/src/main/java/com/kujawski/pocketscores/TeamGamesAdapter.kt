package com.kujawski.pocketscores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.models.NFLGame

class TeamGamesAdapter(private val games: List<NFLGame>) :
    RecyclerView.Adapter<TeamGamesAdapter.TeamGameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return TeamGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamGameViewHolder, position: Int) {
        val game = games[position]
        holder.bind(game)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    class TeamGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gameDateTextView: TextView = itemView.findViewById(R.id.gameDateTextView)
        private val teamsTextView: TextView = itemView.findViewById(R.id.teamsTextView)
        private val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)

        fun bind(game: NFLGame) {
            gameDateTextView.text = game.date
            teamsTextView.text = "${game.homeTeam.name} vs ${game.awayTeam.name}"
            scoreTextView.text = "Score: ${game.score}"
        }
    }
}