package com.kujawski.pocketscores.models

data class LeagueTeam(
    val id: String,
    val displayName: String,
    val logos: List<Logo> // Change this to a list of Logo objects
)

data class Logo(
    val href: String // Represents the URL for the logo
)
