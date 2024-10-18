package com.sherpa.thursdaynighttrivia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameResultActivity : AppCompatActivity() {
    private var correctAnswersCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)

        // Retrieve the correct answers count from the intent
        correctAnswersCount = intent.getIntExtra("correctAnswersCount", 0)

        // Display the result message
        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        resultTextView.text = "You got $correctAnswersCount correct answers!"

        // Display a message based on the score
        val messageTextView = findViewById<TextView>(R.id.messageTextView)
        messageTextView.text = getMessageBasedOnScore(correctAnswersCount)

        // Set up the back to home button
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            navigateToHome()
        }
    }

    private fun getMessageBasedOnScore(score: Int): String {
        return when (score) {
            in 4..5 -> "Great Job!"
            in 2..3 -> "Good effort! Keep practicing."
            else -> "Better luck next time!"
        }
    }

    private fun navigateToHome() {
        finish()
    }
}
