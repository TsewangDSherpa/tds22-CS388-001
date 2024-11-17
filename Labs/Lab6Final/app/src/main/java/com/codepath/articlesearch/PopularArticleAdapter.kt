package com.codepath.articlesearch

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

const val POPULAR_ARTICLE_EXTRA = "POPULAR_ARTICLE_EXTRA"
private const val TAG = "PopularArticleAdapter"

class PopularArticleAdapter(
    private val context: Context,
    private val articles: List<PopularArticle>
) : RecyclerView.Adapter<PopularArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.popular_item_article, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount() = articles.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val mediaImageView: ImageView = itemView.findViewById(R.id.mediaImage)
        private val titleTextView: TextView = itemView.findViewById(R.id.mediaTitle)
        private val abstractTextView: TextView = itemView.findViewById(R.id.mediaAbstract)
        private val learnMoreButton: Button = itemView.findViewById(R.id.media_Btn)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(article: PopularArticle) {
            // Set article title and abstract
            titleTextView.text = article.title
            abstractTextView.text = article.abstract


            val lastImageUrl = article.media
                ?.lastOrNull()
                ?.mediaMetadata
                ?.lastOrNull()
                ?.url


            if (lastImageUrl != null) {
                Glide.with(context)
                    .load(lastImageUrl)
                    .into(mediaImageView)
            } else {
                mediaImageView.setImageResource(R.drawable.baseline_article_24) // Fallback image
            }

            learnMoreButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                itemView.context.startActivity(intent)
            }
        }

        override fun onClick(v: View?) {

            val article = articles[absoluteAdapterPosition]

        }
    }
}
