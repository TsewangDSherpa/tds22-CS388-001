package com.sherpa.bitfit

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        val fragmentManager: FragmentManager = supportFragmentManager

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        // Set up window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sleep_activity_fragment = SleepActivityFragment()
        val input_entry_fragment = InputEntryFragment()
        val dashboard_fragment = DashboardFragment()

        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.nav_home -> fragment = sleep_activity_fragment
                R.id.nav_addEntry -> fragment = input_entry_fragment
                R.id.nav_graph -> fragment = dashboard_fragment
            }
            replaceFragment(fragment)
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.nav_home
//        val intent = Intent(this, SleepActivity::class.java)
//        startActivity(intent)
//        finish()
    }

    private fun replaceFragment(inputtedFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.article_frame_layout, inputtedFragment)
        fragmentTransaction.commit()
    }


}
