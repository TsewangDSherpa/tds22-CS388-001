package com.sherpa.wordle

import android.util.Log

object FourLetterWordList {
    // List of most common 4 letter words from: https://7esl.com/4-letter-words/

    private val colors = listOf("Blue", "Gray", "Gold", "Teal", "Pink", "Aqua", "Cyan", "Moss", "Burg", "Rose")
    private val animals = listOf("Wolf", "Bear", "Lion", "Hawk", "Frog", "Duck", "Crab", "Deer", "Mole", "Puma")
    private val foods = listOf("Corn", "Milk", "Beef", "Nuts", "Cake", "Rice", "Yams", "Pork", "Lamb", "Tuna")
    private val objects = listOf("Book", "Lamp", "Desk", "Pen", "Vase", "Card", "Ring", "Door", "Bowl", "Box")
    private val nature = listOf("Rock", "Hill", "Leaf", "Moss", "Tree", "Rain", "Snow", "Dune", "Lake", "Wind")

    private val allLists = listOf(colors, animals, foods, objects, nature)

    var category = "foods"
    // Returns a random four letter word from the list in all caps
    fun getRandomFourLetterWord(): String {
        var listIndex = 0
        when (category) {
            "colors" -> {
                listIndex = 0
            }
            "animals" -> {
                listIndex = 1
            }
            "foods" -> {
                listIndex = 2
            }
            "objects" -> {
                listIndex = 3
            }
            "nature" -> {
                listIndex = 4
            }
        }

        val chosenWordList = allLists[listIndex]
        val randomNumber = chosenWordList.indices.random()

        return chosenWordList[randomNumber].uppercase()
    }


}