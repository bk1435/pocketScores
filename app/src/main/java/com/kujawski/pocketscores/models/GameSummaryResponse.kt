package com.kujawski.pocketscores.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSummaryResponse(
    val boxscore: Boxscore?
) : Parcelable
