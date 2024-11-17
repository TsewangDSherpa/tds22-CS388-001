package com.codepath.articlesearch


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException
import kotlinx.serialization.json.Json


private const val TAG = "PopularArticleListFragment"
private const val SEARCH_API_KEY = BuildConfig.API_KEY
private const val POPULAR_ARTICLES_URL =
    "https://api.nytimes.com/svc/mostpopular/v2/viewed/1.json?api-key=${SEARCH_API_KEY}"

class PopularArticleListFragment : Fragment() {

    // List of popular articles and the RecyclerView
    private val articles = mutableListOf<PopularArticle>()
    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var articleAdapter: PopularArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_popular_article_list, container, false)


        val layoutManager = LinearLayoutManager(context)
        articlesRecyclerView = view.findViewById(R.id.popularArticle_recycler_view)
        articlesRecyclerView.layoutManager = layoutManager
        articlesRecyclerView.setHasFixedSize(true)

        // Set up the adapter with the list of articles
        articleAdapter = PopularArticleAdapter(view.context, articles)
        articlesRecyclerView.adapter = articleAdapter

        return view
    }

    companion object {
        fun newInstance(): PopularArticleListFragment {
            return PopularArticleListFragment()
        }
    }


    private fun fetchArticles() {
        val client = AsyncHttpClient()
        client.get(POPULAR_ARTICLES_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch articles: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched articles: $json")
                try {

                    val jsonParser = Json { ignoreUnknownKeys = true }


                    val parsedJson = jsonParser.decodeFromString(PopularArticlesResponse.serializer(), json.jsonObject.toString())
                    Log.i(TAG, "parsedJson: $parsedJson")


                    parsedJson.results?.let { list ->
                        articles.addAll(list)
                        articleAdapter.notifyDataSetChanged()
                    }

                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }

        })
    }

    // Fetch the articles when the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchArticles()  // Fetch articles from API
    }
}
