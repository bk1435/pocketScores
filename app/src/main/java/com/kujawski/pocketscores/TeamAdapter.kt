package com.kujawski.pocketscores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.models.NFLTeam

class TeamAdapter(
    private val teams: List<NFLTeam>,
    private val onTeamSelected: (NFLTeam) -> Unit
) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = teams[position]
        holder.bind(team)
        holder.itemView.setOnClickListener {
            onTeamSelected(team)
        }
    }

    override fun getItemCount(): Int {
        return teams.size
    }

    class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val teamNameTextView: TextView = itemView.findViewById(R.id.teamNameTextView)

        fun bind(team: NFLTeam) {
            teamNameTextView.text = team.name
        }
    }
}
