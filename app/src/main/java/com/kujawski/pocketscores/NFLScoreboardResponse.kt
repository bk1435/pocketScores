package com.kujawski.pocketscores

data class NFLScoreboardResponse(
    val events: List<Event>
)

data class Event(
    val name: String,
    val status: GameStatus,
    val date: String
)

data class GameStatus(
    val type: GameStatusType
)

data class GameStatusType(
    val name: String,
    val detail: String
)
