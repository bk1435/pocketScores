package com.kujawski.pocketscores.models

import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class Team(
    val name: String,
    val id: String,
    val displayName: String,
    val logoUrl: String? = null
) : Parcelable {
    companion object {
        fun parseTeamsJson(jsonString: String): List<Team> {
            val teamList = mutableListOf<Team>()
            val jsonObject = JSONObject(jsonString)
            val leagues = jsonObject.getJSONArray("sports").getJSONObject(0).getJSONArray("leagues")
            val teams = leagues.getJSONObject(0).getJSONArray("teams")

            for (i in 0 until teams.length()) {
                val teamObj = teams.getJSONObject(i).getJSONObject("team")
                val name = teamObj.getString("name")
                val id = teamObj.getString("id")
                val displayName = teamObj.getString("displayName")

                Log.d("TeamParser", "Team JSON: ${teamObj.toString()}")

                val logoUrl = try {
                    val logosArray = teamObj.getJSONArray("logos")
                    Log.d("TeamParser", "Logos Array: ${logosArray.toString()}")
                    logosArray.getJSONObject(0).getString("href")
                } catch (e: Exception) {
                    Log.e("TeamParser", "Logo URL parsing error", e)
                    null
                }

                Log.d("TeamParser", "Logo URL: $logoUrl")

                teamList.add(Team(name, id, displayName, logoUrl))
            }

            return teamList
        }
    }
}
