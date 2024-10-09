package com.sherpa.jokepro

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
class Movie {

    companion object {


        fun createJoke(unifiedJoke: String): Movie {
            val joke = Movie()
            val jokeQuestion = unifiedJoke.substringBefore("?").trim() + "?"
            val jokeAnswer = unifiedJoke.substringAfter("?").trim()
            joke.jokeQuestion = jokeQuestion
            joke.jokeAnswer = jokeAnswer
            joke.jokeLiked = false
            return joke
        }


    }

    var jokeQuestion: String? = null
    var jokeAnswer: String? = null
    var jokeLiked: Boolean = false
    fun toggleLike() {
        jokeLiked = !jokeLiked
    }


}

@Keep
@Serializable
data class Joke(
    @SerialName("jokeQuestion")
    val jokeQuestion: String?,
    @SerialName("jokeAnswer")
    val jokeAnswer: String?,
    @SerialName("jokeLiked")
    val jokeLiked: Boolean
) : java.io.Serializable

