package com.sherpa.wishlist.model

class Wishlist (val name: String, val price: Double, val url: String) {
    
    companion object {
        private var wishListContainer = ArrayList<Wishlist>()

        fun initWishList() : ArrayList<Wishlist> {
            return wishListContainer
        }

        fun addToWishList(name: String, price: Double, url: String) : ArrayList<Wishlist> {
            val item = Wishlist(name, price, url)
            wishListContainer.add(item)
            return wishListContainer
        }

    }

}