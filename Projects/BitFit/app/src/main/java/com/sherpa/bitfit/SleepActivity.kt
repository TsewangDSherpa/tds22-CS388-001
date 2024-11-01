package com.sherpa.bitfit

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log


import android.widget.ImageButton
import android.widget.TextView


import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.coroutines.launch


class SleepActivity : AppCompatActivity() {

    private lateinit var sleepAdapter: SleepEntryAdapter
    private val sleepEntries = mutableListOf<SleepEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        sleepAdapter = SleepEntryAdapter(sleepEntries) { sleepEntry, position ->
            deleteSleepEntry(sleepEntry, position)
        }

        val orientation = resources.configuration.orientation
        var layoutId = R.layout.activity_sleep
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutId = R.layout.activity_sleep

        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutId = R.layout.activity_sleep_land
        }
        setContentView(layoutId)
        val averageSleepTextView = findViewById<TextView>(R.id.averageSleepTimeTextView)
        val averageHappinessTextView = findViewById<TextView>(R.id.averageHappinessLevelTextView)

        val recyclerView: RecyclerView = findViewById(R.id.sleepRecyclerView)
        recyclerView.adapter = sleepAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)



        lifecycleScope.launch {
            try {
                (application as SleepApplication).db.sleepDao().getAll().collect { databaseList ->
                    databaseList.map { entity ->
                        Log.d("SleepActivity", "Image URL: ${entity.imageUrl}")
                        SleepEntry(
                            entity.id,
                            entity.date,
                            entity.hoursSlept,
                            entity.feeling,
                            entity.notes,
                            entity.imageUrl
                        )

                    }.also { mappedList ->
                        sleepEntries.clear()
                        sleepEntries.addAll(mappedList)
                        sleepAdapter.notifyDataSetChanged()

                        // Calculate and display average sleep time and happiness level
                        val averageSleep = calculateAverageSleep(sleepEntries)
                        val averageHappiness = calculateAverageHappiness(sleepEntries)

                        averageSleepTextView.text = "Average Sleep Time: %.2f hours".format(averageSleep)
                        averageHappinessTextView.text = "Average Happiness Level: %.1f/10".format(averageHappiness)
                    }
                }
            } catch (e: Exception) {
                Log.e("SleepActivity", "Error fetching data: ${e.message}", e)

            }
        }


        // Button to add new sleep entry
        findViewById<ImageButton>(R.id.addSleepEntryButton).setOnClickListener {
            val intent = Intent(this, AddSleepEntryActivity::class.java)
            startActivity(intent)
        }




    }

    private fun calculateAverageSleep(entries: List<SleepEntry>): Double {
        return if (entries.isEmpty()) 0.0 else entries.map { it.hoursSlept }.average()
    }

    private fun calculateAverageHappiness(entries: List<SleepEntry>): Double {
        return if (entries.isEmpty()) 0.0 else entries.map { it.feeling.toDouble() }.average()
    }

    private fun deleteSleepEntry(sleepEntry: SleepEntry, position: Int) {
        lifecycleScope.launch {
            // Delete from the database
            (application as SleepApplication).db.sleepDao().delete(sleepEntry.id)

            // Remove from the list and update UI after deletion is confirmed
            sleepEntries.removeAt(position)
            sleepAdapter.notifyItemRemoved(position)

            // Show a toast message
//            Toast.makeText(this@SleepActivity, "Deleted entry: ${sleepEntry.id} at position: $position", Toast.LENGTH_SHORT).show()
        }
    }





}
