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
        val params = RequestParams()
        params["api_key"] = BuildConfig.API_KEY

        // Make the HTTP request
        client["https://api.themoviedb.org/3/movie/now_playing", params,
            object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                    progressBar.hide()

                    val resultsJSON: JSONArray = json.jsonObject.get("results") as JSONArray
                    val moviesRawJSON: String = resultsJSON.toString()
                    val arrayMovieType = object : TypeToken<List<Movie>>() {}.type

                    val models: List<Movie> = Gson().fromJson(moviesRawJSON, arrayMovieType)
                    recyclerView.adapter = MoviesRecyclerViewAdapter(models, this@MoviesFragment)
                    Log.d("MoviesFragment", "Response successful")
                }

                override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, t: Throwable?) {
                    progressBar.hide()
                    t?.message?.let {
                        Log.e("MoviesFragment", errorResponse)
                    }
                    // Handle error, maybe show a Snackbar or Toast
                    Toast.makeText(context, "Failed to load movies", Toast.LENGTH_LONG).show()
                }
            }
        ]
    }

    override fun onItemClick(item: Movie) {
        Toast.makeText(context, "Movie: ${item.title}", Toast.LENGTH_LONG).show()
    }
}
