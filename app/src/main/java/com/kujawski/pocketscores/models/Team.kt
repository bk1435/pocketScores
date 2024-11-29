package com.kujawski.pocketscores.models

import org.json.JSONObject

data class Team(
    val name: String,
    val id: String,
    val displayName: String,
    val logoUrl: String? = null // Optional for flexibility
) {
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
                val logoUrl = teamObj.getJSONArray("logos").getJSONObject(0).getString("href") // Get the logo URL
                teamList.add(Team(name, id, displayName, logoUrl))
            }

            return teamList
        }
    }
}
