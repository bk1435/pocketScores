package com.kujawski.pocketscores

import com.kujawski.pocketscores.models.Game
import com.kujawski.pocketscores.models.LeagueTeam

data class TeamGamesResponse(
    val events: List<Game>
)

data class Game(
    val date: String,
    val shortName: String,
    val competitions: List<Competition>
)

data class Competition(
    val competitors: List<Competitor>,
    val status: Status
)

data class Competitor(
    val team: LeagueTeam,
    val score: String
)

data class Status(
    val type: StatusType
)

data class StatusType(
    val description: String
)