package com.kujawski.pocketscores.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.models.Athlete
import com.kujawski.pocketscores.models.Game
import com.kujawski.pocketscores.models.GameSummaryResponse
import com.kujawski.pocketscores.models.PlayerStats
import com.kujawski.pocketscores.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatLeadersActivity : AppCompatActivity() {


    private lateinit var homeTeamPassingLeaderTextView: TextView
    private lateinit var homeTeamRushingLeaderTextView: TextView
    private lateinit var homeTeamReceivingLeaderTextView: TextView


    private lateinit var awayTeamPassingLeaderTextView: TextView
    private lateinit var awayTeamRushingLeaderTextView: TextView
    private lateinit var awayTeamReceivingLeaderTextView: TextView

    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var repository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat_leaders)


        homeTeamPassingLeaderTextView = findViewById(R.id.homeTeamPassingLeaderTextView)
        homeTeamRushingLeaderTextView = findViewById(R.id.homeTeamRushingLeaderTextView)
        homeTeamReceivingLeaderTextView = findViewById(R.id.homeTeamReceivingLeaderTextView)

        awayTeamPassingLeaderTextView = findViewById(R.id.awayTeamPassingLeaderTextView)
        awayTeamRushingLeaderTextView = findViewById(R.id.awayTeamRushingLeaderTextView)
        awayTeamReceivingLeaderTextView = findViewById(R.id.awayTeamReceivingLeaderTextView)

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

        Log.d("StatLeadersActivity", "Number of player stats received: ${playersStats.size}")

        if (teamStats.size != 2) {
            Log.e("StatLeadersActivity", "Unexpected number of teams in teamStats: ${teamStats.size}")
            return
        }

        val homeTeam = teamStats[0].team
        val awayTeam = teamStats[1].team

        Log.d("StatLeadersActivity", "Home Team: ${homeTeam.displayName}")
        Log.d("StatLeadersActivity", "Away Team: ${awayTeam.displayName}")

        val homeTeamStats = playersStats.filter { it.team.id == homeTeam.id }
        val awayTeamStats = playersStats.filter { it.team.id == awayTeam.id }

        Log.d("StatLeadersActivity", "Number of home team stats: ${homeTeamStats.size}")
        Log.d("StatLeadersActivity", "Number of away team stats: ${awayTeamStats.size}")


        displayTeamStatLeaders(
            homeTeamStats,
            homeTeamPassingLeaderTextView,
            homeTeamRushingLeaderTextView,
            homeTeamReceivingLeaderTextView
        )


        displayTeamStatLeaders(
            awayTeamStats,
            awayTeamPassingLeaderTextView,
            awayTeamRushingLeaderTextView,
            awayTeamReceivingLeaderTextView
        )
    }

    private fun displayTeamStatLeaders(
        teamStats: List<PlayerStats>,
        passingLeaderTextView: TextView,
        rushingLeaderTextView: TextView,
        receivingLeaderTextView: TextView
    ) {
        val passingLeader = findLeader(teamStats, "passing")
        passingLeader?.let { athlete ->
            passingLeaderTextView.text = "Passing Leader: ${athlete.athlete.displayName}\n" +
                    "Stats: ${formatStats(athlete.stats)}"
        } ?: run {
            passingLeaderTextView.text = getString(R.string.no_passing_leader)
        }

        val rushingLeader = findLeader(teamStats, "rushing")
        rushingLeader?.let { athlete ->
            rushingLeaderTextView.text = "Rushing Leader: ${athlete.athlete.displayName}\n" +
                    "Stats: ${formatStats(athlete.stats)}"
        } ?: run {
            rushingLeaderTextView.text = getString(R.string.no_rushing_leader)
        }

        val receivingLeader = findLeader(teamStats, "receiving")
        receivingLeader?.let { athlete ->
            receivingLeaderTextView.text = "Receiving Leader: ${athlete.athlete.displayName}\n" +
                    "Stats: ${formatStats(athlete.stats)}"
        } ?: run {
            receivingLeaderTextView.text = getString(R.string.no_receiving_leader)
        }
    }

    private fun findLeader(playersStats: List<PlayerStats>, statName: String): Athlete? {
        var leader: Athlete? = null
        var highestValue = 0

        for (playerStats in playersStats) {
            val stat = playerStats.statistics.find { it.name.equals(statName, ignoreCase = true) }
            if (stat != null) {
                val athlete = stat.athletes.firstOrNull()
                if (athlete != null) {

                    val statValue = athlete.stats.getOrNull(1)?.toIntOrNull() ?: 0
                    if (statValue > highestValue) {
                        highestValue = statValue
                        leader = athlete
                    }
                }
            }
        }

        return leader
    }

    private fun formatStats(stats: List<String>): String {
        val labels = listOf("Attempts", "Yards", "Average", "Touchdowns", "Longest")
        return stats.mapIndexed { index, stat ->
            "${labels.getOrElse(index) { "Stat $index" }}: $stat"
        }.joinToString(separator = ", ")
    }
}

