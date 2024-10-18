package com.sherpa.thursdaynighttrivia

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TriviaDetailActivity : AppCompatActivity() {
    private lateinit var triviaListQuestions: ArrayList<String>
    private lateinit var triviaListAnswers: ArrayList<String>
    private lateinit var triviaListCorrect: ArrayList<String>

    private var currentQuestionIndex = 0
    private var correctAnswersCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trivia_detail)

        // Get the ArrayLists from the intent
        triviaListQuestions = intent.getStringArrayListExtra("ListQuestions") ?: ArrayList()
        triviaListAnswers = intent.getStringArrayListExtra("ListAnswers") ?: ArrayList()
        triviaListCorrect = intent.getStringArrayListExtra("ListCorrect") ?: ArrayList()
        currentQuestionIndex = intent.getIntExtra("currentQuestionIndex", 0)
        correctAnswersCount = intent.getIntExtra("correctAnswersCount", 0) // Retrieve the count of correct answers

        // Check if trivia lists are not empty
        if (triviaListQuestions.isNotEmpty()) {
            displayCurrentQuestion()

        } else {

            Toast.makeText(this, "No trivia questions available.", Toast.LENGTH_SHORT).show()
            finish() // Close activity if no questions
        }
    }

    private fun displayCurrentQuestion() {
        // Get the current trivia question and answers
        val currentQuestion = triviaListQuestions[currentQuestionIndex]
        val currentAnswers = convertStringToArray(triviaListAnswers[currentQuestionIndex])
        val correctAnswerString = triviaListCorrect[currentQuestionIndex]

        // Update UI with the current question
        val questionTextView = findViewById<TextView>(R.id.questionText)
        questionTextView.text = currentQuestion

        // Set up answer buttons
        val answerButtons = listOf<TextView>(
            findViewById(R.id.choice1),
            findViewById(R.id.choice2),
            findViewById(R.id.choice3),
            findViewById(R.id.choice4)
        )

        // Set button texts to the trivia answers
        answerButtons.forEachIndexed { index, button ->
            button.text = currentAnswers[index].trim()
            button.setOnClickListener {
                handleAnswerSelection(correctAnswerString, button.text.toString().trim(), button)
            }
        }

        // Display progress
        val progressTextView = findViewById<TextView>(R.id.progressTextView)
        progressTextView.text = "Question ${currentQuestionIndex + 1} of ${triviaListQuestions.size}"
    }

    private fun convertStringToArray(correctAnswerString: String): List<String> {
        // Remove brackets and split the string into an array
        return correctAnswerString.trim().removePrefix("[").removeSuffix("]").split(",").map { it.trim() }
    }

    private fun handleAnswerSelection(correctAnswers: String, selectedAnswer: String, button: TextView) {
        val isCorrect = correctAnswers == selectedAnswer
        if (isCorrect) {
            correctAnswersCount++ // Increment correct answers count
            button.setBackgroundResource(R.color.green)
            button.setTextColor(Color.WHITE)
        } else {
            button.setBackgroundResource(R.color.red)
            button.setTextColor(Color.WHITE)
        }

        // Move to the next question
        currentQuestionIndex++

        // Check if there are more questions
        if (currentQuestionIndex < triviaListQuestions.size) {
            // Pass the trivia lists and correct answers count to the next activity
            val intent = Intent(this, TriviaDetailActivity::class.java).apply {
                putExtra("ListQuestions", triviaListQuestions)
                putExtra("ListAnswers", triviaListAnswers)
                putExtra("ListCorrect", triviaListCorrect)
                putExtra("currentQuestionIndex", currentQuestionIndex) // Pass the updated index
                putExtra("correctAnswersCount", correctAnswersCount) // Pass the correct answers count
            }
            startActivity(intent)
            finish() // Close current activity
        } else {
            finishQuiz() // Finish the quiz if there are no more questions
        }
    }

    private fun finishQuiz() {
        val intent = Intent(this, GameResultActivity::class.java).apply {
            putExtra("correctAnswersCount", correctAnswersCount)
        }
        startActivity(intent)
        finish()
    }
}
