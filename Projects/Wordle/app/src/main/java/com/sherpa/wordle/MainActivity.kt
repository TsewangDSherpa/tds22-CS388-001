package com.sherpa.wordle


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat



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
        var streaks = 0
        val numOfChances= 3
        val helperClass = FourLetterWordList
        val wordLength = 4
        helperClass.category = "colors"
        var wordToGuess = helperClass.getRandomFourLetterWord()
        var currentTry = 0
        val categoryText = findViewById<TextView>(R.id.categoryText)

        fun checkGuess(guess: String) : String {
            var result = ""
            for (i in 0..3) {
                result += if (guess[i] == wordToGuess[i]) {
                    "O"
                } else if (guess[i] in wordToGuess) {
                    "+"
                } else {
                    "X"
                }
            }
            return result
        }
//        Get reference of the button, and the EachGuessText
        val guessButton = findViewById<Button>(R.id.guessCheck)
        val simpleEditText = findViewById<EditText>(R.id.et_simple)
        val restartButton = findViewById<Button>(R.id.restartButton)
        val revealText = findViewById<TextView>(R.id.revealWord)
        val streakText = findViewById<TextView>(R.id.streaksText)

        val initialColor: Int = Color.BLUE
        guessButton.setBackgroundColor(initialColor)

        val colorsText = findViewById<TextView>(R.id.colors)
        val animalsText = findViewById<TextView>(R.id.animals)
        val foodsText = findViewById<TextView>(R.id.foods)
        val objectsText = findViewById<TextView>(R.id.objects)
        val natureText = findViewById<TextView>(R.id.nature)

        val listOfCategories: MutableList<TextView> = mutableListOf(colorsText, animalsText, foodsText, objectsText, natureText)
        val listOfCategoryString: List<String> = listOf("colors", "animals", "foods", "objects", "nature")
        for (i in listOfCategories.indices) {
            listOfCategories[i].setOnClickListener {
                helperClass.category = listOfCategoryString[i]
                val textCategory = "Next Word From : ${helperClass.category.uppercase()}"
                categoryText.text = textCategory
            }
        }


        restartButton.setOnClickListener {

            wordToGuess = helperClass.getRandomFourLetterWord()
            currentTry = 0


            for (i in guessTextIDs.indices) {
                listOfTupleOfTextViews[i].first.text = " "
                listOfTupleOfTextViews[i].second.text = " "
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
            // Hiding the keyboard
            try {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(guessButton.windowToken, 0)
                simpleEditText.setText("")
            }
            catch (e: Exception) {
                Toast.makeText(this, "ERR: Enter 4 Letter Word", Toast.LENGTH_SHORT).show()
            }


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
//                guessCheckTextView.text = result

                val spannable = SpannableString(userGuess)

                for (i in 0..3) {
                    if (result.isNotEmpty() && result[i] == 'O') {
                        spannable.setSpan(ForegroundColorSpan(Color.GREEN), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    else if (result[i] == '+') {
                        spannable.setSpan(ForegroundColorSpan(Color.RED), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }

                }

                guessCheckTextView.text = spannable

                revealText.visibility = View.VISIBLE
                if (result == "OOOO") {
                    streaks++
                    val streakTextShow = "Streaks: $streaks"
                    streakText.text = streakTextShow
                    guessButton.isEnabled = false // Disables the button
                    guessButton.setBackgroundColor(Color.parseColor("#90EE90"))
                    simpleEditText.setText("")
                    simpleEditText.hint = "You so SMART!"
                    simpleEditText.isEnabled = false
                    restartButton.visibility = View.VISIBLE
                    val messageWordResponse = "You Guessed it Right! \n   '$wordToGuess' is Correct"
                    revealText.text = messageWordResponse
                }
                else if (result.contains("+")){

                    revealText.text = getString(R.string.you_have_some_letters_there)
                }
                else if (result.contains("O") ){
                    revealText.text = getString(R.string.you_have_some_letters_in_right_positions)
                }
                else {
                    revealText.text = getString(R.string.try_again)
                }

                currentTry++
                if (currentTry == numOfChances && result != "OOOO") {

                    val streakTextShow = "Streaks: 0"
                    streakText.text = streakTextShow
                    guessButton.isEnabled = false // Disables the button
                    guessButton.setBackgroundColor(Color.GRAY)
                    simpleEditText.setText("")
                    simpleEditText.hint = "Out of Tries!"
                    simpleEditText.isEnabled = false
                    restartButton.visibility = View.VISIBLE
                    revealText.visibility = View.VISIBLE
                    val correctWordResponse = "Correct Word was $wordToGuess"
                    revealText.text = correctWordResponse
                    streaks = 0
                }
            }
        }

    }
}