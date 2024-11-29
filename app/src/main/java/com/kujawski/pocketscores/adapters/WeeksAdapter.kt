package com.kujawski.pocketscores.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.models.Week
import com.kujawski.pocketscores.ui.fragments.AroundLeagueFragmentDirections

class WeeksAdapter(
    private val weeks: List<Week>
) : RecyclerView.Adapter<WeeksAdapter.WeekViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_week, parent, false)
        return WeekViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        val week = weeks[position]
        holder.bind(week)
    }

    override fun getItemCount(): Int = weeks.size

    inner class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val weekTextView: TextView = itemView.findViewById(R.id.textViewWeek)

        fun bind(week: Week) {
            Log.d("WeeksAdapter", "Binding Week: ${week.weekNumber}, Label: ${week.label}")
            weekTextView.text = week.label
            itemView.setOnClickListener {
                val action = AroundLeagueFragmentDirections
                    .actionAroundLeagueFragmentToWeekDetailsFragment(week.weekNumber)
                it.findNavController().navigate(action)
            }
        }

    }
}
