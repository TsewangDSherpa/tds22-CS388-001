package com.sherpa.flixsterPro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

const val MOVIE_EXTRA = "MOVIE_EXTRA"
private const val TAG = "MovieAdapter"

class MovieAdapter(private val context: Context, private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val mediaImageView = itemView.findViewById<ImageView>(R.id.actorImage)
        private val movieTitleTextView = itemView.findViewById<TextView>(R.id.actorName)
        private val moviePopularityTextView = itemView.findViewById<TextView>(R.id.actorPopularity)

        init {
            itemView.setOnClickListener(this)
        }


        fun bind(movie: Movie) {
            movieTitleTextView.text = movie.originalTitle
            moviePopularityTextView.text = "Popularity: " + movie.popularity.toString()

            Glide.with(context)
                .load(movie.posterImageUrl)
                .placeholder(R.drawable.movieplaceholder)
                .centerCrop()
                .transform(RoundedCorners(80 ))
                .into(mediaImageView)
        }
        override fun onClick(v: View?) {
            val movie = movies[absoluteAdapterPosition]
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(MOVIE_EXTRA, movie)


            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                mediaImageView, // The shared element view
                "sharedImage" // The transition name
            )

            context.startActivity(intent, options.toBundle())
        }


    }
}