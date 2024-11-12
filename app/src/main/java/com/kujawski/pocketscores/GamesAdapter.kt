package com.kujawski.pocketscores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.models.Game
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GamesAdapter : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    private var gamesList: List<Game> = emptyList()

    fun submitList(games: List<Game>) {
        gamesList = games
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(gamesList[position])
    }

    override fun getItemCount(): Int = gamesList.size

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gameDateTextView: TextView = itemView.findViewById(R.id.gameDateTextView)
        private val gameOpponentTextView: TextView = itemView.findViewById(R.id.gameOpponentTextView)
        private val gameScoreTextView: TextView = itemView.findViewById(R.id.gameScoreTextView)
        private val gameStatusTextView: TextView = itemView.findViewById(R.id.gameStatusTextView)

        fun bind(game: Game) {
            // Set date
            gameDateTextView.text = formatDateTime(game.date)


            if (game.competitions.isNotEmpty()) {
                val competition = game.competitions[0]
                val competitors = competition.competitors

                if (competitors.size == 2) {
                    val homeTeam = competitors[0]
                    val awayTeam = competitors[1]


                    gameOpponentTextView.text = awayTeam.team.displayName // Display the opponent's team name


                    val homeScore = homeTeam.score?.value?.toString() ?: "TBD"
                    val awayScore = awayTeam.score?.value?.toString() ?: "TBD"

                    gameScoreTextView.text = "$homeScore - $awayScore"
                }


                gameStatusTextView.text = competition.status.type.description
            }
        }
    }


    private fun formatDateTime(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateTime)
        return outputFormat.format(date ?: Date())
    }
}