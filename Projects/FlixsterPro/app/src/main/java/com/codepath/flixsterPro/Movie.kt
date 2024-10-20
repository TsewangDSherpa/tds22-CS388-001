package com.sherpa.flixsterPro
import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Keep
@Serializable
data class Results(
    @SerialName("results")
    val Movies: List<Movie>?
)

@Keep
@Serializable
data class Movie(
    @SerialName("original_title")
    val originalTitle: String?,
    @SerialName("overview")
    val overview: String?,
    @SerialName("popularity")
    val popularity: Double?,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("vote_average")
    val voteAverage: Double?,
    @SerialName("backdrop_path")
    val backdropPath: String?


): java.io.Serializable {
    val posterImageUrl = "https://image.tmdb.org/t/p/w500/${posterPath}"
    val backdropImageUrl = "https://image.tmdb.org/t/p/w500/${backdropPath}"
}

