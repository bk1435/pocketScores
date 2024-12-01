package com.kujawski.pocketscores.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.models.Athlete
import com.kujawski.pocketscores.models.Game
import com.kujawski.pocketscores.models.GameSummaryResponse
import com.kujawski.pocketscores.models.PlayerStats
import com.kujawski.pocketscores.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatLeadersActivity : AppCompatActivity() {

    private lateinit var homeTeamStatLeadersLabel: TextView
    private lateinit var awayTeamStatLeadersLabel: TextView

    private lateinit var homeTeamPassingLeaderImage: ImageView
    private lateinit var homeTeamPassingLeaderName: TextView
    private lateinit var homeTeamPassingLeaderStats: TextView
    private lateinit var homeTeamRushingLeaderImage: ImageView
    private lateinit var homeTeamRushingLeaderName: TextView
    private lateinit var homeTeamRushingLeaderStats: TextView
    private lateinit var homeTeamReceivingLeaderImage: ImageView
    private lateinit var homeTeamReceivingLeaderName: TextView
    private lateinit var homeTeamReceivingLeaderStats: TextView

    private lateinit var awayTeamPassingLeaderImage: ImageView
    private lateinit var awayTeamPassingLeaderName: TextView
    private lateinit var awayTeamPassingLeaderStats: TextView
    private lateinit var awayTeamRushingLeaderImage: ImageView
    private lateinit var awayTeamRushingLeaderName: TextView
    private lateinit var awayTeamRushingLeaderStats: TextView
    private lateinit var awayTeamReceivingLeaderImage: ImageView
    private lateinit var awayTeamReceivingLeaderName: TextView
    private lateinit var awayTeamReceivingLeaderStats: TextView

    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var repository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat_leaders)


        homeTeamStatLeadersLabel = findViewById(R.id.homeTeamStatLeadersLabel)
        awayTeamStatLeadersLabel = findViewById(R.id.awayTeamStatLeadersLabel)

        homeTeamPassingLeaderImage = findViewById(R.id.homeTeamPassingLeaderImage)
        homeTeamPassingLeaderName = findViewById(R.id.homeTeamPassingLeaderName)
        homeTeamPassingLeaderStats = findViewById(R.id.homeTeamPassingLeaderStats)
        homeTeamRushingLeaderImage = findViewById(R.id.homeTeamRushingLeaderImage)
        homeTeamRushingLeaderName = findViewById(R.id.homeTeamRushingLeaderName)
        homeTeamRushingLeaderStats = findViewById(R.id.homeTeamRushingLeaderStats)
        homeTeamReceivingLeaderImage = findViewById(R.id.homeTeamReceivingLeaderImage)
        homeTeamReceivingLeaderName = findViewById(R.id.homeTeamReceivingLeaderName)
        homeTeamReceivingLeaderStats = findViewById(R.id.homeTeamReceivingLeaderStats)

        awayTeamPassingLeaderImage = findViewById(R.id.awayTeamPassingLeaderImage)
        awayTeamPassingLeaderName = findViewById(R.id.awayTeamPassingLeaderName)
        awayTeamPassingLeaderStats = findViewById(R.id.awayTeamPassingLeaderStats)
        awayTeamRushingLeaderImage = findViewById(R.id.awayTeamRushingLeaderImage)
        awayTeamRushingLeaderName = findViewById(R.id.awayTeamRushingLeaderName)
        awayTeamRushingLeaderStats = findViewById(R.id.awayTeamRushingLeaderStats)
        awayTeamReceivingLeaderImage = findViewById(R.id.awayTeamReceivingLeaderImage)
        awayTeamReceivingLeaderName = findViewById(R.id.awayTeamReceivingLeaderName)
        awayTeamReceivingLeaderStats = findViewById(R.id.awayTeamReceivingLeaderStats)

        loadingProgressBar = findViewById(R.id.loadingProgressBar)

        repository = GameRepository()

        val game: Game? = intent.getParcelableExtra("GAME_DATA")
        if (game != null) {
            val eventId = game.eventId
            Log.d("StatLeadersActivity", "Event ID received: $eventId")
            fetchGameSummary(eventId)
        } else {
            Log.e("StatLeadersActivity", "Failed to load game data. Game object is null.")
            Toast.makeText(this, "Failed to load game data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchGameSummary(eventId: String) {
        Log.d("StatLeadersActivity", "Fetching game summary for event ID: $eventId")
        loadingProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                repository.getGameSummary(eventId) { gameSummary: GameSummaryResponse? ->
                    if (gameSummary != null) {
                        Log.d("StatLeadersActivity", "Game Summary fetched successfully.")
                        runOnUiThread {
                            displayStatLeaders(gameSummary)
                            loadingProgressBar.visibility = View.GONE
                        }
                    } else {
                        Log.e("StatLeadersActivity", "Game Summary is null. Could not fetch data.")
                        runOnUiThread {
                            Toast.makeText(
                                this@StatLeadersActivity,
                                "Failed to load game data",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadingProgressBar.visibility = View.GONE
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("StatLeadersActivity", "Exception during fetchGameSummary: ${e.message}")
                runOnUiThread {
                    Toast.makeText(
                        this@StatLeadersActivity,
                        "An error occurred while fetching data.",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingProgressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun displayStatLeaders(gameSummary: GameSummaryResponse) {
        Log.d("StatLeadersActivity", "Displaying stat leaders for the game.")
        val playersStats = gameSummary.boxscore?.players ?: return
        val teamStats = gameSummary.boxscore?.teams ?: return

        if (teamStats.size != 2) {
            Log.e(
                "StatLeadersActivity",
                "Unexpected number of teams in teamStats: ${teamStats.size}"
            )
            return
        }

        val homeTeam = teamStats[0].team
        val awayTeam = teamStats[1].team


        homeTeamStatLeadersLabel.text = "${homeTeam.displayName} Stat Leaders"
        awayTeamStatLeadersLabel.text = "${awayTeam.displayName} Stat Leaders"

        val homeTeamStats = playersStats.filter { it.team.id == homeTeam.id }
        val awayTeamStats = playersStats.filter { it.team.id == awayTeam.id }

        displayTeamStatLeaders(
            homeTeamStats,
            homeTeamPassingLeaderImage,
            homeTeamPassingLeaderName,
            homeTeamPassingLeaderStats,
            "passing"
        )
        displayTeamStatLeaders(
            homeTeamStats,
            homeTeamRushingLeaderImage,
            homeTeamRushingLeaderName,
            homeTeamRushingLeaderStats,
            "rushing"
        )
        displayTeamStatLeaders(
            homeTeamStats,
            homeTeamReceivingLeaderImage,
            homeTeamReceivingLeaderName,
            homeTeamReceivingLeaderStats,
            "receiving"
        )

        displayTeamStatLeaders(
            awayTeamStats,
            awayTeamPassingLeaderImage,
            awayTeamPassingLeaderName,
            awayTeamPassingLeaderStats,
            "passing"
        )
        displayTeamStatLeaders(
            awayTeamStats,
            awayTeamRushingLeaderImage,
            awayTeamRushingLeaderName,
            awayTeamRushingLeaderStats,
            "rushing"
        )
        displayTeamStatLeaders(
            awayTeamStats,
            awayTeamReceivingLeaderImage,
            awayTeamReceivingLeaderName,
            awayTeamReceivingLeaderStats,
            "receiving"
        )
    }

    private fun displayTeamStatLeaders(
        teamStats: List<PlayerStats>,
        imageView: ImageView,
        nameView: TextView,
        statsView: TextView,
        statName: String
    ) {
        val leader = findLeader(teamStats, statName)
        leader?.let { athlete ->
            val headshotUrl =
                "https://a.espncdn.com/i/headshots/nfl/players/full/${athlete.athlete.id}.png"

            Glide.with(this)
                .load(headshotUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(imageView)

            nameView.text = getFormattedName(athlete.athlete.displayName)
            statsView.text = formatStats(statName, athlete.stats)
        } ?: run {
            nameView.text = getString(R.string.no_passing_leader)
            statsView.text = ""
        }
    }

    private fun findLeader(playersStats: List<PlayerStats>, statName: String): Athlete? {
        return playersStats.flatMap { it.statistics }
            .filter { it.name.equals(statName, ignoreCase = true) }
            .flatMap { it.athletes }
            .maxByOrNull { it.stats.getOrNull(1)?.toIntOrNull() ?: 0 }
    }

    private fun formatStats(statName: String, stats: List<String>): String {
        val formattedStats = when (statName.lowercase()) {
            "passing" -> {
                val attempts = stats.getOrNull(0) ?: "N/A"
                val yards = stats.getOrNull(1) ?: "N/A"
                val touchdowns = stats.getOrNull(3)?.takeIf { it != "0" }?.let { "$it TDS" } ?: ""
                val interceptions = stats.getOrNull(4)?.takeIf { it != "0" }?.let { "$it INT" } ?: ""

                listOf("$attempts", "$yards YDS", touchdowns, interceptions)
                    .filter { it.isNotEmpty() }
                    .joinToString("\n")
            }

            "rushing" -> {
                val carries = stats.getOrNull(0) ?: "N/A"
                val yards = stats.getOrNull(1) ?: "N/A"
                val touchdowns = stats.getOrNull(3)?.takeIf { it != "0" }?.let { "$it TDS" } ?: ""

                listOf("$carries CAR", "$yards YDS", touchdowns)
                    .filter { it.isNotEmpty() }
                    .joinToString("\n")
            }

            "receiving" -> {
                val receptions = stats.getOrNull(0) ?: "N/A"
                val yards = stats.getOrNull(1) ?: "N/A"
                val touchdowns = stats.getOrNull(3)?.takeIf { it != "0" }?.let { "$it TDS" } ?: ""

                listOf("$receptions REC", "$yards YDS", touchdowns)
                    .filter { it.isNotEmpty() }
                    .joinToString("\n")
            }

            else -> {
                stats.joinToString("\n")
            }
        }
        return formattedStats
    }

    private fun getFormattedName(fullName: String): String {
        val nameParts = fullName.split(" ")
        return if (nameParts.size > 1) {
            val firstInitial = nameParts[0].firstOrNull()?.toString() ?: ""
            val lastName = nameParts[1]
            "$firstInitial. $lastName"
        } else {
            fullName
        }
    }
}

