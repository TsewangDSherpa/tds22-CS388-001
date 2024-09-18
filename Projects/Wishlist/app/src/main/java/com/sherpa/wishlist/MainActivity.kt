package com.sherpa.wishlist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.sherpa.wishlist.adapter.WishListAdapter
import com.sherpa.wishlist.model.Wishlist

class MainActivity : AppCompatActivity() {
    lateinit var wishLists: List<Wishlist>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wishitems)

        val wishListRv = findViewById<RecyclerView>(R.id.rvWishItems)
        wishLists = Wishlist.initWishList()


        val adapter = WishListAdapter(wishLists as ArrayList<Wishlist>)
        wishListRv.adapter = adapter

        wishListRv.layoutManager = LinearLayoutManager(this)

        val addButton = findViewById<Button>(R.id.addButton)
        val nameEditText = findViewById<TextInputEditText>(R.id.nameEditText)
        val priceEditText = findViewById<TextInputEditText>(R.id.priceEditText)
        val urlEditText = findViewById<TextInputEditText>(R.id.urlEditText)
        fun View.hideKeyboard() {
            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(windowToken, 0)
        }
        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val price = priceEditText.text.toString().toDouble()
            val url = urlEditText.text.toString()


            if (name.isNotEmpty() && price > 0 && url.isNotEmpty()) {
                Wishlist.addToWishList(name, price, url)
                adapter.notifyDataSetChanged()
                nameEditText.text?.clear()
                priceEditText.text?.clear()
                urlEditText.text?.clear()
                it.hideKeyboard()
            }
        }
    }
}