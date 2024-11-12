package com.kujawski.pocketscores.models

data class SportsResponse(
    val sports: List<Sport>
)

data class Sport(
    val leagues: List<League>
)

data class League(
    val teams: List<TeamWrapper>
)

data class TeamWrapper(
    val team: LeagueTeam
)
