package com.sherpa.jokepro

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.sherpa.jokepro.R
import okhttp3.Headers


class MoviesFragment : Fragment(), OnListFragmentInteractionListener {
    private val jokesList = mutableListOf<Movie>()
    private val totalRequests = 15 // Easily modifiable request count

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
        val progressBar = view.findViewById<ContentLoadingProgressBar>(R.id.progress)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        val context = view.context

        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2
        recyclerView.layoutManager = GridLayoutManager(context, spanCount)

        updateAdapter(progressBar, recyclerView)

        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()
        val client = AsyncHttpClient()
        val headers = RequestHeaders().apply {
            this["x-rapidapi-key"] = BuildConfig.API_KEY
        }

        var completedRequests = 0

        // Make HTTP requests for jokes
        repeat(totalRequests) {
            client.get("https://jokes-always.p.rapidapi.com/common", headers, null,
                object : JsonHttpResponseHandler() {
                    override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                        progressBar.hide()
                        val jokeText = json.jsonObject.get("data") as String
                        jokesList.add(Movie.createJoke(jokeText)) // Assume Joke is a data class

                        completedRequests++
                        if (completedRequests == totalRequests) updateRecyclerView(recyclerView)
                    }

                    override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String?, t: Throwable?) {
                        progressBar.hide()
                        Log.e("API_ERROR", errorResponse ?: "Error response was null")
                        Toast.makeText(context, "Failed to load jokes", Toast.LENGTH_LONG).show()

                        completedRequests++
                        if (completedRequests == totalRequests) updateRecyclerView(recyclerView)
                    }
                }
            )
        }
    }

    private fun updateRecyclerView(recyclerView: RecyclerView) {
        if (jokesList.isNotEmpty()) {
            recyclerView.adapter = MoviesRecyclerViewAdapter(jokesList, this)
        } else {
            Toast.makeText(context, "No jokes available", Toast.LENGTH_LONG).show()
            Log.e("MoviesFragment", "Jokes list is empty after all requests.")
        }
    }

    override fun onItemClick(item: Movie) {
        val intent = Intent(context, DetailJoke::class.java).apply {

        putExtra("ExtraJoke", Joke(item.jokeQuestion, item.jokeAnswer, item.jokeLiked) ) // Assuming Joke implements Serializable
        }
        try {
            context?.startActivity(intent)

        } catch (e: Exception) {
            Log.e("DetailActivity", "Error starting DetailActivity", e)
        }
    }


}
