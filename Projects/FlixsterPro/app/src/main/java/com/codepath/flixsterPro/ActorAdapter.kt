package com.sherpa.flixsterPro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

private const val TAG = "ActorAdapter"

class ActorAdapter(private val context: Context, private val actors: List<Actor>) :
    RecyclerView.Adapter<ActorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_actor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actor = actors[position]
        holder.bind(actor)
    }

    override fun getItemCount() = actors.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val mediaImageView: ImageView = itemView.findViewById(R.id.actorImage)
        private val actorNameTextView: TextView = itemView.findViewById(R.id.actorName)
        private val actorPopularityTextView: TextView = itemView.findViewById(R.id.actorPopularity)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(actor: Actor) {
            actorNameTextView.text = actor.originalName
            actorPopularityTextView.text = "Popularity: ${actor.popularity}"

            Glide.with(context)
                .load(actor.profileImagePath)
                .placeholder(R.drawable.movieplaceholder)
                .centerCrop()
                .centerInside()
                .circleCrop()
                .into(mediaImageView)
        }

        override fun onClick(p0: View?) {
            val actor = actors[absoluteAdapterPosition]
           Toast.makeText(context, if (actor.gender == 1) "She/Her" else "He/Him", Toast.LENGTH_SHORT).show()
        }


    }
}
