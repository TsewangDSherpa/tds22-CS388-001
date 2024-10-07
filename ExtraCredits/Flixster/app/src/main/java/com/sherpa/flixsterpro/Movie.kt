package com.sherpa.flixsterpro
import com.google.gson.annotations.SerializedName

/**
 * The Model for storing a single Movie
 *
 * SerializedName tags MUST match the JSON response for the
 * object to correctly parse with the gson library.
 */
class Movie {

    @JvmField
    @SerializedName("title")
    var title: String? = null

    @JvmField
    @SerializedName("poster_path")
    var poster_path: String? = null;

    @JvmField
    @SerializedName("overview")
    var overview: String? = null

    @JvmField
    @SerializedName("vote_average")
    var vote_average: Float? = 0.0f

    @JvmField
    @SerializedName("release_date")
    var release_date: String? = null

    fun getFullPosterUrl(): String? {
        return poster_path?.let { "https://image.tmdb.org/t/p/w500/$it" }
    }
}