package com.codepath.articlesearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.articlesearch.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.serialization.json.Json

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private const val TAG = "MainActivity/"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val fragmentManager: FragmentManager = supportFragmentManager

        // Define your fragments
        val bestSellerBooksFragment: Fragment = BestSellerBooksFragment()
        val articleListFragment: Fragment = ArticleListFragment()
        val popularArticleListFragment: Fragment = PopularArticleListFragment()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // Handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_books -> bestSellerBooksFragment
                R.id.nav_articles -> articleListFragment
                R.id.nav_home -> popularArticleListFragment
                else -> bestSellerBooksFragment
            }
            replaceFragment(fragment)
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.nav_home
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.article_frame_layout, fragment)
        fragmentTransaction.commit()
    }
}