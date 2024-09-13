package com.example.lab1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.ceil
import kotlin.random.Random

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
        val countText = findViewById<TextView>(R.id.textView)
        var counter = 0
        val upgradebutton = findViewById<Button>(R.id.upgradeBtn)
        val button = findViewById<ImageButton>(R.id.button)
        val textView3 = findViewById<TextView>(R.id.textView3)
        val goalTextView = findViewById<TextView>(R.id.goalText)
        var goal: Int = 100
        var numOfGoalsReached : Int = 0
        button.setOnClickListener {
            //            Toast.makeText(it.context, "Clicked Button! $counter", Toast.LENGTH_SHORT).show()
            val randomStep = Random.nextInt(1, 5)
            counter+= randomStep

            textView3.text = "You took $randomStep steps!"

            countText.text = counter.toString()


            if (counter >= goal) {
                upgradebutton.text = "Take the Upgrade!"
                upgradebutton.visibility = View.VISIBLE
                goal += ceil(goal * 0.5).toInt()
                numOfGoalsReached++
                goalTextView.text = "Goal : $goal Steps \n Reached : $numOfGoalsReached Goals"
                upgradebutton.setOnClickListener {
                    button.background = getDrawable(R.drawable.start)
                    button.setOnClickListener {
                        val randomStep = Random.nextInt(1, 15)
                        counter += randomStep
                        textView3.text = "You took $randomStep steps!"
                        countText.text = counter.toString()
                        if (counter >= goal) {
                            goal += ceil(goal * 0.5).toInt()
                            numOfGoalsReached++
                            goalTextView.text = "Goal : $goal Steps \n Reached : $numOfGoalsReached Goals"
                        }
                        if ( counter % 11 == 0 || counter % 7 == 0 ) {
                            var slippedSteps = Random.nextInt(3,15)
                            counter -= slippedSteps
                            if (counter < 0) {
                                slippedSteps = slippedSteps + counter
                                counter = 0
                            }
                            textView3.text = "You slipped $slippedSteps steps!"

                            countText.text = counter.toString()
                        }

                    }
                    Toast.makeText(this, "You just Upgraded!\n \tWhoo!", Toast.LENGTH_LONG).show()

                upgradebutton.visibility = View.INVISIBLE
                }
            }



            if ( counter % 11 == 0 || counter % 7 == 0 ) {
                var slippedSteps = Random.nextInt(3,15)
                counter -= slippedSteps
                if (counter < 0) {
                    slippedSteps = slippedSteps + counter
                    counter = 0
                }
                textView3.text = "You slipped $slippedSteps steps!"
                countText.text = counter.toString()
            }

        }
    }
}