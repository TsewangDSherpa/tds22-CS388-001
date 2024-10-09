package com.sherpa.jokepro

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class DetailJoke : AppCompatActivity() {
    private lateinit var joke: Joke

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.joke_detail)

        val jokeQuestion = findViewById<TextView>(R.id.joke_question)
        val jokeAnswer = findViewById<TextView>(R.id.joke_answer)
        val shareButton = findViewById<Button>(R.id.share_button)
        val instagram = findViewById<TextView>(R.id.instagram)
        val twitter = findViewById<TextView>(R.id.twitter)
        val facebook = findViewById<TextView>(R.id.facebook)

        joke = intent.getSerializableExtra("ExtraJoke") as Joke

        jokeQuestion.text = joke.jokeQuestion
        jokeAnswer.text = joke.jokeAnswer

        // Set click listeners for sharing
        shareButton.setOnClickListener {
            shareJokeView()
        }

        instagram.setOnClickListener {
            shareJokeText("${joke.jokeQuestion}\n${joke.jokeAnswer} #Instagram")
        }

        twitter.setOnClickListener {
            shareJokeText("${joke.jokeQuestion}\n${joke.jokeAnswer} #Twitter")
        }

        facebook.setOnClickListener {
            shareJokeText("${joke.jokeQuestion}\n${joke.jokeAnswer} #Facebook")
        }
    }

    private fun shareJokeText(message: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }

        // Check if there are apps that can handle this intent
        val packageManager: PackageManager = packageManager
        val resolvedActivities = packageManager.queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY)

        if (resolvedActivities.isNotEmpty()) {
            startActivity(Intent.createChooser(shareIntent, "Share joke via"))
        } else {
            Toast.makeText(this, "No apps available to share the joke", Toast.LENGTH_SHORT).show()
        }
    }

    private fun captureView(view: LinearLayout): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, view.width, view.height)
        view.draw(canvas)
        return bitmap
    }

    private fun saveBitmapToFile(bitmap: Bitmap): Uri {
        val file = File(cacheDir, "shared_joke_image.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        return FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)
    }

    private fun shareJokeView() {
        // Capture the layout you want to share
        val jokeView = findViewById<LinearLayout>(R.id.joke_container)
        val bitmap = captureView(jokeView)

        // Save bitmap and get URI
        val imageUri = saveBitmapToFile(bitmap)

        // Share the image
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            putExtra(Intent.EXTRA_TEXT, "Sharing this funny Joke! #SharedFromApp") // Combine question and answer
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Share joke via"))
    }
}
