package com.sherpa.jokepro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.sherpa.jokepro.R

class DetailJoke : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.joke_detail)

        val jokeQuestion = findViewById<TextView>(R.id.joke_question)
        val jokeAnswer = findViewById<TextView>(R.id.joke_answer)
        val jokeLiked = findViewById<TextView>(R.id.joke_like_button)

        val joke = intent.getSerializableExtra("ExtraJoke") as Joke

        jokeQuestion.text = joke.jokeQuestion
        jokeAnswer.text = joke.jokeAnswer
        jokeLiked.text = if (joke.jokeLiked) "Liked" else "Not Liked"

    }
}