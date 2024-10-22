package com.kujawski.pocketscores.models

import com.google.gson.annotations.SerializedName

data class NFLScoreboardResponse(
    @SerializedName("week")
    val week: Int,
    @SerializedName("games")
    val games: List<NFLGame>
)

