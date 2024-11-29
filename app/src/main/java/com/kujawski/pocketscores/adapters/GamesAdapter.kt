package com.kujawski.pocketscores.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.models.Game
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

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

            if (competitors.size < 2) {
                gameTitle.text = "Invalid Game Data"
                gameScores.text = "N/A"
                gameStatus.text = "N/A"
                return
            }

            val homeCompetitor = competitors.find { it.homeAway == "home" }
            val awayCompetitor = competitors.find { it.homeAway == "away" }

            if (homeCompetitor == null || awayCompetitor == null) {
                gameTitle.text = "Incomplete Data"
                gameScores.text = "N/A"
                gameStatus.text = "N/A"
                return
            }

            val homeTeam = homeCompetitor.team
            val awayTeam = awayCompetitor.team
            val homeScore = homeCompetitor.score?.value ?: 0
            val awayScore = awayCompetitor.score?.value ?: 0

            val title = "${awayTeam.displayName} @ ${homeTeam.displayName}"
            val scores = "$awayScore - $homeScore"
            val status = game.competitions.firstOrNull()?.status?.type?.description ?: "Unknown"


            Log.d("GamesAdapter", "Raw date string: ${game.date}")


            val formattedDate = formatDate(game.date)

            gameTitle.text = title
            gameDate.text = formattedDate
            gameScores.text = scores
            gameStatus.text = status
        }

        private fun formatDate(dateString: String): String {
            Log.d("GamesAdapter", "Attempting to parse date: $dateString")
            return try {

                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")

                val date = inputFormat.parse(dateString)
                if (date != null) {

                    val outputFormatDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    val outputFormatTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    "${outputFormatDate.format(date)} at ${outputFormatTime.format(date)}"
                } else {
                    "Invalid Date"
                }
            } catch (e: Exception) {
                Log.e("GamesAdapter", "Error parsing date: $dateString", e)
                "Invalid Date"
            }
        }
    }
}