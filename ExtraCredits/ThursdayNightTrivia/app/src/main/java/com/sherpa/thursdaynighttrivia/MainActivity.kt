package com.sherpa.thursdaynighttrivia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tryButton = findViewById<ImageButton>(R.id.imageButton)
        val triviaObj = Trivias()
        // Load trivia from the JSON file
        triviaObj.loadTriviaFromJson(this)

        tryButton.setOnClickListener {
            triviaObj.getUniqueTrivia()
            val selectedTrivia: List<Trivia> = triviaObj.getUniqueTrivia()

            // Start the first TriviaQuestionActivity and pass the trivia list
            val intent = Intent(this, TriviaDetailActivity::class.java)
            intent.putStringArrayListExtra("ListQuestions", ArrayList(selectedTrivia.map { it.question }))
            intent.putStringArrayListExtra("ListAnswers", ArrayList(selectedTrivia.map { it.answers.toList().toString() }))
            intent.putStringArrayListExtra("ListCorrect", ArrayList(selectedTrivia.map { it.correctAnswer }))

            startActivity(intent)

        }


    }
}
