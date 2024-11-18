package com.sherpa.bitfit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.charts.LineChart
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private lateinit var sleepTrendChart: LineChart
    private val avgSleepText: TextView by lazy {
        requireView().findViewById(R.id.averageSleepTimeTextView)
    }
    private val avgHappinessText: TextView by lazy {
        requireView().findViewById(R.id.averageHappinessLevelTextView)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        sleepTrendChart = view.findViewById(R.id.sleepTrendChart)

        // Fetch data and update UI
        lifecycleScope.launch {
            fetchSleepData()
        }

        return view
    }

    private suspend fun fetchSleepData() {
        (activity?.application as SleepApplication).db.sleepDao().getAll().collect { databaseList ->
            val sleepEntries = databaseList.map { entity ->
                SleepEntry(
                    id = entity.id,
                    date = entity.date,
                    hoursSlept = entity.hoursSlept,
                    feeling = entity.feeling,
                    notes = entity.notes,
                    imageUrl = entity.imageUrl
                )
            }
            displaySleepTrend(sleepEntries)
        }
    }

    private fun displaySleepTrend(entries: List<SleepEntry>) {
        val entriesForChart = entries.mapIndexed { index, entry ->
            Entry(index.toFloat(), entry.hoursSlept.toFloat())
        }

        val avgSleep = returnAverageSleep(entries)
        val avgHappiness = returnAverageHappiness(entries)

        avgSleepText.text = "Average Sleep Time: $avgSleep hours"
        avgHappinessText.text = "Average Happiness Level: $avgHappiness/10"

        val dataSet = LineDataSet(entriesForChart, "Sleep Trend")
        dataSet.color = R.color.teal
        dataSet.valueTextColor = R.color.black
        dataSet.lineWidth = 2f
        sleepTrendChart.description.text = "Track your sleeping trend"
        sleepTrendChart.axisRight.isEnabled = false
        val lineData = LineData(dataSet)
        sleepTrendChart.data = lineData
        sleepTrendChart.invalidate()
    }


    fun returnAverageSleep(sleepEntries: List<SleepEntry>): Float {
        if (sleepEntries.isEmpty()) return 0f

        var totalHours = 0f
        for (entry in sleepEntries) {
            totalHours += entry.hoursSlept
        }
        return totalHours / sleepEntries.size
    }

    fun returnAverageHappiness(sleepEntries: List<SleepEntry>): Float {
        if (sleepEntries.isEmpty()) return 0f

        var totalHappiness = 0f
        for (entry in sleepEntries) {
            totalHappiness += entry.feeling.toFloat()
        }
        return totalHappiness / sleepEntries.size
    }
}
