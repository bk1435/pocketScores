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
        Log.d("GamesAdapter", "Favorite Team ID: $favoriteTeamId") // Debug log
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
        holder.bind(game, favoriteTeamId)
    }

    override fun getItemCount(): Int = gamesList.size

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gameTitle: TextView = itemView.findViewById(R.id.game_title)
        private val gameDate: TextView = itemView.findViewById(R.id.game_date)
        private val gameScores: TextView = itemView.findViewById(R.id.game_scores)
        private val gameStatus: TextView = itemView.findViewById(R.id.game_status)

        fun bind(game: Game, favoriteTeamId: String?) {
            val competitors = game.competitions.firstOrNull()?.competitors ?: emptyList()

            if (competitors.isEmpty()) {
                gameTitle.text = "Incomplete Game Data"
                gameScores.text = ""
                gameStatus.text = ""
                return
            }

            competitors.forEach {
                Log.d("GamesAdapter", "Competitor: ${it.team.displayName}, HomeAway: ${it.homeAway}, TeamID: ${it.team.id}")
            }

            // Find competitors by their home/away role
            val homeCompetitor = competitors.find { it.homeAway == "home" }
            val awayCompetitor = competitors.find { it.homeAway == "away" }

            if (homeCompetitor == null || awayCompetitor == null) {
                gameTitle.text = "Invalid Game Data"
                gameScores.text = ""
                gameStatus.text = ""
                return
            }

            val homeTeam = homeCompetitor.team
            val awayTeam = awayCompetitor.team
            val homeScore = homeCompetitor.score?.value?.toInt() ?: 0
            val awayScore = awayCompetitor.score?.value?.toInt() ?: 0

            // Check if the favorite team is home or away
            val isFavoriteHome = favoriteTeamId == homeTeam.id
            val isFavoriteAway = favoriteTeamId == awayTeam.id

            // Format game title
            val title = when {
                isFavoriteHome -> "${awayTeam.displayName} @ ${homeTeam.displayName}"
                isFavoriteAway -> "${homeTeam.displayName} @ ${awayTeam.displayName}"
                else -> "${homeTeam.displayName} vs ${awayTeam.displayName}"
            }

            // Display scores and status
            val scores = "$awayScore - $homeScore"
            val status = game.competitions.firstOrNull()?.status?.type?.description ?: "Unknown"

            gameTitle.text = title
            gameDate.text = game.date
            gameScores.text = scores
            gameStatus.text = status
        }
    }
}
