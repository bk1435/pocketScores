package com.kujawski.pocketscores.models

import com.google.gson.annotations.SerializedName

data class NFLGame(
    @SerializedName("competitions")
    val competitions: List<Competition>?
)

data class Competition(
    @SerializedName("competitors")
    val competitors: List<Competitor>?
)

data class Competitor(
    @SerializedName("team")
    val team: Team?
)

data class Team(
    @SerializedName("displayName")
    val displayName: String
)
