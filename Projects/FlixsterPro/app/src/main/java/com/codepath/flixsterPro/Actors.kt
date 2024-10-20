package com.sherpa.flixsterPro
import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Keep
@Serializable
data class ActorResults(
    @SerialName("results")
    val Actors: List<Actor>?
)

@Keep
@Serializable
data class Actor(
    @SerialName("original_name")
    val originalName: String?,
    @SerialName("profile_path")
    val profilePath: String?,
    @SerialName("popularity")
    val popularity: Double?,
    @SerialName("gender")
    val gender: Int?,


): java.io.Serializable {
    val profileImagePath = "https://image.tmdb.org/t/p/w500/${profilePath}"
}

