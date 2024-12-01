package com.kujawski.pocketscores.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Game(
    val date: String,
    @SerializedName("competitions") val competitions: List<Competition> = emptyList(),
    val eventId: String
) : Parcelable