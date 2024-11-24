package com.kujawski.pocketscores.models

object ESPNModels {
    data class ApiResponse(
        val sports: List<Sport>
    )

    data class Sport(
        val leagues: List<League>
    )

    data class League(
        val teams: List<TeamWrapper>
    )


    data class TeamWrapper(
        val team: Team
    )

    data class Team(
        val id: String,
        val uid: String,
        val location: String,
        val name: String,
        val abbreviation: String,
        val displayName: String,
        val shortDisplayName: String,
        val color: String,
        val alternateColor: String,
        val logos: List<Logo>
    )

    data class Logo(
        val href: String,
        val rel: List<String>,
        val width: Int,
        val height: Int
    )
}