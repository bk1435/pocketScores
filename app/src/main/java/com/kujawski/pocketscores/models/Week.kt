package com.kujawski.pocketscores.models

data class Week(
    val weekNumber: Int,
    val games: List<Any> = emptyList(),
    val label: String = "Week $weekNumber"
)