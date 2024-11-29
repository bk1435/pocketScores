package com.kujawski.pocketscores.models

data class LeagueTeam(
    val id: String,
    val displayName: String,
    val logos: List<Logo>
)

data class Logo(
    val href: String
)