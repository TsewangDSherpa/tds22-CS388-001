package com.sherpa.thursdaynighttrivia

import android.content.Context
import org.bson.BsonArray
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.random.Random




class Trivias {
    private val triviaList = mutableListOf<Trivia>()
    private val addedQuestions = mutableSetOf<String>()

    // Add a trivia question to the list
    fun addTrivia(trivia: Trivia) {
        triviaList.add(trivia)
    }

    fun getUniqueTrivia() : List<Trivia> {
        val uniqueTrivia = mutableSetOf<Trivia>()
        if (addedQuestions.size >= 50){
            resetTriviaList()
        }

        while (uniqueTrivia.size < 5 && uniqueTrivia.size < triviaList.size) {
            val randomIndex = Random.nextInt(0, triviaList.size)
            val trivia = triviaList[randomIndex]

            if (addedQuestions.add(trivia.question)) {
                uniqueTrivia.add(trivia)
            }
        }

        return uniqueTrivia.toList()
    }

    fun resetTriviaList() {
        addedQuestions.clear()
    }

    fun loadTriviaFromJson(context: Context) {
        try {
            val inputStream = context.resources.openRawResource(R.raw.trivias)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val json = reader.use { it.readText() }
            val bsonArray = BsonArray.parse(json)

            bsonArray.forEach { bsonElement ->
                val document = bsonElement.asDocument()
                val question = document.getString("question").value
                val answers = document.getArray("answers").map { it.asString().value }.toTypedArray()
                val correctAnswer = document.getString("correctAnswer").value

                addTrivia(Trivia(question, answers, correctAnswer))
            }
        } catch (e: Exception) {
            // Handle exceptions like file not found, parsing errors, etc.
            e.printStackTrace()
        }
    }
}


data class Trivia (
    var question: String,
    var answers: Array<String>,
    var correctAnswer: String
): java.io.Serializable {

    fun checkAnswer(answered: String): Boolean {
        return answered == correctAnswer
    }
}
