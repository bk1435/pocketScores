package com.kujawski.pocketscores

import android.os.Parcelable
import com.kujawski.pocketscores.models.Competition
import kotlinx.parcelize.Parcelize

@Parcelize
data class NFLScoreboardResponse(
    val events: List<Event>,
    val label: String?
) : Parcelable

@Parcelize
data class Event(
    val name: String,
    val status: GameStatus,
    val date: String,
    val competitions: List<Competition>
) : Parcelable

@Parcelize
data class GameStatus(
    val type: GameStatusType
) : Parcelable

@Parcelize
data class GameStatusType(
    val name: String,
    val detail: String
) : Parcelable
