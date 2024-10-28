package com.kujawski.pocketscores.models

import com.google.gson.annotations.SerializedName

data class NFLTeam(
    @SerializedName("id") val id: String,
    @SerializedName("displayName") val displayName: String  // Ensure this is present
)
