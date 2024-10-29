package com.codepath.articlesearch

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.articlesearch.databinding.ActivityMainBinding
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
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
private const val ARTICLE_SEARCH_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=${SEARCH_API_KEY}"



class MainActivity : AppCompatActivity() {
    private val articles = mutableListOf<DisplayArticle>()
    private val allArticles = mutableListOf<DisplayArticle>()
    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swipeRefreshLayout = binding.swipeRefreshLayout
        articlesRecyclerView = binding.articles

        networkChangeReceiver = NetworkChangeReceiver { isConnected ->
            if (isConnected) {
                binding.offlineMessage.visibility = View.GONE
                fetchArticlesFromApi()
            } else {
                binding.offlineMessage.visibility = View.VISIBLE
            }
        }

        // Set up ArticleAdapter with articles
        articleAdapter = ArticleAdapter(this, articles) { isCacheEnabled ->
            saveCachePreference(isCacheEnabled)
        }
        articlesRecyclerView.adapter = articleAdapter

        // Set up SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchArticlesFromApi(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterArticles(newText)
                return true
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            fetchArticlesFromApi()
        }

        articlesRecyclerView.layoutManager = LinearLayoutManager(this).apply {
            val dividerItemDecoration = DividerItemDecoration(this@MainActivity, orientation)
            articlesRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        fetchArticlesFromApi()
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(networkChangeReceiver)
    }

    private fun loadFromDatabase() {
        lifecycleScope.launch {
            (application as ArticleApplication).db.articleDao().getAll().collect { databaseList ->
                val mappedList = databaseList.map { entity ->
                    DisplayArticle(
                        entity.headline,
                        entity.articleAbstract,
                        entity.byline,
                        entity.mediaImageUrl
                    )
                }
                articles.clear()
                articles.addAll(mappedList)
                allArticles.clear() // Keep all articles for filtering
                allArticles.addAll(mappedList)
                articleAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun fetchArticlesFromApi(query: String? = null) {
        swipeRefreshLayout.isRefreshing = true
        val client = AsyncHttpClient()
        val apiUrl = ARTICLE_SEARCH_URL

        client.get(apiUrl, object : JsonHttpResponseHandler() {
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e(TAG, "Failed to fetch articles: $statusCode")
                loadFromDatabase()
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched articles: $json")
                try {
                    val parsedJson = createJson().decodeFromString(
                        SearchNewsResponse.serializer(),
                        json.jsonObject.toString()
                    )

                    parsedJson.response?.docs?.let { list ->
                        allArticles.clear() // Clear previous articles
                        allArticles.addAll(list.map {
                            DisplayArticle(
                                headline = it.headline?.main,
                                abstract = it.abstract,
                                byline = it.byline?.original,
                                mediaImageUrl = it.mediaImageUrl
                            )
                        })


                        articleAdapter.notifyDataSetChanged()


                        // Cache if enabled
                        if (getCachePreference()) {
                            lifecycleScope.launch(IO) {
                                (application as ArticleApplication).db.articleDao().deleteAll()
                                (application as ArticleApplication).db.articleDao().insertAll(allArticles.map {
                                    ArticleEntity(
                                        headline = it.headline,
                                        articleAbstract = it.abstract,
                                        byline = it.byline,
                                        mediaImageUrl = it.mediaImageUrl
                                    )
                                })
                            }
                        }
                        else {
                            // Clear the database
                            lifecycleScope.launch(IO) {
                                (application as ArticleApplication).db.articleDao().deleteAll()
                            }
                        }
                        articles.clear()
                        articles.addAll(allArticles)

                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                } finally {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    private fun filterArticles(query: String?) {
        if (query.isNullOrEmpty()) {
            articles.clear()
            articles.addAll(allArticles)
        } else {
            val filteredList = allArticles.filter { article ->
                article.headline?.contains(query, ignoreCase = true) == true ||
                        article.abstract?.contains(query, ignoreCase = true) == true
            }

            articles.clear()
            articles.addAll(filteredList)
        }
        articleAdapter.notifyDataSetChanged()
    }

    private fun getCachePreference(): Boolean {
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("cache_articles", true)
    }

    private fun saveCachePreference(isCachingEnabled: Boolean) {
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("cache_articles", isCachingEnabled).apply()
    }
}
