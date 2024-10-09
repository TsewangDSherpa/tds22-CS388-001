package com.sherpa.jokepro

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Joke(
    @SerialName("jokeQuestion")
    val jokeQuestion: String,
    @SerialName("jokeAnswer")
    val jokeAnswer: String,
    @SerialName("jokeLiked")
    var jokeLiked: Boolean = false
) : java.io.Serializable {

    companion object {
        fun createJoke(unifiedJoke: String): Joke {
            val jokeQuestion = unifiedJoke.substringBefore("?").trim() + "?"
            val jokeAnswer = unifiedJoke.substringAfter("?").trim()
            return Joke(jokeQuestion, jokeAnswer)
        }
    }

    fun toggleLike() {
        jokeLiked = !jokeLiked
    }
}
