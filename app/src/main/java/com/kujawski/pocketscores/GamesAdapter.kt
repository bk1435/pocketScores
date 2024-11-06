package com.kujawski.pocketscores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GamesAdapter(
    private val gamesList: List<Game>
) : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameDateTextView: TextView = itemView.findViewById(R.id.gameDateTextView)
        val gameNameTextView: TextView = itemView.findViewById(R.id.gameNameTextView)
        val gameScoreTextView: TextView = itemView.findViewById(R.id.gameScoreTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gamesList[position]
        val competition = game.competitions.firstOrNull()
        val scoreSummary = competition?.competitors?.joinToString(" vs ") {
            "${it.team.displayName} (${it.score})"
        } ?: "Score Unavailable"

        holder.gameDateTextView.text = game.date
        holder.gameNameTextView.text = game.shortName
        holder.gameScoreTextView.text = scoreSummary
    }

    override fun getItemCount() = gamesList.size
}
