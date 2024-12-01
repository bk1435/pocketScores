package com.kujawski.pocketscores.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Competitor(
    val team: Team,
    val score: Score?,
    val type: String,
    val homeAway: String,
    val playerStats: List<PlayerStats>? = null
) : Parcelable
