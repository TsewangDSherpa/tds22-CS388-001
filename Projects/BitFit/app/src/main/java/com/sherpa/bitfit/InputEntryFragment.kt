
package com.sherpa.bitfit

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.CompletableDeferred

class InputEntryFragment : Fragment() {

    private lateinit var hoursSleptSeekBar: SeekBar
    private lateinit var feelingSeekBar: SeekBar
    private lateinit var notesEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var hoursSleptTextView: TextView
    private lateinit var feelingTextView: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_input_entry, container, false)

        // Initialize views
        hoursSleptSeekBar = view.findViewById(R.id.hoursSleptSeekBar)
        feelingSeekBar = view.findViewById(R.id.feelingSeekBar)
        notesEditText = view.findViewById(R.id.notesEditText)
        submitButton = view.findViewById(R.id.submitButton)
        hoursSleptTextView = view.findViewById(R.id.hoursSleptTextView)
        feelingTextView = view.findViewById(R.id.feelingTextView)

        // Set SeekBar max values and listeners
        hoursSleptSeekBar.max = 96

        hoursSleptSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val hours = progress * 0.25 // 0.25 hours per increment (15 mins)
                hoursSleptTextView.text = "Hours Slept: %.2f".format(hours)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        feelingSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                feelingTextView.text = "Feeling: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Handle submit button click
        submitButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val imageUrl = fetchApiResponse()
                val hoursSlept = hoursSleptSeekBar.progress * 0.25
                val feeling = feelingSeekBar.progress
                val notes = notesEditText.text.toString()
                Log.v("InputEntryFragment", "Hours Slept: $hoursSlept, Feeling: $feeling, Notes: $imageUrl")

                withContext(Dispatchers.IO) {
                    // Assuming you have access to the app's database and the sleep entity model
                    (requireActivity().application as SleepApplication).db.sleepDao().insert(
                        SleepEntity(
                            hoursSlept = hoursSlept.toFloat(),
                            feeling = feeling.toString(),
                            date = getCurrentDate(),
                            notes = notes,
                            imageUrl = imageUrl
                        )
                    )
                }
                hoursSleptSeekBar.progress = 0
                feelingSeekBar.progress = 0
                notesEditText.text.clear()

                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.article_frame_layout, SleepActivityFragment.newInstance())
                requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.nav_home
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.now().format(formatter)
    }

    private suspend fun fetchApiResponse(): String {
        val client = AsyncHttpClient()
        val promise = CompletableDeferred<String>()

        client.get("https://yesno.wtf/api", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject) {
                val imageUrl = response.getString("image")
                Log.v("InputEntryFragment", "Fetched Image URL: $imageUrl")
                promise.complete(imageUrl)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                Log.v("InputEntryFragment", "Failed to fetch API response", throwable)
                promise.complete("")
            }
        })

        return promise.await()
    }

    companion object {
        fun newInstance(): InputEntryFragment {
            return InputEntryFragment()
        }
    }
}
