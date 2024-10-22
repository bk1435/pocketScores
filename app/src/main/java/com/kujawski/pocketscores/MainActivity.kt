package com.kujawski.pocketscores

import android.os.Bundle
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

        // Find the TextView in the layout
        nflDataTextView = findViewById(R.id.nflDataTextView)

        // Call the function to fetch NFL data when the app starts
        fetchNFLScoreboard()
    }

    // Function to fetch NFL scoreboard data
    private fun fetchNFLScoreboard() {
        // Make the API call using RetrofitInstance
        val call = RetrofitInstance.api.getNFLScoreboard()

        call.enqueue(object : Callback<NFLScoreboardResponse> {
            override fun onResponse(
                call: Call<NFLScoreboardResponse>,
                response: Response<NFLScoreboardResponse>
            ) {
                if (response.isSuccessful) {
                    val nflData = response.body()
                    if (nflData != null) {
                        val formattedData = formatNFLData(nflData)
                        nflDataTextView.text = formattedData
                    }
                }
            }

            override fun onFailure(call: Call<NFLScoreboardResponse>, t: Throwable) {
                nflDataTextView.text = "Failed to load data."
            }
        })
    }

    // Function to format NFL data for display
    private fun formatNFLData(nflData: NFLScoreboardResponse): String {
        // Here you can process the NFLScoreboardResponse and format it as needed
        // For simplicity, this example just returns the raw data
        return nflData.toString()
    }
}
