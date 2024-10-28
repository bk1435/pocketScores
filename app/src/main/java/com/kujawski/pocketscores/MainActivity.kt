package com.kujawski.pocketscores

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kujawski.pocketscores.models.NFLScoreboardResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var nflDataTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nflDataTextView = findViewById(R.id.nflDataTextView)
        fetchNFLScoreboard()
    }

    private fun fetchNFLScoreboard() {
        val apiService = RetrofitInstance.api
        val call = apiService.getNFLScoreboard()

        call.enqueue(object : Callback<NFLScoreboardResponse> {
            override fun onResponse(call: Call<NFLScoreboardResponse>, response: Response<NFLScoreboardResponse>) {
                if (response.isSuccessful) {
                    val nflData = response.body()
                    Log.d("API Response", "Fetched data: $nflData")
                    val formattedData = formatNFLData(nflData)
                    nflDataTextView.text = formattedData
                } else {
                    nflDataTextView.text = getString(R.string.no_data_available)
                }
            }

            override fun onFailure(call: Call<NFLScoreboardResponse>, t: Throwable) {
                Log.e("API Error", "Network error: ${t.localizedMessage}")
                nflDataTextView.text = getString(R.string.network_error, t.localizedMessage)
            }
        })
    }

    private fun formatNFLData(nflData: NFLScoreboardResponse?): String {
        if (nflData == null || nflData.games.isNullOrEmpty()) {
            Log.d("Data Formatting", "NFL data is null or games list is empty.")
            return getString(R.string.no_data_available)
        }

        return nflData.games.joinToString(separator = "\n") { game ->
            val competition = game.competitions?.firstOrNull()
            val team1 = competition?.competitors?.getOrNull(0)?.team?.displayName ?: "Unknown Team"
            val team2 = competition?.competitors?.getOrNull(1)?.team?.displayName ?: "Unknown Team"
            "$team1 vs $team2"
        }
    }
}
