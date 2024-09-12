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
        button.setOnClickListener {
            //            Toast.makeText(it.context, "Clicked Button! $counter", Toast.LENGTH_SHORT).show()
            counter++
            countText.text = counter.toString()

            if (counter >= 100) {
                upgradebutton.visibility = View.VISIBLE

                upgradebutton.setOnClickListener {
//                    button.text = "Add 2"
                    button.setOnClickListener {
                        counter += 2
                        countText.text = counter.toString()
                    }
                    Toast.makeText(this, "You just Upgraded!\n \tWhoo!", Toast.LENGTH_LONG).show()
                upgradebutton.visibility = View.INVISIBLE
                }
            }


        }




    }
}