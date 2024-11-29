package com.kujawski.pocketscores.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.models.LeagueTeam

class TeamAdapter(private val onClick: (LeagueTeam) -> Unit) :
    ListAdapter<LeagueTeam, TeamAdapter.TeamViewHolder>(TeamDiffCallback()) {

    // ViewHolder for individual team items
    class TeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val teamLogo: ImageView = view.findViewById(R.id.team_logo)
        val teamName: TextView = view.findViewById(R.id.team_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = getItem(position)
        holder.teamName.text = team.displayName

        // Load the first logo URL using Glide
        val logoUrl = team.logos.firstOrNull()?.href.orEmpty()
        Glide.with(holder.itemView.context)
            .load(logoUrl)
            .into(holder.teamLogo)

        holder.itemView.setOnClickListener { onClick(team) }
    }
}

class TeamDiffCallback : DiffUtil.ItemCallback<LeagueTeam>() {
    override fun areItemsTheSame(oldItem: LeagueTeam, newItem: LeagueTeam): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LeagueTeam, newItem: LeagueTeam): Boolean {
        return oldItem == newItem
    }
}
