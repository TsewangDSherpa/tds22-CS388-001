package com.sherpa.flixsterPro

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sherpa.flixsterPro.databinding.ActivityMainBinding
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private const val TAG = "MainActivity/"
private const val SEARCH_API_KEY = BuildConfig.API_KEY
private const val UPCOMING_MOVIES_SEARCH_URL =
    "https://api.themoviedb.org/3/movie/upcoming?&api_key=$SEARCH_API_KEY"
private const val ACTOR_SEARCH_URL =
    "https://api.themoviedb.org/3/person/popular?&api_key=$SEARCH_API_KEY"


class MainActivity : AppCompatActivity() {
    private lateinit var moviesRecyclerView: RecyclerView
    private lateinit var actorRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private val movies = mutableListOf<Movie>()
    private val actors = mutableListOf<Actor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        moviesRecyclerView = findViewById(R.id.movies)
        actorRecyclerView = findViewById(R.id.actors)

        val actorAdapter = ActorAdapter(this, actors)
        val movieAdapter = MovieAdapter(this, movies)
        moviesRecyclerView.adapter = movieAdapter
        actorRecyclerView.adapter = actorAdapter

        moviesRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            moviesRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        actorRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)



        val client = AsyncHttpClient()
        client.get(UPCOMING_MOVIES_SEARCH_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.v(TAG, "Failed to fetch movies: $statusCode")
//                Toast.makeText(this@MainActivity, "Failed to fetch movies", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.v(TAG, "Successfully fetched movies: $json")
//                Toast.makeText(this@MainActivity, "Successfully fetched movies", Toast.LENGTH_SHORT).show()
                try {
                    // TODO: Create the parsedJSON

                    val parsedJson = createJson().decodeFromString(
                        Results.serializer(),
                        json.jsonObject.toString()
                    )

                    // TODO: Do something with the returned json (contains article information)
                    parsedJson.Movies?.let { list ->
                        movies.addAll(list)
                        movieAdapter.notifyDataSetChanged()
                    }

                } catch (e: JSONException) {
                    Log.v(TAG, "Exception: $e")
                }
            }

        })
        client.get(ACTOR_SEARCH_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.v(TAG, "Failed to fetch actors: $statusCode")
//                Toast.makeText(this@MainActivity, "Failed to fetch actors", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.v(TAG, "Successfully fetched actors: $json")
//                Toast.makeText(this@MainActivity, "Successfully fetched actors", Toast.LENGTH_SHORT).show()
                try {
                    // TODO: Create the parsedJSON

                    val parsedJson = createJson().decodeFromString(
                        ActorResults.serializer(),
                        json.jsonObject.toString()
                    )


                    // TODO: Do something with the returned json (contains article information)
                    parsedJson.Actors?.let { list ->
                        actors.addAll(list)
                        actorAdapter.notifyDataSetChanged()
                    }

                } catch (e: JSONException) {
                    Log.v(TAG, "Exception: $e")
                }
            }

        })
    }
}