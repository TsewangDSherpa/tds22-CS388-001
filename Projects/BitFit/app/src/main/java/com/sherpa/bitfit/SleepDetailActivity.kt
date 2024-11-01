package com.sherpa.bitfit

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class SleepDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_sleep_activity)

        // Get references to the views
        val sleepImageView: ImageView = findViewById(R.id.sleepImageView)
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val hoursSleptTextView: TextView = findViewById(R.id.hoursSleptTextView)
        val feelingTextView: TextView = findViewById(R.id.feelingTextView)
        val notesTextView: TextView = findViewById(R.id.notesTextView)

        // Retrieve data from Intent
        val hoursSlept = intent.getFloatExtra("HOURS_SLEPT", 0f)
        val feeling = intent.getStringExtra("FEELING") ?: "No feeling recorded"
        val notes = intent.getStringExtra("NOTES") ?: "No notes available"
        val date = intent.getStringExtra("DATE") ?: "Unknown date"
        val imageUrl = intent.getStringExtra("IMAGE_URL") ?: ""

        // Set the text for each TextView
        dateTextView.text = "Date: $date"
        hoursSleptTextView.text = "Hours Slept: $hoursSlept"
        feelingTextView.text = "Feeling: $feeling"
        notesTextView.text = "Notes: $notes"

        // Load the image URL into the ImageView using Glide
        Glide.with(this)
            .load(imageUrl)
            .into(sleepImageView)
    }
}
