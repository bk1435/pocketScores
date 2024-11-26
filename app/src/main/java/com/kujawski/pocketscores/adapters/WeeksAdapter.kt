package com.kujawski.pocketscores.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kujawski.pocketscores.R
import com.kujawski.pocketscores.models.Week

class WeeksAdapter(
    private val weeks: List<Week>,
    private val onClick: (Week) -> Unit
) : RecyclerView.Adapter<WeeksAdapter.WeekViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_week, parent, false)
        return WeekViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        val week = weeks[position]
        Log.d("WeeksAdapter", "Binding week at position $position: ${week.weekNumber}")
        holder.bind(week, onClick)
    }

    override fun getItemCount(): Int {
        Log.d("WeeksAdapter", "Total items: ${weeks.size}")
        return weeks.size
    }

    class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val weekNumberTextView: TextView = itemView.findViewById(R.id.week_number_text_view)

        fun bind(week: Week, onClick: (Week) -> Unit) {
            weekNumberTextView.text = when (week.weekNumber) {
                -4 -> "Wild Card Round"
                -3 -> "Divisional Round"
                -2 -> "Conference Round"
                -1 -> "Super Bowl"
                else -> "Week ${week.weekNumber}"
            }

            Log.d("WeeksAdapter", "Label for this week: ${weekNumberTextView.text}")

            itemView.setOnClickListener {
                Log.d("WeeksAdapter", "Clicked on: ${weekNumberTextView.text}")
                onClick(week)
            }
        }
    }
}
