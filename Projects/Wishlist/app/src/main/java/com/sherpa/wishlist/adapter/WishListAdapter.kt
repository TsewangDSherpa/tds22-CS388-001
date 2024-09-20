package com.sherpa.wishlist.adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sherpa.wishlist.R
import com.sherpa.wishlist.model.Wishlist
import java.util.Locale

class WishListAdapter(private val wishList: ArrayList<Wishlist>) : RecyclerView.Adapter<WishListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView
        val priceTextView: TextView
        val urlTextView: TextView

        init {
            nameTextView = itemView.findViewById(R.id.item_name)
            priceTextView = itemView.findViewById(R.id.item_price)
            urlTextView = itemView.findViewById(R.id.item_url)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val wishListView = inflater.inflate(R.layout.item_wishlist, parent, false)
        return ViewHolder(wishListView)
    }

    override fun getItemCount(): Int {
        return wishList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Wishlist = wishList[position]
        holder.nameTextView.text = item.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val price = "$%.2f".format(item.price)
        holder.priceTextView.text = price
        holder.urlTextView.text = item.url
        holder.itemView.setOnLongClickListener {
            wishList.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            true
        }

        holder.urlTextView.setOnClickListener {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                ContextCompat.startActivity(it.context, browserIntent, null)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(it.context, "Invalid URL for " + item.name, Toast.LENGTH_LONG).show()
            }
        }



    }
}