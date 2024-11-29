package com.kujawski.pocketscores.models

import com.google.gson.annotations.SerializedName

data class Game(
    val date: String,
    @SerializedName("competitions") val competitions: List<Competition>
)

data class Competition(
    val competitors: List<Competitor>,
    val status: Status
)


data class Status(
    val type: StatusType,
    val quarter: Int? = null,
    val timeLeft: String? = null
)

data class StatusType(
    val description: String
)