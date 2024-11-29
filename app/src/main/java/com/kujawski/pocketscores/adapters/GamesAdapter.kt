package com.kujawski.pocketscores.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.models.Game
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class GamesAdapter : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    private val gamesList = mutableListOf<Game>()
    private var favoriteTeamId: String? = null
    private var teamMap: Map<String, String?> = mutableMapOf()


    fun submitList(games: List<Game>, favoriteTeamId: String, teamMap: Map<String, String?>) {
        this.favoriteTeamId = favoriteTeamId
        this.teamMap = teamMap
        gamesList.clear()
        gamesList.addAll(games)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view, teamMap)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gamesList[position]
        holder.bind(game)
    }

    override fun getItemCount(): Int = gamesList.size


    class GameViewHolder(itemView: View, private val teamMap: Map<String, String?>) : RecyclerView.ViewHolder(itemView) {
        private val gameTitle: TextView = itemView.findViewById(R.id.game_title)
        private val gameDate: TextView = itemView.findViewById(R.id.game_date)
        private val gameScores: TextView = itemView.findViewById(R.id.game_scores)
        private val gameStatus: TextView = itemView.findViewById(R.id.game_status)
        private val homeTeamLogo: ImageView = itemView.findViewById(R.id.home_team_logo)
        private val awayTeamLogo: ImageView = itemView.findViewById(R.id.away_team_logo)
        private val gameQuarter: TextView = itemView.findViewById(R.id.game_quarter)
        private val timeLeft: TextView = itemView.findViewById(R.id.time_left)

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

            val formattedDate = formatDate(game.date)

            gameTitle.text = title
            gameDate.text = formattedDate
            gameScores.text = scores
            gameStatus.text = status


            val gameQuarterText = game.competitions.firstOrNull()?.status?.quarter?.toString() ?: "Final"
            val gameTimeLeftText = game.competitions.firstOrNull()?.status?.timeLeft ?: "N/A"

            if (status == "STATUS_IN_PROGRESS") {
                gameQuarter.text = "Quarter: $gameQuarterText"
                timeLeft.text = "Time Left: $gameTimeLeftText"
                gameQuarter.visibility = View.VISIBLE
                timeLeft.visibility = View.VISIBLE
            } else {

                gameQuarter.visibility = View.GONE
                timeLeft.visibility = View.GONE
            }


            val homeLogoUrl = teamMap[homeTeam.id]
            val awayLogoUrl = teamMap[awayTeam.id]

            Glide.with(itemView.context)
                .load(homeLogoUrl ?: R.drawable.error_logo)
                .placeholder(R.drawable.placeholder_logo)
                .into(homeTeamLogo)

            Glide.with(itemView.context)
                .load(awayLogoUrl ?: R.drawable.error_logo)
                .placeholder(R.drawable.placeholder_logo)
                .into(awayTeamLogo)
        }


        private fun formatDate(dateString: String): String {
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
