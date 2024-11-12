package com.kujawski.pocketscores.models

import android.util.Log

data class Score(
    val value: Int? = null
) {

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

