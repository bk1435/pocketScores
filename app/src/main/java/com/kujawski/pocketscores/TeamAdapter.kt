package com.kujawski.pocketscores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.models.LeagueTeam

class TeamAdapter(private val onTeamSelected: (LeagueTeam) -> Unit) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    private val teams = mutableListOf<LeagueTeam>()

    fun submitList(teamsList: List<LeagueTeam>?) {
        teams.clear()
        if (teamsList != null) {
            teams.addAll(teamsList)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = teams[position]
        holder.bind(team)
    }

    override fun getItemCount(): Int = teams.size

    inner class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val teamNameTextView: TextView = itemView.findViewById(R.id.teamNameTextView)

        fun bind(team: LeagueTeam) {
            teamNameTextView.text = team.displayName
            itemView.setOnClickListener { onTeamSelected(team) }
        }
    }
}