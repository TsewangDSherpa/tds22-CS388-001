package com.sherpa.flixsterPro

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

private const val TAG = "DetailActivity"

class DetailActivity : AppCompatActivity() {
    private lateinit var backdropImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var releaseDateTextView: TextView
    private lateinit var overviewTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var mediaPosterView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // TODO: Find the views for the screen
        titleTextView = findViewById(R.id.actorName)
        backdropImageView = findViewById(R.id.actorImage)
        mediaPosterView = findViewById(R.id.moviePoster)
        releaseDateTextView = findViewById(R.id.mediaReleaseDate)
        overviewTextView = findViewById(R.id.actorPopularity)
        ratingTextView = findViewById(R.id.mediaRating)

        // TODO: Get the extra from the Intent
        val movie = intent.getSerializableExtra(MOVIE_EXTRA) as Movie
        // TODO: Set the title, byline, and abstract information from the article
        titleTextView.text = movie.originalTitle
        releaseDateTextView.text = "Release Date: " + movie.releaseDate
        overviewTextView.text = movie.overview
        ratingTextView.text = "Rating: " + movie.voteAverage.toString()

        // TODO: Load the media image
        Glide.with(this)
            .load(movie.backdropImageUrl)
            .placeholder(R.drawable.movieplaceholder)
            .centerInside()
            .into(backdropImageView)

        Glide.with(this)
            .load(movie.posterImageUrl)
            .placeholder(R.drawable.movieplaceholder)
            .centerCrop()
            .transform(RoundedCorners(80 ))
            .into(mediaPosterView)

    }
}