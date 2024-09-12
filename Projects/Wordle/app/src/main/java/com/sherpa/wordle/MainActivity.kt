package com.sherpa.wordle


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val guessTextIDs: List<Int> = listOf(R.id.guess1_right_top_text, R.id.guess2_right_top_text, R.id.guess3_right_top_text)
        val guessCheckTextIDs: List<Int> = listOf(R.id.guess1_right_bottom_text, R.id.guess2_right_bottom_text, R.id.guess3_right_bottom_text)

        val listOfTupleOfTextViews: MutableList<Pair<TextView, TextView>> = mutableListOf()

        for (i in guessTextIDs.indices) {
            val topTextView = findViewById<TextView>(guessTextIDs[i])
            val bottomTextView = findViewById<TextView>(guessCheckTextIDs[i])
            if (topTextView != null && bottomTextView != null) {
                listOfTupleOfTextViews.add(Pair(topTextView, bottomTextView))
            }
        }

        val numOfChances: Int = 3
        val helperClass = FourLetterWordList
        val wordLength: Int = 4
//        var wordToGuess = helperClass.getRandomFourLetterWord()
        var wordToGuess = "PLAY"
        var currentTry: Int = 0

        /**
         * Parameters / Fields:
         *   wordToGuess : String - the target word the user is trying to guess
         *   guess : String - what the user entered as their guess
         *
         * Returns a String of 'O', '+', and 'X', where:
         *   'O' represents the right letter in the right place
         *   '+' represents the right letter in the wrong place
         *   'X' represents a letter not in the target word
         */
        fun checkGuess(guess: String) : String {
            var result = ""
            for (i in 0..3) {
                if (guess[i] == wordToGuess[i]) {
                    result += "O"
                }
                else if (guess[i] in wordToGuess) {
                    result += "+"
                }
                else {
                    result += "X"
                }
            }
            return result
        }
//        Get reference of the button, and the EachGuessText
        val guessButton = findViewById<Button>(R.id.guessCheck)
        val simpleEditText = findViewById<EditText>(R.id.et_simple)
        val restartButton = findViewById<Button>(R.id.restartButton)
        val revealText = findViewById<TextView>(R.id.revealWord)

        val initialColor: Int = Color.BLUE
        guessButton.setBackgroundColor(initialColor)

        restartButton.setOnClickListener {
            wordToGuess = helperClass.getRandomFourLetterWord()
            currentTry = 0

            for (i in guessTextIDs.indices) {
                listOfTupleOfTextViews[i].first.text = ""
                listOfTupleOfTextViews[i].second.text = ""
            }

            simpleEditText.isEnabled = true
            simpleEditText.hint = "Enter any 4 letter word!"
            guessButton.isEnabled = true
            restartButton.visibility = View.INVISIBLE
            guessButton.setBackgroundColor(initialColor)
            revealText.visibility = View.INVISIBLE
        }

        guessButton.setOnClickListener {
            val userGuess = simpleEditText.text.toString().uppercase()

            if (currentTry == 3) {
                Toast.makeText(this, "Out of Tries!\n Womp Womp", Toast.LENGTH_LONG).show()

            }
            else if (userGuess.length != wordLength) {
                Toast.makeText(this, "Please Enter 4 Letter Word", Toast.LENGTH_LONG).show()
            }
            else{

                val result = checkGuess(userGuess)
                val guessTextView = listOfTupleOfTextViews[currentTry].first
                val guessCheckTextView = listOfTupleOfTextViews[currentTry].second


                guessTextView.text = userGuess
                guessCheckTextView.text = result
                revealText.visibility = View.VISIBLE
                if (result == "OOOO") {
                    guessButton.isEnabled = false // Disables the button
                    guessButton.setBackgroundColor(Color.parseColor("#90EE90"))
                    simpleEditText.setText("")
                    simpleEditText.hint = "You so SMART!"
                    simpleEditText.isEnabled = false
                    restartButton.visibility = View.VISIBLE
                    val messageWordResponse: String = "You Guessed it Right! \n   '$wordToGuess' is Correct"
                    revealText.text = messageWordResponse
                }
                else if (result.contains("+")){

                    revealText.text = "You have some letters there"
                }
                else if (result.contains("O") ){
                    revealText.text = "You have some letters in right positions"
                }
                else {
                    revealText.text = "Try Again!"
                }

                currentTry++
                if (currentTry == numOfChances && result != "OOOO") {
                    guessButton.isEnabled = false // Disables the button
                    guessButton.setBackgroundColor(Color.GRAY)
                    simpleEditText.setText("")
                    simpleEditText.hint = "Out of Tries!"
                    simpleEditText.isEnabled = false
                    restartButton.visibility = View.VISIBLE
                    revealText.visibility = View.VISIBLE
                    val correctWordResponse: String = "Correct Word was $wordToGuess"
                    revealText.text = correctWordResponse
                }
            }
        }







    }
}