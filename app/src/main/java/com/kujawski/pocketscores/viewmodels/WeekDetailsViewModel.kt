package com.kujawski.pocketscores.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kujawski.pocketscores.models.Game
import com.kujawski.pocketscores.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeekDetailsViewModel : ViewModel() {

    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> get() = _games

    fun loadGamesForWeek(weekNumber: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getGamesForWeek(
                        seasonType = 2,
                        week = weekNumber
                    ).execute()
                }

                if (response.isSuccessful) {
                    val events = response.body()?.events ?: emptyList()
                    Log.d("WeekDetailsViewModel", "Events: $events")


                    val games = events.map { event ->
                        val competition = event.competitions.firstOrNull()
                        Game(
                            date = event.date,
                            competitions = listOfNotNull(competition)
                        )
                    }


                    val correctLabel = when (weekNumber) {
                        -4 -> "Wild Card Round"
                        -3 -> "Divisional Round"
                        -2 -> "Conference Championships"
                        -1 -> "Super Bowl"
                        else -> response.body()?.label ?: "Week $weekNumber"
                    }

                    Log.d("WeekDetailsViewModel", "Week Label: $correctLabel")


                    _games.postValue(games)
                } else {
                    Log.e("WeekDetailsViewModel", "Error: ${response.errorBody()?.string()}")
                    _games.postValue(emptyList())
                }
            } catch (e: Exception) {
                Log.e("WeekDetailsViewModel", "Exception occurred", e)
                _games.postValue(emptyList())
            }
        }
    }

}
