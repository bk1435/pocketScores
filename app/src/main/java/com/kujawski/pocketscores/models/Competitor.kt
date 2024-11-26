package com.kujawski.pocketscores.models

data class Competitor(
    val team: Team,
    val score: Score?,
    val type: String,
    val homeAway: String
)