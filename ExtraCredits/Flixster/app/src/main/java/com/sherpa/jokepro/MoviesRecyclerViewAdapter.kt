package com.sherpa.jokepro

import com.sherpa.jokepro.R.id
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sherpa.jokepro.R


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
        val mMovieQuestion: TextView = mView.findViewById<View>(id.joke_question) as TextView
//        val mMovieAnswer: TextView = mView.findViewById<View>(id.joke_answer) as TextView
        val mMovieLiked: TextView = mView.findViewById<View>(id.joke_liked) as TextView


//        fun bind(movie: Movie) {
//
//            Glide.with(mMoviePoster_path.context)
//                .load("https://image.tmdb.org/t/p/w500/${movie.poster_path}")
//                .placeholder(R.drawable.movieplaceholder)
//                .into(mMoviePoster_path)
//
//        }

        override fun toString(): String {
            return mMovieQuestion.toString() + " '" + mMovieQuestion.text + "'"
        }



    }

    /**
     * This lets us "bind" each Views in the ViewHolder to its' actual data!
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        val movie = movies[position]

        holder.mItem = movie
        holder.mMovieQuestion.text = movie.jokeQuestion
//        holder.mMovieAnswer.text = movie.jokeAnswer

        val ratingText =  if (movie.jokeLiked) "Liked" else "Un-Liked"


        holder.mMovieLiked.text = ratingText


        holder.mView.setOnClickListener {
            holder.mItem?.let { book ->
                mListener?.onItemClick(book)
                notifyItemChanged(position)
            }
        }

        holder.mMovieLiked.setOnClickListener {
            movie.toggleLike()
            holder.mMovieLiked.text = if (movie.jokeLiked) "Liked" else "Un-Liked"
        }




    }

    /**
     * Remember: RecyclerView adapters require a getItemCount() method.
     */
    override fun getItemCount(): Int {
        return movies.size
    }
}