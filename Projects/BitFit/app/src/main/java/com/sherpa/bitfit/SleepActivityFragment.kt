package com.sherpa.bitfit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch



class SleepActivityFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var sleepAdapter: SleepEntryAdapter
    private val sleepEntries = mutableListOf<SleepEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sleep_activity, container, false)

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.article_recycler_view)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        sleepAdapter = SleepEntryAdapter(sleepEntries) { sleepEntry, position ->
            deleteSleepEntry(sleepEntry, position)
        }
        recyclerView.adapter = sleepAdapter


        return view
    }
    private fun deleteSleepEntry(sleepEntry: SleepEntry, position: Int) {
        lifecycleScope.launch {
            // Delete from the database
//            (application as SleepApplication).db.sleepDao().delete(sleepEntry.id)
            (activity?.application as SleepApplication).db.sleepDao().delete(sleepEntry.id)


            sleepEntries.removeAt(position)
            sleepAdapter.notifyItemRemoved(position)
         }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            try {
                (activity?.application as SleepApplication).db.sleepDao().getAll().collect { databaseList ->
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

//                        // Calculate and display average sleep time and happiness level
//                        val averageSleep = calculateAverageSleep(sleepEntries)
//                        val averageHappiness = calculateAverageHappiness(sleepEntries)
//
//                        averageSleepTextView.text = "Average Sleep Time: %.2f hours".format(averageSleep)
//                        averageHappinessTextView.text = "Average Happiness Level: %.1f/10".format(averageHappiness)
                    }
                }
            } catch (e: Exception) {
                Log.e("SleepActivity", "Error fetching data: ${e.message}", e)

            }
        }
    }
    companion object {
        fun newInstance(): SleepActivityFragment {
            return SleepActivityFragment()
        }


    }


}