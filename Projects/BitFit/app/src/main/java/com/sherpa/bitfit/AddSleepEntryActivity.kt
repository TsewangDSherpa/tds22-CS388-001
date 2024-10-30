package com.sherpa.bitfit

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

        // Set max value for the hours slept SeekBar
        hoursSleptSeekBar.max = 96 // 24 hours in 15-minute increments

        // Set up listener for the hours slept SeekBar
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
            val hoursSlept = hoursSleptSeekBar.progress * 0.25
            val feeling = feelingSeekBar.progress
            val notes = notesEditText.text.toString()
            println("Hours Slept: $hoursSlept, Feeling: $feeling, Notes: $notes")
            lifecycleScope.launch(IO) {
                (application as SleepApplication).db.sleepDao().insert(
                    SleepEntity(
                        hoursSlept = hoursSlept.toFloat(),
                        feeling = feeling.toString(),
                        date = getCurrentDate(),
                        notes = notes
                    )
                )
            }

            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy") // Change pattern as needed
        return LocalDate.now().format(formatter)
    }
}
