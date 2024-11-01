package com.sherpa.bitfit

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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

class AddSleepEntryActivity : AppCompatActivity() {

    private lateinit var hoursSleptSeekBar: SeekBar
    private lateinit var feelingSeekBar: SeekBar
    private lateinit var notesEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var hoursSleptTextView: TextView
    private lateinit var feelingTextView: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sleep_entry)

        hoursSleptSeekBar = findViewById(R.id.hoursSleptSeekBar)
        feelingSeekBar = findViewById(R.id.feelingSeekBar)
        notesEditText = findViewById(R.id.notesEditText)
        submitButton = findViewById(R.id.submitButton)
        hoursSleptTextView = findViewById(R.id.hoursSleptTextView)
        feelingTextView = findViewById(R.id.feelingTextView)


        hoursSleptSeekBar.max = 96 


        hoursSleptSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val hours = progress * 0.25 // 0.25 hours per increment (15 mins)
                hoursSleptTextView.text = "Hours Slept: %.2f".format(hours)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Set up listener for the feeling SeekBar
        feelingSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                feelingTextView.text = "Feeling: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Handle submit button click
        submitButton.setOnClickListener {
            lifecycleScope.launch {
                val imageUrl = fetchApiResponse()
                val hoursSlept = hoursSleptSeekBar.progress * 0.25
                val feeling = feelingSeekBar.progress
                val notes = notesEditText.text.toString()
                Log.v("AddSleepEntryActivity", "Hours Slept: $hoursSlept, Feeling: $feeling, Notes: $imageUrl")

                withContext(Dispatchers.IO) {
                    (application as SleepApplication).db.sleepDao().insert(
                        SleepEntity(
                            hoursSlept = hoursSlept.toFloat(),
                            feeling = feeling.toString(),
                            date = getCurrentDate(),
                            notes = notes,
                            imageUrl = imageUrl
                        )
                    )
                }
                finish()
            }
        }
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
                Log.v("AddSleepEntryActivity", "Fetched Image URL: $imageUrl")
                promise.complete(imageUrl)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                Log.v("AddSleepEntryActivity", "Failed to fetch API response", throwable)
                promise.complete("")
            }
        })

        return promise.await()
    }
}
