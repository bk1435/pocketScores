package com.kujawski.pocketscores.models

import com.google.gson.annotations.SerializedName

data class NFLScoreboardResponse(
    @SerializedName("week")
    val week: Week?,
    @SerializedName("games")
    val games: List<NFLGame> = emptyList() // Default to empty list if null
)

data class Week(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("type")
    val type: String?
)
