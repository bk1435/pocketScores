package com.kujawski.pocketscores.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.models.Game

class GamesAdapter : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    private val gamesList = mutableListOf<Game>()
    private var favoriteTeamId: String? = null

    fun submitList(games: List<Game>, favoriteTeamId: String) {
        this.favoriteTeamId = favoriteTeamId
        gamesList.clear()
        gamesList.addAll(games)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gamesList[position]
        holder.bind(game)
    }

    override fun getItemCount(): Int = gamesList.size

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gameTitle: TextView = itemView.findViewById(R.id.game_title)
        private val gameDate: TextView = itemView.findViewById(R.id.game_date)
        private val gameScores: TextView = itemView.findViewById(R.id.game_scores)
        private val gameStatus: TextView = itemView.findViewById(R.id.game_status)

        fun bind(game: Game) {
            val competitors = game.competitions.firstOrNull()?.competitors ?: emptyList()

            // Handle cases where competitors are missing or incomplete
            if (competitors.size < 2) {
                gameTitle.text = "Invalid Game Data"
                gameScores.text = "N/A"
                gameStatus.text = "N/A"
                return
            }

            // Use the homeAway property to determine teams
            val homeCompetitor = competitors.find { it.homeAway == "home" }
            val awayCompetitor = competitors.find { it.homeAway == "away" }

            if (homeCompetitor == null || awayCompetitor == null) {
                gameTitle.text = "Incomplete Data"
                gameScores.text = "N/A"
                gameStatus.text = "N/A"
                Log.w("GamesAdapter", "Missing home/away data for game on ${game.date}")
                return
            }

            val homeTeam = homeCompetitor.team
            val awayTeam = awayCompetitor.team
            val homeScore = homeCompetitor.score?.value ?: 0
            val awayScore = awayCompetitor.score?.value ?: 0

            // Construct title without altering home/away logic
            val title = "${awayTeam.displayName} @ ${homeTeam.displayName}"

            gameTitle.text = title
            gameDate.text = game.date
            gameScores.text = "$awayScore - $homeScore"
            gameStatus.text = game.competitions.firstOrNull()?.status?.type?.description ?: "Unknown"
        }
    }
}
