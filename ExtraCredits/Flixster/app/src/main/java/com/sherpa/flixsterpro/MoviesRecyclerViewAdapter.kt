package com.sherpa.flixsterpro

import com.sherpa.flixsterpro.R.id
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


/**
 * [RecyclerView.Adapter] that can display a [Movie] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MoviesRecyclerViewAdapter(
    private val movies: List<Movie>,
    private val mListener: OnListFragmentInteractionListener?
    )
    : RecyclerView.Adapter<MoviesRecyclerViewAdapter.MovieViewHolder>()
    {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movie, parent, false)
        return MovieViewHolder(view)
    }

    /**
     * This inner class lets us refer to all the different View elements
     * (Yes, the same ones as in the XML layout files!)
     */
    inner class MovieViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: Movie? = null
        val mMovieTitle: TextView = mView.findViewById<View>(id.movie_title) as TextView
        val mMoviePoster_path: ImageView = mView.findViewById<View>(id.movie_image) as ImageView
        val mMovie_overview: TextView = mView.findViewById<View>(id.movie_overview) as TextView
        val mMovie_vote_average: TextView = mView.findViewById<View>(id.movie_rating) as TextView
        val mMovie_release_date: TextView = mView.findViewById<View>(id.movie_release_date) as TextView

        fun bind(movie: Movie) {

            Glide.with(mMoviePoster_path.context)
                .load("https://image.tmdb.org/t/p/w500/${movie.poster_path}")
                .placeholder(R.drawable.movieplaceholder)
                .into(mMoviePoster_path)

        }

        override fun toString(): String {
            return mMovieTitle.toString() + " '" + mMovieTitle.text + "'"
        }
    }

    /**
     * This lets us "bind" each Views in the ViewHolder to its' actual data!
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.mItem = movie
        holder.mMovieTitle.text = movie.title
        holder.mMovie_overview.text = movie.overview

        val ratingText = buildString {
            append("Rating: ")
            append(String.format("%.2f", movie.vote_average))
            append("/10")
        }

        val ratingSpannable = SpannableStringBuilder(ratingText).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        holder.mMovie_vote_average.text = ratingSpannable

        val releaseDateText = buildString {
            append("Release Date: ")
            append(movie.release_date)
        }

        val releaseDateSpannable = SpannableStringBuilder(releaseDateText).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        holder.mMovie_release_date.text = releaseDateSpannable



        holder.mView.setOnClickListener {
            holder.mItem?.let { book ->
                mListener?.onItemClick(book)
            }
        }



    }

    /**
     * Remember: RecyclerView adapters require a getItemCount() method.
     */
    override fun getItemCount(): Int {
        return movies.size
    }
}