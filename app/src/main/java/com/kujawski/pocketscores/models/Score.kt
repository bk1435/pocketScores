package com.kujawski.pocketscores.models

import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize

@Parcelize
data class Score(
    val value: Int? = null
) : Parcelable {
    constructor(scoreString: String?) : this(scoreString?.let {
        if (it == "TBD") {
            Log.d("Score", "Score is TBD")
            null
        } else {
            it.toIntOrNull().also { score ->
                Log.d("Score", "Parsed score: $score")
            }
        }
    })
}
