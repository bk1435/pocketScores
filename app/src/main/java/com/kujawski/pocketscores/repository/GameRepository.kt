package com.kujawski.pocketscores.repository

import android.util.Log
import com.kujawski.pocketscores.models.GameSummaryResponse
import com.kujawski.pocketscores.network.ESPNApiService
import com.kujawski.pocketscores.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameRepository(private val apiService: ESPNApiService = RetrofitInstance.api) {

    private val TAG = "GameRepository"

    fun getGameSummary(eventId: String, onResult: (GameSummaryResponse?) -> Unit) {
        Log.d(TAG, "Fetching game summary for event ID: $eventId")
        apiService.getGameSummary(eventId).enqueue(object : Callback<GameSummaryResponse> {
            override fun onResponse(call: Call<GameSummaryResponse>, response: Response<GameSummaryResponse>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "API call successful.")

                    val gameSummary = response.body()
                    if (gameSummary != null) {
                        Log.d(TAG, "Parsed Response: $gameSummary")
                        onResult(gameSummary)
                    } else {
                        val responseBodyString = response.errorBody()?.string()
                        Log.e(TAG, "API response body is null, response body string: $responseBodyString")
                        onResult(null)
                    }
                } else {
                    val responseBodyString = response.errorBody()?.string()
                    Log.e(TAG, "API call failed with code: ${response.code()}, response body: $responseBodyString")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<GameSummaryResponse>, t: Throwable) {
                Log.e(TAG, "API call failed: ${t.message}")
                onResult(null)
            }
        })
    }
}
