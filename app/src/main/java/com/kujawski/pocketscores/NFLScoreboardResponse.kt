package com.kujawski.pocketscores

import com.kujawski.pocketscores.models.Competition

data class NFLScoreboardResponse(
    val events: List<Event>
)

data class Event(
    val name: String,
    val status: GameStatus,
    val date: String,
    val competitions: List<Competition>
)

data class GameStatus(
    val type: GameStatusType
)

data class GameStatusType(
    val name: String,
    val detail: String
)