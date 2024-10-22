package com.kujawski.pocketscores.models

data class NFLGame(
    val id: String,
    val date: String,
    val homeTeam: NFLTeam,
    val awayTeam: NFLTeam,
    val score: String
)
