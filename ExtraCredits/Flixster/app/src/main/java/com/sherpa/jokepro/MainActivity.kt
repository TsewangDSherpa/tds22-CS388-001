package com.sherpa.jokepro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle



import com.sherpa.jokepro.R.id



/**
 * The MainActivity for the BestSellerList app.
 * Launches a [JokesFragment].
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val supportFragmentManager = supportFragmentManager
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(id.content, JokesFragment(), null).commit()
    }
}