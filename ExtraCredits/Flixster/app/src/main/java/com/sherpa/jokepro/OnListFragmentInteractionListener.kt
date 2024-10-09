package com.sherpa.jokepro




/**
 * This interface is used by the [JokesRecyclerViewAdapter] to ensure
 * it has an appropriate Listener.
 *
 * In this app, it's implemented by [JokesFragment]
 */
interface OnListFragmentInteractionListener {
    fun onItemClick(item: Joke)
}
