package com.sherpa.bitfit

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SleepEntryAdapter(
    private var sleepEntries: List<SleepEntry>,
    private val onLongClick: (SleepEntry, Int) -> Unit // Now passing position as well
) : RecyclerView.Adapter<SleepEntryAdapter.SleepEntryViewHolder>() {

    class SleepEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hoursSleptTextView: TextView = itemView.findViewById(R.id.hoursSleptTextView)
        val feelingTextView: TextView = itemView.findViewById(R.id.feelingTextView)
        val notesTextView: TextView = itemView.findViewById(R.id.notesTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepEntryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutId = if (parent.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            R.layout.item_sleep_entry_land
        } else {
            R.layout.item_sleep_entry
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return SleepEntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SleepEntryViewHolder, position: Int) {
        val entry = sleepEntries[position]
        holder.hoursSleptTextView.text = "Hours Slept: ${entry.hoursSlept}"
        holder.feelingTextView.text = "Feeling: ${entry.feeling}/10"
        holder.notesTextView.text = "Notes: ${entry.notes}"
        holder.dateTextView.text = "Date: ${entry.date}"

        // Set up long press listener
        holder.itemView.setOnLongClickListener {
            onLongClick(entry, position) // Pass entry and position
            true // Indicate that the event has been handled
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, SleepDetailActivity::class.java).apply {
                putExtra("HOURS_SLEPT", entry.hoursSlept)
                putExtra("FEELING", entry.feeling)
                putExtra("NOTES", entry.notes)
                putExtra("DATE", entry.date)
                putExtra("IMAGE_URL", entry.imageUrl)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = sleepEntries.size

    fun updateData(newEntries: List<SleepEntry>) {
        sleepEntries = newEntries
        notifyDataSetChanged()
    }

    fun returnAverageSleep(): Float {
        if (sleepEntries.isEmpty()) return 0f

        var totalHours = 0f
        for (entry in sleepEntries) {
            totalHours += entry.hoursSlept
        }
        return totalHours / sleepEntries.size
    }

    fun returnAverageHappiness(): Float {
        if (sleepEntries.isEmpty()) return 0f

        var totalHappiness = 0f
        for (entry in sleepEntries) {
            totalHappiness += entry.feeling.toFloat()
        }
        return totalHappiness / sleepEntries.size
    }
}
