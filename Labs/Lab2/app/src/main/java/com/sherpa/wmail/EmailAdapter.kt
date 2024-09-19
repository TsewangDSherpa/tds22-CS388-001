package com.sherpa.wmail

import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class EmailAdapter(private val emails: List<Email>) : RecyclerView.Adapter<EmailAdapter.ViewHolder>() {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val senderTextView: TextView
        val titleTextView: TextView
        val summaryTextView: TextView
        val dateTextView: TextView
        val profileImgView: ImageView

        // as you render a row.

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each sub-view
        init {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            senderTextView = itemView.findViewById(R.id.senderTv)
            titleTextView = itemView.findViewById(R.id.titleTv)
            summaryTextView = itemView.findViewById(R.id.summaryTv)
            dateTextView = itemView.findViewById(R.id.dateTv2)
            profileImgView = itemView.findViewById(R.id.imgView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.email_item, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val email = emails[position]

        // Set item views based on views and data model
        holder.senderTextView.text = email.sender
        holder.titleTextView.text = email.title
        holder.summaryTextView.text = email.summary
        holder.dateTextView.text = email.date
        holder.profileImgView.setImageResource(email.picId)

        // Set text style and click listener
        holder.titleTextView.setTypeface(null, if (email.unRead) Typeface.BOLD else Typeface.NORMAL)
        holder.dateTextView.setTypeface(null, if (email.unRead) Typeface.BOLD else Typeface.NORMAL)
        holder.senderTextView.setTypeface(null, if (email.unRead) Typeface.BOLD else Typeface.NORMAL)

        holder.itemView.setOnClickListener {
            email.unRead = !email.unRead
            notifyItemChanged(position) // Notify adapter to refresh the item
        }
    }

    override fun getItemCount(): Int {
        return emails.size
    }
}
