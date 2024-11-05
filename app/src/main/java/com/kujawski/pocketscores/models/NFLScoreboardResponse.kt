package com.kujawski.pocketscores.models

import com.google.gson.annotations.SerializedName


data class NFLScoreboardResponse(
    @SerializedName("events")
    val events: List<Event>?
)


data class Event(
    @SerializedName("competitions")
    val competitions: List<Competition>?,

    @SerializedName("date")
    val date: String?
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
    val displayName: String?
)
