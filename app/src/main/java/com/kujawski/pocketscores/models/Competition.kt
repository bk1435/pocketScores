package com.kujawski.pocketscores.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Competition(
    val competitors: List<Competitor> = emptyList(),
    val status: Status,
    val id: String
) : Parcelable

@Parcelize
data class Status(
    val type: StatusType,
    val quarter: Int? = null,
    val timeLeft: String? = null
) : Parcelable

@Parcelize
data class StatusType(
    val description: String
) : Parcelable
