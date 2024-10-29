package com.codepath.articlesearch

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

const val ARTICLE_EXTRA = "ARTICLE_EXTRA"

class ArticleAdapter(
    private val context: Context,
    private val displayArticles: List<DisplayArticle>,
    private val onToggleChange: (Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ARTICLE = 0
    private val VIEW_TYPE_TOGGLE = 1

    override fun getItemViewType(position: Int): Int {
        return if (position < displayArticles.size) VIEW_TYPE_ARTICLE else VIEW_TYPE_TOGGLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ARTICLE) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false)
            ArticleViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_toggle, parent, false)
            ToggleViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArticleViewHolder) {
            val article = displayArticles[position]
            holder.bind(article)
        } else if (holder is ToggleViewHolder) {
            holder.bind(onToggleChange)
        }
    }

    override fun getItemCount() = displayArticles.size + 1 // +1 for the toggle

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val mediaImageView = itemView.findViewById<ImageView>(R.id.mediaImage)
        private val titleTextView = itemView.findViewById<TextView>(R.id.mediaTitle)
        private val abstractTextView = itemView.findViewById<TextView>(R.id.mediaAbstract)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(displayArticle: DisplayArticle) {
            titleTextView.text = displayArticle.headline
            abstractTextView.text = displayArticle.abstract

            Glide.with(context)
                .load(displayArticle.mediaImageUrl)
                .into(mediaImageView)
        }

        override fun onClick(v: View?) {
            val article = displayArticles[absoluteAdapterPosition]
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(ARTICLE_EXTRA, article)
            context.startActivity(intent)
        }
    }

    inner class ToggleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val toggleSwitch: Switch = itemView.findViewById(R.id.switch_cache)

        fun bind(onToggleChange: (Boolean) -> Unit) {
            val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
            toggleSwitch.isChecked = sharedPreferences.getBoolean("cache_articles", true)

            toggleSwitch.setOnCheckedChangeListener { _, isChecked ->
                // Save the new preference state
                sharedPreferences.edit().putBoolean("cache_articles", isChecked).apply()
                onToggleChange(isChecked)
            }
        }
    }
}
