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
import com.github.mikephil.charting.components.XAxis
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {
    private lateinit var sleepTrendChart: LineChart
    private lateinit var avgSleepText: TextView
    private lateinit var avgHappinessText: TextView

    // Initialize sleepEntries as an empty list to avoid null issues
    private var sleepEntries: List<SleepEntry> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // Initialize the chart
        sleepTrendChart = view.findViewById(R.id.sleepTrendChart)

        // Fetch data and update UI
        lifecycleScope.launch {
            fetchSleepData()
        }

        return view
    }

    // Move view initialization to onViewCreated to avoid issues with lazy initialization
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        avgSleepText = view.findViewById(R.id.averageSleepTimeTextView)
        avgHappinessText = view.findViewById(R.id.averageHappinessLevelTextView)
    }

    private suspend fun fetchSleepData() {
        (activity?.application as SleepApplication).db.sleepDao().getAll().collect { databaseList ->
            // Map the database list to SleepEntry objects
            val sleepEntriesC = databaseList.map { entity ->
                SleepEntry(
                    id = entity.id,
                    date = entity.date,
                    hoursSlept = entity.hoursSlept,
                    feeling = entity.feeling,
                    notes = entity.notes,
                    imageUrl = entity.imageUrl
                )
            }
            // Update sleepEntries and refresh the UI
            sleepEntries = sleepEntriesC
            displaySleepTrend(sleepEntries)
            updateAverageText(sleepEntries)
        }
    }

    private fun updateAverageText(entries: List<SleepEntry>) {
        val avgSleep = returnAverageSleep(entries)
        val avgHappiness = returnAverageHappiness(entries)

        avgSleepText.text = "Average Sleep Time: $avgSleep hours"
        avgHappinessText.text = "Average Happiness Level: $avgHappiness/10"
    }

    override fun onResume() {
        super.onResume()

        if (sleepEntries.isNotEmpty()) {
            updateAverageText(sleepEntries)
        }
    }

    private fun displaySleepTrend(entries: List<SleepEntry>) {
        if (entries.isEmpty()) {
            sleepTrendChart.visibility = View.GONE
            return
        }

        val entriesForChart = entries.mapIndexed { index, entry ->
            Entry(index.toFloat() + 1, entry.hoursSlept.toFloat()) // X-axis step of 1
        }

        val dataSet = LineDataSet(entriesForChart, "Sleep Trend")
        dataSet.color = resources.getColor(R.color.teal, null)
        dataSet.valueTextColor = resources.getColor(R.color.black, null)
        dataSet.lineWidth = 2f

        // Configure the chart
        sleepTrendChart.description.text = "Track your sleep per day!"
        sleepTrendChart.axisRight.isEnabled = false

        // Configure the X-axis
        val xAxis = sleepTrendChart.xAxis
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        // Add the data and refresh the chart
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

        return String.format("%.2f", totalHours / sleepEntries.size).toFloat()
    }

    fun returnAverageHappiness(sleepEntries: List<SleepEntry>): Float {
        if (sleepEntries.isEmpty()) return 0f

        var totalHappiness = 0f
        for (entry in sleepEntries) {
            totalHappiness += entry.feeling.toFloat()
        }

        return String.format("%.2f", totalHappiness / sleepEntries.size).toFloat()
    }

}
