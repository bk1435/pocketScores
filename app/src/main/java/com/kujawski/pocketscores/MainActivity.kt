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


        fetchNFLData()
    }

    private fun fetchNFLData() {
        val apiService = RetrofitInstance.api
        val call = apiService.getNFLScoreboard()

        call.enqueue(object : Callback<NFLScoreboardResponse> {
            override fun onResponse(call: Call<NFLScoreboardResponse>, response: Response<NFLScoreboardResponse>) {
                if (response.isSuccessful) {
                    val nflData = response.body()


                    Log.d("API Raw JSON", "Raw JSON response: $nflData")


                    nflDataTextView.text = formatNFLData(nflData)
                } else {
                    Log.d("API Response", "Unsuccessful response: ${response.errorBody()?.string()}")
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

        if (nflData == null || nflData.events.isNullOrEmpty()) {
            Log.d("Data Formatting", "NFL data is null or events list is empty.")
            return getString(R.string.no_data_available)
        }


        return nflData.events.joinToString(separator = "\n") { event ->
            val competition = event.competitions?.firstOrNull()
            val homeTeamName = competition?.competitors?.getOrNull(0)?.team?.displayName ?: "N/A"
            val awayTeamName = competition?.competitors?.getOrNull(1)?.team?.displayName ?: "N/A"


            Log.d("Data Formatting", "Game: $homeTeamName vs $awayTeamName")
            "$homeTeamName vs $awayTeamName"
        }
    }
}
