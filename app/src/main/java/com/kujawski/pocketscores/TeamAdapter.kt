package com.kujawski.pocketscores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.models.ESPNModels

class TeamAdapter(private val onItemClicked: (ESPNModels.Team) -> Unit) :
    RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    private val teams = mutableListOf<ESPNModels.Team>()

    fun setTeams(newTeams: List<ESPNModels.Team>) {
        teams.clear()
        teams.addAll(newTeams)
        notifyDataSetChanged() // Notify RecyclerView of data change
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_team, parent, false)
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = teams[position]
        holder.bind(team)
    }

    override fun getItemCount(): Int = teams.size

    inner class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val teamNameTextView: TextView = itemView.findViewById(R.id.teamNameTextView)

        fun bind(team: ESPNModels.Team) {
            teamNameTextView.text = team.displayName
            itemView.setOnClickListener { onItemClicked(team) }
        }
    }
}
