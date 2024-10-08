package com.sherpa.flixsterpro

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.BuildConfig
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray

class MoviesFragment : Fragment(), OnListFragmentInteractionListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
        val progressBar = view.findViewById<ContentLoadingProgressBar>(R.id.progress)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        val context = view.context


        val orientation = resources.configuration.orientation
        val spanCount = if (orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2

        val layoutManager = GridLayoutManager(context, spanCount)
        recyclerView.layoutManager = layoutManager

        updateAdapter(progressBar, recyclerView)

        return view
    }


    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        // Set up AsyncHttpClient
        val client = AsyncHttpClient()
        val headers = RequestHeaders()
        headers["x-rapidapi-key"] = com.sherpa.flixsterpro.BuildConfig.API_KEY

// Initialize a mutable list to hold jokes
        val jokesList = mutableListOf<String>()

// Track how many requests have completed
        var completedRequests = 0

// Make 15 HTTP requests for jokes
        for (i in 0 until 15) {
            client.get(
                "https://jokes-always.p.rapidapi.com/common", headers,
                null,
                object : JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Headers,
                        json: JsonHttpResponseHandler.JSON
                    ) {
                        progressBar.hide()

                        // Get the joke from the JSON response
                        val joke = json.jsonObject.get("data") as String

                        // Add the joke to the list
                        jokesList.add(joke)

                        // Increment completed requests
                        completedRequests++

                        // Check if all requests have completed
                        if (completedRequests == 15) {
                            // Print the jokes list
                            Log.v("jokes", jokesList.toString())
                        }
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        errorResponse: String?,
                        t: Throwable?
                    ) {
                        progressBar.hide()
                        t?.message?.let {
                            Log.e("API_ERROR", errorResponse ?: "Error response was null")
                        }
                        // Show a toast message to indicate failure
                        Toast.makeText(context, "Failed to load jokes", Toast.LENGTH_LONG).show()

                        // Increment completed requests, even in failure, to ensure the count reaches 15
                        completedRequests++

                        // Check if all requests have completed
                        if (completedRequests == 15) {
                            // Print the jokes list (in case of partial success)
                            Log.d("jokes", jokesList.toString())
                        }
                    }
                })
        }

        Log.d("JokesDone", jokesList.toString())
    }


        override fun onItemClick(item: Movie) {

        Toast.makeText(context, "Movie: ${item.title}", Toast.LENGTH_LONG).show()
    }
}
